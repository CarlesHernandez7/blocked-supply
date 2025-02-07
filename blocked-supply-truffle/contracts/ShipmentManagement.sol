// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ShipmentManagement {
    enum State { Created, InTransit, Stored, Delivered }

    struct Shipment {
        uint256 id;
        string name;
        string description;
        string origin;
        string destination;
        uint256 units;
        uint256 weight;
        State currentState;
        address currentOwner;
    }

    struct Transfer {
        uint256 id;
        uint256 shipmentId;
        uint256 timestamp;
        address newShipmentOwner;
        string transferNotes;
    }

    uint256 private nextShipmentId = 1;
    uint256 private nextTransferId = 1;

    mapping(uint256 => Shipment) private shipments;
    mapping(uint256 => Transfer[]) private transfersByShipment;

    event ShipmentCreated(uint256 shipmentId, string name, address owner);
    event ShipmentTransfer(uint256 shipmentId, uint256 timestamp, address indexed previousOwner, address indexed newShipmentOwner, State newState, string transferNotes);

    // Modifiers

    modifier onlyOwner(uint256 shipmentId) {
        require(shipments[shipmentId].currentOwner == msg.sender, "Only the current owner can perform this action.");
        _;
    }
    
    modifier validStateChange(State oldState, State newState) {
        require(
            (oldState == State.Created && newState == State.InTransit) ||
            (oldState == State.InTransit && newState == State.Stored) ||
            (oldState == State.Stored && newState == State.Delivered),
            "Invalid state transition"
        );
        _;
    }
    
    // Functions

    function createShipment(
        string memory productName,
        string memory description,
        string memory origin,
        string memory destination,
        uint256 units,
        uint256 weight
    ) public {
        require(units > 0, "Units must be greater than 0.");
        require(weight > 0, "Weight must be greater than 0.");
        
        uint256 shipmentId = nextShipmentId++;
        shipments[shipmentId] = Shipment({
            id: shipmentId,
            name: productName,
            description: description,
            origin: origin,
            destination: destination,
            units: units,
            weight: weight,
            currentState: State.Created,
            currentOwner: msg.sender
        });

        //emit ShipmentCreated(shipmentId, productName, msg.sender);
    }

    function shipmentTransfer(
        uint256 shipmentId,
        address newShipmentOwner,
        State newState,
        string memory transferNotes
    ) public onlyOwner(shipmentId) validStateChange(shipments[shipmentId].currentState, newState) {
        Shipment storage shipment = shipments[shipmentId];

        address previousOwner = shipment.currentOwner;
        shipment.currentOwner = newShipmentOwner;
        shipment.currentState = newState;

        uint256 transferId = nextTransferId++;
        transfersByShipment[shipmentId].push(Transfer({
            id: transferId,
            shipmentId: shipmentId,
            timestamp: block.timestamp,
            newShipmentOwner: newShipmentOwner,
            transferNotes: transferNotes
        }));

        emit ShipmentTransfer(shipmentId, block.timestamp, previousOwner, newShipmentOwner, newState, transferNotes);
    }

    function getShipment(uint256 shipmentId) public view returns (Shipment memory) {
        return shipments[shipmentId];
    }

    function getTransferHistory(uint256 shipmentId) public view returns (Transfer[] memory) {
        return transfersByShipment[shipmentId];
    }
}
