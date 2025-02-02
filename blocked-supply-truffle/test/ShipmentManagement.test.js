const ShipmentManagement = artifacts.require("ShipmentManagement");
const { expectRevert } = require('@openzeppelin/test-helpers');

contract("ShipmentManagement", (accounts) => {
    const [admin, customer, transporter, warehouse] = accounts;

    let contract;

    beforeEach(async () => {
        contract = await ShipmentManagement.new();

        await contract.registerUser("Admin User", [0], "admin@example.com", { from: admin }); // ADMIN = 0
        await contract.registerUser("Customer User", [1], "customer@example.com", { from: customer }); // CUSTOMER = 1
        await contract.registerUser("Transporter User", [2], "transporter@example.com", { from: transporter }); // TRANSPORTER = 2
        await contract.registerUser("Warehouse User", [3], "warehouse@example.com", { from: warehouse }); // WAREHOUSE = 3
    });

    it("should register users correctly", async () => {
        const adminDetails = await contract.getUser(admin, { from: admin });
        assert.equal(adminDetails.username, "Admin User", "Admin name mismatch");
        assert.equal(adminDetails.roles[0].toNumber(), 0, "Admin role mismatch"); // ADMIN = 0

        const customerDetails = await contract.getUser(customer, { from: admin });
        assert.equal(customerDetails.username, "Customer User", "Customer name mismatch");
        assert.equal(customerDetails.roles[0].toNumber(), 1, "Customer role mismatch"); // CUSTOMER = 1

        const transporterDetails = await contract.getUser(transporter, { from: admin });
        assert.equal(transporterDetails.username, "Transporter User", "Transporter name mismatch");
        assert.equal(transporterDetails.roles[0].toNumber(), 2, "Transporter role mismatch"); // TRANSPORTER = 2

        const warehouseDetails = await contract.getUser(warehouse, { from: admin });
        assert.equal(warehouseDetails.username, "Warehouse User", "Warehouse name mismatch");
        assert.equal(warehouseDetails.roles[0].toNumber(), 3, "Warehouse role mismatch"); // WAREHOUSE = 3
    });

    it("should allow admin to create shipments", async () => {
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: admin });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();
        const shipment = await contract.getShipment(shipmentId, { from: admin });

        assert.equal(shipment.name, "Product A", "Shipment name mismatch");
        assert.equal(shipment.origin, "Factory", "Shipment origin mismatch");
        assert.equal(shipment.destination, "Warehouse", "Shipment destination mismatch");
        assert.equal(shipment.units.toNumber(), 100, "Shipment units mismatch");
        assert.equal(shipment.weight.toNumber(), 200, "Shipment weight mismatch");
        assert.equal(shipment.currentState.toString(), "0", "Shipment state mismatch"); // CREATED = 0
    });

    it("should not allow non-admin to create shipments", async () => {
        await expectRevert(
            contract.createShipment("Product A", "Description", "Origin", "Destination", 10, 100, { from: customer }),
            "Not authorized for this role."
        );
    });

    it("should allow the shipment owner to transfer shipment", async () => {
        // Admin creates shipment
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: admin });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Transfer shipment from admin to transporter
        await contract.shipmentTransfer(shipmentId, transporter, 1, "Transferred to transporter", { from: admin });

        const shipment = await contract.getShipment(shipmentId, { from: admin });
        assert.equal(shipment.currentOwner, transporter, "Shipment owner mismatch after transfer");
        assert.equal(shipment.currentState.toString(), "1", "Shipment state mismatch after transfer"); // IN_TRANSIT = 1
    });

    it("should not allow non-owner to transfer shipment", async () => {
        // Admin creates shipment
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: admin });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Attempt transfer by non-owner
        await expectRevert(
            contract.shipmentTransfer(shipmentId, customer, 2, "Unauthorized transfer", { from: transporter }),
            "Only the current owner can perform this action."
        );
    });

    it("should return correct transfer history", async () => {
        // Admin creates shipment
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: admin });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Transfer shipment from admin to transporter
        await contract.shipmentTransfer(shipmentId, transporter, 1, "First transfer", { from: admin });

        // Transfer shipment from transporter to warehouse
        await contract.shipmentTransfer(shipmentId, warehouse, 2, "Second transfer", { from: transporter });

        const history = await contract.getTransferHistory(shipmentId, { from: customer });
        assert.equal(history.length, 2, "Transfer history length mismatch");
        assert.equal(history[0].newShipmentOwner, transporter, "First transfer owner mismatch");
        assert.equal(history[1].newShipmentOwner, warehouse, "Second transfer owner mismatch");
    });

    it("should only allow authorized roles to get shipment details", async () => {
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: admin });
        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Only admin should be able to call getShipment
        await expectRevert(
            contract.getShipment(shipmentId, { from: customer }),
            "Not authorized for this role."
        );
    });

    it("should only allow customers to get transfer history", async () => {
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: admin });
        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        await contract.shipmentTransfer(shipmentId, transporter, 1, "Transferred to transporter", { from: admin });

        // Only customer should be able to call getTransferHistory
        await expectRevert(
            contract.getTransferHistory(shipmentId, { from: transporter }),
            "Not authorized for this role."
        );

        const history = await contract.getTransferHistory(shipmentId, { from: customer });
        assert.equal(history.length, 1, "Transfer history length mismatch");
        assert.equal(history[0].newShipmentOwner, transporter, "Transfer history owner mismatch");
    });
});
