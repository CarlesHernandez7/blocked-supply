const ShipmentManagement = artifacts.require("ShipmentManagement");
const { expectRevert } = require('@openzeppelin/test-helpers');

contract("ShipmentManagement", (accounts) => {
    const [manager, operator, viewer] = accounts;

    let contract;

    beforeEach(async () => {
        contract = await ShipmentManagement.new();

        // Register users with roles
        await contract.registerUser("Manager User", ["Manager"], "manager@example.com", { from: manager });
        await contract.registerUser("Operator User", ["Operator"], "operator@example.com", { from: operator });
        await contract.registerUser("Viewer User", ["Viewer"], "viewer@example.com", { from: viewer });
    });

    it("should register users correctly", async () => {
        const managerDetails = await contract.getUser(manager, { from: manager });
        assert.equal(managerDetails.name, "Manager User", "Manager name mismatch");
        assert.equal(managerDetails.roles[0], "Manager", "Manager role mismatch");

        const operatorDetails = await contract.getUser(operator, { from: operator });
        assert.equal(operatorDetails.name, "Operator User", "Operator name mismatch");
        assert.equal(operatorDetails.roles[0], "Operator", "Operator role mismatch");

        const viewerDetails = await contract.getUser(viewer, { from: viewer });
        assert.equal(viewerDetails.name, "Viewer User", "Viewer name mismatch");
        assert.equal(viewerDetails.roles[0], "Viewer", "Viewer role mismatch");
    });

    it("should allow manager to create shipments", async () => {
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: manager });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        const shipment = await contract.getShipment(shipmentId, { from: manager });
        assert.equal(shipment.productName, "Product A", "Shipment product name mismatch");
        assert.equal(shipment.origin, "Factory", "Shipment origin mismatch");
        assert.equal(shipment.destination, "Warehouse", "Shipment destination mismatch");
        assert.equal(shipment.units, 100, "Shipment units mismatch");
        assert.equal(shipment.weight, 200, "Shipment weight mismatch");
        assert.equal(shipment.currentState.toString(), "0", "Shipment state mismatch"); // Created = 0
    });

    it("should not allow non-manager to create shipments", async () => {
        // Non-manager tries to create a shipment
        await expectRevert(
            contract.createShipment("ProductA", "Description", "Origin", "Destination", 10, 100, { from: operator }),
            "Not authorized for this role."
        );
    });

    it("should allow the shipment owner to transfer shipment", async () => {
        // Create shipment by manager
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: manager });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Transfer shipment to operator
        await contract.shipmentTransfer(shipmentId, operator, 1, "Transferred to operator", { from: manager });

        const shipment = await contract.getShipment(shipmentId, { from: operator });
        assert.equal(shipment.currentOwner, operator, "Shipment owner mismatch after transfer");
        assert.equal(shipment.currentState.toString(), "1", "Shipment state mismatch after transfer"); // InTransit = 1
    });

    it("should not allow non-owner to transfer shipment", async () => {
        // Create shipment by manager
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: manager });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Attempt transfer by non-owner
        await expectRevert(
            contract.shipmentTransfer(shipmentId, viewer, 2, "Unauthorized transfer", { from: operator }),
            "Only the current owner can perform this action."
        );
    });

    it("should return correct transfer history", async () => {
        // Create shipment by manager
        const tx = await contract.createShipment("Product A", "Description A", "Factory", "Warehouse", 100, 200, { from: manager });

        const shipmentId = tx.logs[0].args.shipmentId.toNumber();

        // Transfer shipment to operator
        await contract.shipmentTransfer(shipmentId, operator, 1, "First transfer", { from: manager });

        // Transfer shipment to viewer
        await contract.shipmentTransfer(shipmentId, viewer, 2, "Second transfer", { from: operator });

        const history = await contract.getTransferHistory(shipmentId, { from: viewer });
        assert.equal(history.length, 2, "Transfer history length mismatch");
        assert.equal(history[0].newShipmentOwner, operator, "First transfer owner mismatch");
        assert.equal(history[1].newShipmentOwner, viewer, "Second transfer owner mismatch");
    });
});