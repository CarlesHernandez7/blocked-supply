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

    event ShipmentRetrieved(
        uint256 indexed id,
        string name,
        string description,
        string origin,
        string destination,
        uint256 units,
        uint256 weight,
        uint256 currentState,
        address currentOwner
    );

    event TransferRetrieved(
        uint256 indexed id,
        uint256 shipmentId,
        uint256 timestamp,
        uint256 newState,
        string location,
        address newShipmentOwner,
        string transferNotes
    );

    uint256 private nextShipmentId = 1;
    uint256 private nextTransferId = 1;

    mapping(uint256 => Shipment) private shipments;
    mapping(uint256 => Transfer[]) private transfersByShipment;
    mapping(address => uint256[]) shipmentsByOwner;

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
            currentState: State.CREATED,
            currentOwner: msg.sender
        });

        shipmentsByOwner[msg.sender].push(shipmentId);
    }

    function shipmentTransfer(
        uint256 shipmentId,
        address newShipmentOwner,
        State newState,
        string memory location,
        string memory transferNotes
    ) public onlyOwner(shipmentId) {
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
    }

    function getShipment(uint256 shipmentId) public exists(shipmentId){    
        
        Shipment storage shipment = shipments[shipmentId];

        emit ShipmentRetrieved(
            shipment.id,
            shipment.name,
            shipment.description,
            shipment.origin,
            shipment.destination,
            shipment.units,
            shipment.weight,
            uint256(shipment.currentState),
            shipment.currentOwner
        );
    }

    function getTransferByIndex(uint256 shipmentId, uint256 index) public {
        Transfer storage transfer = transfersByShipment[shipmentId][index];

        emit TransferRetrieved(
            transfer.id,
            transfer.shipmentId,
            transfer.timestamp,
            uint256(transfer.newState),
            transfer.location,
            transfer.newShipmentOwner,
            transfer.transferNotes
        );
    }

    function getTransferCount(uint256 shipmentId) public exists(shipmentId) view returns (uint256) {
        return transfersByShipment[shipmentId].length;
    }

    function getShipmentsByOwner(address owner) public view returns (uint256[] memory) {
        return shipmentsByOwner[owner];
    }

    function getNextShipmentId() public view returns (uint256) {
        return nextShipmentId;
    }

    function getNextTransferId() public view returns (uint256) {
        return nextTransferId;
    }
}
