const ShipmentManagement = artifacts.require("ShipmentManagement");

contract("ShipmentManagement", accounts => {
  const owner = accounts[0];
  const user1 = accounts[1];
  const user2 = accounts[2];
  let contract;

  beforeEach(async () => {
    contract = await ShipmentManagement.new();
  });

  it("should create a shipment successfully", async () => {
    const result = await contract.createShipment(
      "Product A",
      "Description A",
      "Origin A",
      "Destination A",
      "2025-12-01",
      10,
      100,
      { from: owner }
    );

    const shipmentId = 1;
    const shipment = await contract.getShipment(shipmentId);

    assert.strictEqual(shipment[0].toNumber(), shipmentId, "Shipment ID should match");
    assert.strictEqual(shipment[1], "Product A");
    assert.strictEqual(shipment[2], "Description A");
    assert.strictEqual(shipment[3], "Origin A");
    assert.strictEqual(shipment[4], "Destination A");
    assert.strictEqual(shipment[5], "2025-12-01");
    assert.strictEqual(shipment[6].toNumber(), 10);
    assert.strictEqual(shipment[7].toNumber(), 100);
    assert.strictEqual(shipment[8].toNumber(), 0); // CREATED
    assert.strictEqual(shipment[9], owner);
  });

  it("should not create a shipment with 0 units or weight", async () => {
    try {
      await contract.createShipment(
        "Bad Product",
        "No units",
        "Origin",
        "Dest",
        "2025-12-01",
        0,
        100,
        { from: owner }
      );
      assert.fail("Expected revert not received for 0 units");
    } catch (error) {
      assert(error.message.includes("Units must be greater than 0."), error.message);
    }

    try {
      await contract.createShipment(
        "Bad Product",
        "No weight",
        "Origin",
        "Dest",
        "2025-12-01",
        10,
        0,
        { from: owner }
      );
      assert.fail("Expected revert not received for 0 weight");
    } catch (error) {
      assert(error.message.includes("Weight must be greater than 0."), error.message);
    }
  });

  it("should allow shipment transfer by owner", async () => {
    await contract.createShipment(
      "Product B",
      "Desc B",
      "NY",
      "LA",
      "2025-11-11",
      5,
      50,
      { from: owner }
    );

    await contract.shipmentTransfer(
      1,
      user1,
      1, // IN_TRANSIT
      "Checkpoint 1",
      "Left origin",
      { from: owner }
    );

    const shipment = await contract.getShipment(1);
    assert.strictEqual(shipment[8].toNumber(), 1); // IN_TRANSIT
    assert.strictEqual(shipment[9], user1);

    const transfers = await contract.getTransfers(1);
    assert.strictEqual(transfers.length, 1);
    assert.strictEqual(transfers[0].location, "Checkpoint 1");
    assert.strictEqual(transfers[0].transferNotes, "Left origin");
  });

  it("should reject shipment transfer by non-owner", async () => {
    await contract.createShipment(
      "Product C",
      "Desc C",
      "A",
      "B",
      "2025-11-01",
      3,
      30,
      { from: owner }
    );

    try {
      await contract.shipmentTransfer(
        1,
        user2,
        1,
        "Somewhere",
        "Attempted transfer",
        { from: user1 }
      );
      assert.fail("Expected revert for non-owner transfer");
    } catch (error) {
      assert(error.message.includes("Only the current owner can perform this action."), error.message);
    }
  });

  it("should record multiple transfers", async () => {
    await contract.createShipment(
      "Product D",
      "Desc D",
      "Origin X",
      "Dest Y",
      "2025-10-10",
      2,
      20,
      { from: owner }
    );

    await contract.shipmentTransfer(1, user1, 1, "Point A", "First leg", { from: owner });
    await contract.shipmentTransfer(1, owner, 2, "Point B", "Returned", { from: user1 });

    const transfers = await contract.getTransfers(1);
    assert.strictEqual(transfers.length, 2);
    assert.strictEqual(transfers[1].newShipmentOwner, owner);
    assert.strictEqual(transfers[1].transferNotes, "Returned");
  });

  it("should reject calls for non-existent shipment", async () => {
    try {
      await contract.getShipment(999);
      assert.fail("Expected revert for non-existent shipment");
    } catch (error) {
      assert(error.message.includes("Shipment does not exist."), error.message);
    }

    try {
      await contract.getTransfers(999);
      assert.fail("Expected revert for non-existent shipment");
    } catch (error) {
      assert(error.message.includes("Shipment does not exist."), error.message);
    }
  });

  it("should return next shipment and transfer IDs", async () => {
    const nextShipmentBefore = await contract.getNextShipmentId();
    const nextTransferBefore = await contract.getNextTransferId();

    assert.strictEqual(nextShipmentBefore.toNumber(), 1);
    assert.strictEqual(nextTransferBefore.toNumber(), 1);

    await contract.createShipment(
      "Product E",
      "Desc E",
      "One",
      "Two",
      "2025-07-07",
      7,
      70,
      { from: owner }
    );

    const nextShipmentAfter = await contract.getNextShipmentId();
    assert.strictEqual(nextShipmentAfter.toNumber(), 2);
  });
});
