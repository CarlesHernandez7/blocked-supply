// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ShipmentManagement {
    enum State { CREATED, IN_TRANSIT, STORED, DELIVERED }

    struct Shipment {
        uint256 id;
        string name;
        string description;
        string origin;
        string destination;
        string deliveryDate;
        uint256 units;
        uint256 weight;
        State currentState;
        address currentOwner;
    }

    struct Transfer {
        uint256 id;
        uint256 shipmentId;
        uint256 timestamp;
        State newState;
        string location;
        address newShipmentOwner;
        string transferNotes;
    }

    uint256 private nextShipmentId = 1;
    uint256 private nextTransferId = 1;

    mapping(uint256 => Shipment) private shipments;
    mapping(uint256 => Transfer[]) private transfersByShipment;

    modifier onlyOwner(uint256 shipmentId) {
        require(shipments[shipmentId].currentOwner == msg.sender, "Only the current owner can perform this action.");
        _;
    }

    modifier exists(uint256 shipmentId) {
        require(shipmentId > 0, "Shipment ID must be greater than 0.");
        require(shipmentId < nextShipmentId, "Shipment does not exist.");
        _;
    }

    function createShipment(
        string memory productName,
        string memory description,
        string memory origin,
        string memory destination,
        string memory deliveryDate,
        uint256 units,
        uint256 weight
    ) public returns (uint256, address, string memory) {
        require(units > 0, "Units must be greater than 0.");
        require(weight > 0, "Weight must be greater than 0.");
        
        uint256 newShipmentId = nextShipmentId++;
        shipments[newShipmentId] = Shipment({
            id: newShipmentId,
            name: productName,
            description: description,
            origin: origin,
            destination: destination,
            deliveryDate: deliveryDate,
            units: units,
            weight: weight,
            currentState: State.CREATED,
            currentOwner: msg.sender
        });

        return (newShipmentId, msg.sender, deliveryDate);
    }

    function shipmentTransfer(
        uint256 shipmentId,
        address newShipmentOwner,
        State newState,
        string memory location,
        string memory transferNotes
    ) public onlyOwner(shipmentId) returns (uint256, State) {
        Shipment storage shipment = shipments[shipmentId];
        
        shipment.currentOwner = newShipmentOwner;
        shipment.currentState = newState;

        uint256 transferId = nextTransferId++;
        transfersByShipment[shipmentId].push(Transfer({
            id: transferId,
            shipmentId: shipmentId,
            timestamp: block.timestamp,
            newState: newState,
            location: location,
            newShipmentOwner: newShipmentOwner,
            transferNotes: transferNotes
        }));

        return (shipmentId, newState);
    }

    function getShipment(uint256 shipmentId) public view exists(shipmentId) 
        returns (
            uint256 id, 
            string memory name, 
            string memory description, 
            string memory origin, 
            string memory destination, 
            string memory deliveryDate, 
            uint256 units, 
            uint256 weight, 
            uint256 currentState, 
            address currentOwner
        ) 
    {    
        Shipment storage shipment = shipments[shipmentId];

        return (
            shipment.id,
            shipment.name,
            shipment.description,
            shipment.origin,
            shipment.destination,
            shipment.deliveryDate,
            shipment.units,
            shipment.weight,
            uint256(shipment.currentState),
            shipment.currentOwner
        );
    }

    function getTransfers(uint256 shipmentId) public view exists(shipmentId) returns (Transfer[] memory) {
        return transfersByShipment[shipmentId];
    }

    function getNextShipmentId() public view returns (uint256) {
        return nextShipmentId;
    }

    function getNextTransferId() public view returns (uint256) {
        return nextTransferId;
    }
}
