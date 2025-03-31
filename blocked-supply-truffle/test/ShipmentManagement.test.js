const ShipmentManagement = artifacts.require("ShipmentManagement");

contract("ShipmentManagement", (accounts) => {
    const owner = accounts[0];
    const newOwner = accounts[1];

    let shipmentContract;
    let shipmentId;

    before(async () => {
        shipmentContract = await ShipmentManagement.new();
    });

    it("should create a new shipment", async () => {
        const receipt = await shipmentContract.createShipment(
            "Electronics",
            "Fragile Items",
            "New York",
            "San Francisco",
            10,
            100,
            { from: owner }
        );

        assert.isDefined(receipt.logs[0], "ShipmentCreated event should be emitted");
        shipmentId = (await shipmentContract.getNextShipmentId()).toNumber() - 1;
    });

    it("should retrieve shipment details", async () => {
        const shipment = await shipmentContract.getShipment(shipmentId);
        assert.isDefined(shipment, "Shipment should exist");
    });

    it("should transfer shipment to a new owner", async () => {
        const receipt = await shipmentContract.shipmentTransfer(
            shipmentId,
            newOwner,
            1, // IN_TRANSIT
            "Chicago",
            "Shipment left New York",
            { from: owner }
        );

        assert.isDefined(receipt.logs[0], "TransferCreated event should be emitted");
    });

    it("should fail transfer if not owner", async () => {
        try {
            await shipmentContract.shipmentTransfer(
                shipmentId,
                owner,
                2, // STORED
                "Los Angeles",
                "Arrived at warehouse",
                { from: accounts[2] }
            );
            assert.fail("Expected revert, but transaction succeeded");
        } catch (error) {
            assert.include(error.message, "Only the current owner can perform this action.");
        }
    });

    it("should retrieve transfer details", async () => {
        const transferCount = await shipmentContract.getTransferCount(shipmentId);
        assert.equal(transferCount.toNumber(), 1, "Transfer count should be 1");
    });
});
