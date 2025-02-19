const ShipmentManagement = artifacts.require("ShipmentManagement");

contract("ShipmentManagement", (accounts) => {
    let contractInstance;

    before(async () => {
        contractInstance = await ShipmentManagement.new();
    });

    it("should create a shipment", async () => {
        await contractInstance.createShipment(
            "Laptop",
            "A high-end gaming laptop",
            "New York",
            "San Francisco",
            10,
            2000,
            { from: accounts[0] }
        );

        const nextShipmentId = await contractInstance.getNextShipmentId();
        assert.equal(nextShipmentId.toNumber(), 2, "Shipment ID should be 2 after first creation");
    });

    it("should transfer a shipment", async () => {
        const shipmentId = 1;
        const newOwner = accounts[1];
        const newState = 1; // IN_TRANSIT
        const transferNotes = "Shipment is now in transit.";

        await contractInstance.shipmentTransfer(
            shipmentId,
            newOwner,
            newState,
            transferNotes,
            { from: accounts[0] }
        );

        const transferCount = await contractInstance.getTransferCount(shipmentId);
        assert.equal(transferCount.toNumber(), 1, "There should be one transfer record");
    });

    it("should not allow non-owner to transfer a shipment", async () => {
        const shipmentId = 1;
        const newOwner = accounts[2];
        const newState = 2; // STORED
        const transferNotes = "Unauthorized transfer attempt.";

        try {
            await contractInstance.shipmentTransfer(
                shipmentId,
                newOwner,
                newState,
                transferNotes,
                { from: accounts[2] } // Not the owner
            );
            assert.fail("Only the current owner can perform this action.");
        } catch (error) {
            assert.include(error.message, "Only the current owner can perform this action.", "Error message should indicate ownership restriction.");
        }

        const transferCount = await contractInstance.getTransferCount(shipmentId);
        assert.equal(transferCount.toNumber(), 1, "There should be one transfer record");
    });

    it("should retrieve shipment details", async () => {
        const shipmentId = 1;
        const result = await contractInstance.getShipment(shipmentId);
        assert.isDefined(result, "Shipment retrieval should emit an event");
    });

    it("should retrieve transfer details by index", async () => {
        const shipmentId = 1;
        const transferIndex = 0;
        const result = await contractInstance.getTransferByIndex(shipmentId, transferIndex);
        assert.isDefined(result, "Transfer retrieval should emit an event");
    });
});