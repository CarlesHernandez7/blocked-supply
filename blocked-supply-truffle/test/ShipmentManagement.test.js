const ShipmentManagement = artifacts.require("ShipmentManagement");

contract("ShipmentManagement", (accounts) => {
    let contract;
    const [admin, customer, transporter, warehouse] = accounts;

    beforeEach(async () => {
        contract = await ShipmentManagement.new();
    });

    it("should register a user with valid roles", async () => {
        const tx = await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });

        assert.equal(tx.logs[0].event, "UserRegistered", "UserRegistered event should be emitted");
        assert.equal(tx.logs[0].args.userAddress, admin, "User address should match");
        assert.equal(tx.logs[0].args.name, "AdminUser", "User name should match");
    });

    it("should not allow duplicate user registration", async () => {
        await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });

        try {
            await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });
            assert.fail("Duplicate registration should not be allowed");
        } catch (error) {
            assert.include(error.message, "User already registered.", "Expected revert message not found");
        }
    });

    it("should allow an admin to create a shipment", async () => {
        await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });

        const tx = await contract.createShipment("Product1", "Desc1", "Origin1", "Destination1", 10, 100, { from: admin });

        assert.equal(tx.logs[0].event, "ShipmentCreated", "ShipmentCreated event should be emitted");
        assert.equal(tx.logs[0].args.productName, "Product1", "Product name should match");
    });

    it("should not allow a non-admin to create a shipment", async () => {
        await contract.registerUser("CustomerUser", ["CUSTOMER"], "customer@example.com", { from: customer });

        try {
            await contract.createShipment("Product1", "Desc1", "Origin1", "Destination1", 10, 100, { from: customer });
            assert.fail("Non-admin user should not be able to create a shipment");
        } catch (error) {
            assert.include(error.message, "Not authorized for this role.", "Expected revert message not found");
        }
    });

    it("should transfer shipment ownership correctly", async () => {
        await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });
        await contract.registerUser("TransporterUser", ["TRANSPORTER"], "transporter@example.com", { from: transporter });

        await contract.createShipment("Product1", "Desc1", "Origin1", "Destination1", 10, 100, { from: admin });

        const tx = await contract.shipmentTransfer(0, transporter, "IN_TRANSIT", "Moving shipment", { from: admin });

        assert.equal(tx.logs[0].event, "ShipmentTransfer", "ShipmentTransfer event should be emitted");
        assert.equal(tx.logs[0].args.newShipmentOwner, transporter, "New owner should match transporter");
    });

    it("should not allow an invalid shipment state transition", async () => {
        await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });

        await contract.createShipment("Product1", "Desc1", "Origin1", "Destination1", 10, 100, { from: admin });

        try {
            await contract.shipmentTransfer(0, transporter, "INVALID_STATE", "Invalid move", { from: admin });
            assert.fail("Invalid state should not be accepted");
        } catch (error) {
            assert.include(error.message, "Invalid shipment state.", "Expected revert message not found");
        }
    });

    it("should fetch shipment details", async () => {
        await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });

        await contract.createShipment("Product1", "Desc1", "Origin1", "Destination1", 10, 100, { from: admin });

        const shipment = await contract.getShipment(0, { from: admin });

        assert.equal(shipment.name, "Product1", "Product name should match");
        assert.equal(shipment.units, 10, "Units should match");
    });

    it("should fetch transfer history correctly", async () => {
        await contract.registerUser("AdminUser", ["ADMIN"], "admin@example.com", { from: admin });
        await contract.registerUser("CustomerUser", ["CUSTOMER"], "customer@example.com", { from: customer });

        await contract.createShipment("Product1", "Desc1", "Origin1", "Destination1", 10, 100, { from: admin });

        await contract.shipmentTransfer(0, customer, "IN_TRANSIT", "Moving shipment", { from: admin });

        const transferHistory = await contract.getTransferHistory(0, { from: customer });

        assert.equal(transferHistory.length, 1, "Should have one transfer record");
        assert.equal(transferHistory[0].newShipmentOwner, customer, "Transfer ownership should be correct");
    });
});
