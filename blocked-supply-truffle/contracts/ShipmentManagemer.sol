// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ShipmentManager {

    struct Shipment {
        uint256 id;
        string productName;
        string description;
        string origin;
        string destination;
        uint256 units;
        uint256 weight;
        string currentState;
        address currentOwner;
        uint256[] history;
    }

    struct Transfer {
        uint256 id;
        uint256 shipmentId;
        uint256 timestamp;
        address newOwner;
    }

    struct User {
        address userAddress;
        string name;
        string role;
        string email;
    }

    uint256 private nextShipmentId;
    uint256 private nextTransferId;

    mapping(uint256 => Shipment) private shipments; // shipmentId => Shipment
    mapping(uint256 => Transfer[]) private transfersByShipment; // shipmentId => array of Transfers
    mapping(address => User) private users; // userAddress => User

    // Events
    event ShipmentCreated(uint256 shipmentId, address indexed owner);
    event ShipmentUpdated(uint256 shipmentId, string newState, address indexed updatedBy);
    event OwnershipTransferred(uint256 shipmentId, address indexed newOwner, address indexed previousOwner);
    event UserRegistered(address indexed userAddress, string name, string role);

    // Modifiers
    modifier onlyOwner(uint256 shipmentId) {
        require(shipments[shipmentId].currentOwner == msg.sender, "Only the current owner can perform this action.");
        _;
    }

    modifier onlyAuthorized(string memory requiredRole) {
        require(keccak256(abi.encodePacked(users[msg.sender].role)) == keccak256(abi.encodePacked(requiredRole)), "Not authorized.");
        _;
    }

    // Functions

    // Register a new user
    function registerUser(string memory name, string memory role, string memory email) public {
        require(users[msg.sender].userAddress == address(0), "User already registered.");
        users[msg.sender] = User({userAddress: msg.sender, name: name, role: role, email: email});
        emit UserRegistered(msg.sender, name, role);
    }

    // Create a new shipment
    function createShipment(
        string memory productName,
        string memory description,
        string memory origin,
        string memory destination,
        uint256 units,
        uint256 weight
    ) public onlyAuthorized("manager") {
        uint256 shipmentId = nextShipmentId++;
        shipments[shipmentId] = Shipment({
            id: shipmentId,
            productName: productName,
            description: description,
            origin: origin,
            destination: destination,
            units: units,
            weight: weight,
            currentState: "Created",
            currentOwner: msg.sender,
            history: new uint256[](0)
        });
        emit ShipmentCreated(shipmentId, msg.sender);
    }

    // Update the shipment state
    function updateShipment(uint256 shipmentId, string memory newState) public onlyOwner(shipmentId) {
        Shipment storage shipment = shipments[shipmentId];
        shipment.currentState = newState;
        shipment.history.push(block.timestamp);
        emit ShipmentUpdated(shipmentId, newState, msg.sender);
    }

    // Transfer ownership of a shipment
    function transferOwnership(uint256 shipmentId, address newOwner) public onlyOwner(shipmentId) {
        Shipment storage shipment = shipments[shipmentId];
        shipment.currentOwner = newOwner;

        // Record the transfer
        transfersByShipment[shipmentId].push(Transfer({
            id: nextTransferId++,
            shipmentId: shipmentId,
            timestamp: block.timestamp,
            newOwner: newOwner
        }));

        emit OwnershipTransferred(shipmentId, newOwner, msg.sender);
    }

    // Get shipment details
    function getShipment(uint256 shipmentId) public view returns (Shipment memory) {
        return shipments[shipmentId];
    }

    // Get transfer history for a shipment
    function getTransferHistory(uint256 shipmentId) public view returns (Transfer[] memory) {
        return transfersByShipment[shipmentId];
    }

    // Get user details
    function getUser(address userAddress) public view returns (User memory) {
        return users[userAddress];
    }
}
