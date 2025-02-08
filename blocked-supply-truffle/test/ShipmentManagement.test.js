const ShipmentManagement = artifacts.require("ShipmentManagement");
const { expectRevert, time } = require("@openzeppelin/test-helpers");
const { assert } = require("chai");

contract("ShipmentManagement", (accounts) => {
  let shipmentContract;
  const [owner, user1, user2] = accounts;

  before(async () => {
    shipmentContract = await ShipmentManagement.new();
  });

  it("should create a shipment", async () => {
    const tx = await shipmentContract.createShipment(
      "Laptop",
      "High-end gaming laptop",
      "New York",
      "Los Angeles",
      10,
      50,
      { from: owner }
    );

    const shipment = await shipmentContract.getShipment(1);
    assert.equal(shipment.name, "Laptop", "Shipment name should match");
    assert.equal(shipment.units, 10, "Units should match");
    assert.equal(shipment.currentOwner, owner, "Owner should be correct");
  });

  it("should transfer a shipment and update state", async () => {
    await shipmentContract.shipmentTransfer(1, user1, 1, "Shipped to warehouse", { from: owner });
    const shipment = await shipmentContract.getShipment(1);
    assert.equal(shipment.currentOwner, user1, "New owner should be user1");
    assert.equal(shipment.currentState, 1, "State should be InTransit");
  });

  it("should prevent unauthorized transfers", async () => {
    await expectRevert(
      shipmentContract.shipmentTransfer(1, user2, 2, "Stored at facility", { from: user2 }),
      "Only the current owner can perform this action."
    );
  });

  it("should correctly track transfer history", async () => {
    await shipmentContract.shipmentTransfer(1, user2, 2, "Stored at facility", { from: user1 });
    const transfers = await shipmentContract.getTransferHistory(1);
    assert.equal(transfers.length, 2, "There should be two transfers");
  });
});
