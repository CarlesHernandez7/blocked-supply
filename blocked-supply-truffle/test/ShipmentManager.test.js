const ShipmentManager = artifacts.require("ShipmentManager");

contract("ShipmentManager", (accounts) => {
    let shipmentManager;
    const manager = accounts[1];
    const user = accounts[2];

    before(async () => {
        shipmentManager = await ShipmentManager.deployed();
    });

    it("should register a new user", async () => {
        await shipmentManager.registerUser("Manager", "manager", "manager@example.com", { from: manager });

        const userDetails = await shipmentManager.getUser(manager);
        assert.equal(userDetails.name, "Manager", "Name should be Manager");
        assert.equal(userDetails.role, "manager", "Role should be manager");
        assert.equal(userDetails.email, "manager@example.com", "Email should match");
    });

    it("should allow manager to create a shipment", async () => {
        await shipmentManager.createShipment(
            "Product A",
            "Description of Product A",
            "Origin City",
            "Destination City",
            10,
            50,
            { from: manager }
        );

        const shipment = await shipmentManager.getShipment(0);
        assert.equal(shipment.productName, "Product A", "Product name should be Product A");
        assert.equal(shipment.currentOwner, manager, "Current owner should be the manager");
    });

    it("should update the shipment state", async () => {
        await shipmentManager.updateShipment(0, "In Transit", { from: manager });

        const shipment = await shipmentManager.getShipment(0);
        assert.equal(shipment.currentState, "In Transit", "State should be updated to In Transit");
    });

    it("should not allow unauthorized users to update shipment state", async () => {
        const shipment = await shipmentManager.getShipment(0);
        assert.notEqual(shipment.currentOwner, user, "Test setup: The user should not be the current owner");
    
        try {
            await shipmentManager.updateShipment(0, "Delivered", { from: user });
            assert.fail("Unauthorized user should not be able to update the shipment");
        } catch (error) {
            assert.include(
                error.message,
                "Only the current owner can perform this action.",
                "Should revert with correct message"
            );
        }
    });

    it("should transfer ownership of a shipment", async () => {
        await shipmentManager.transferOwnership(0, user, { from: manager });

        const shipment = await shipmentManager.getShipment(0);
        assert.equal(shipment.currentOwner, user, "New owner should be the user");
    });

    it("should retrieve transfer history", async () => {
        const transfers = await shipmentManager.getTransferHistory(0);
        assert.equal(transfers.length, 1, "There should be one transfer record");
        assert.equal(transfers[0].newOwner, user, "The new owner in the transfer should be the user");
    });
});
