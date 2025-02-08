const ShipmentManagement = artifacts.require("ShipmentManagement");

contract("ShipmentManagement", (accounts) => {
    let shipmentInstance;
    
    // Test accounts
    const owner = accounts[0];
    const newOwner = accounts[1];

    before(async () => {
        shipmentInstance = await ShipmentManagement.deployed();
    });

    it("should deploy the contract", async () => {
        assert(shipmentInstance.address !== "", "Contract was not deployed");
    });

    it("should create a shipment", async () => {
        let result = await shipmentInstance.createShipment(
            "Laptop",
            "Dell XPS 13",
            "Warehouse A",
            "Retail Store B",
            50,
            10,
            { from: owner }
        );

        let nextShipmentId = await shipmentInstance.getNextShipmentId();
        assert.equal(nextShipmentId.toNumber(), 2, "Shipment ID should be 2 after creation");
    });

    it("should retrieve the shipment details", async () => {
        let shipment = await shipmentInstance.getShipment(1);
        assert(shipment, "Shipment should exist");

        // Additional checks for emitted event (Optional)
        let event = shipment.logs.find(log => log.event === "ShipmentRetrieved");
        assert(event, "ShipmentRetrieved event should have been emitted");
        assert.equal(event.args.id.toNumber(), 1, "Shipment ID should be 1");
        assert.equal(event.args.name, "Laptop", "Shipment name should match");
    });

    it("should transfer the shipment ownership", async () => {
        await shipmentInstance.shipmentTransfer(
            1,                // shipmentId
            newOwner,         // newShipmentOwner
            1,                // newState (IN_TRANSIT)
            "Shipment sent to new owner",
            { from: owner }
        );

        let transfers = await shipmentInstance.getTransferHistory(1);
        assert.equal(transfers.length, 1, "Transfer should be recorded");
        assert.equal(transfers[0].newShipmentOwner, newOwner, "New owner should be correct");
    });

    it("should fail transfer if sender is not owner", async () => {
        try {
            await shipmentInstance.shipmentTransfer(
                1,                 // shipmentId
                accounts[2],       // newShipmentOwner
                2,                 // newState (STORED)
                "Attempting unauthorized transfer",
                { from: accounts[2] }
            );
            assert.fail("Expected revert but did not get one");
        } catch (error) {
            assert(error.message.includes("Only the current owner can perform this action."), "Incorrect error message");
        }
    });
});
