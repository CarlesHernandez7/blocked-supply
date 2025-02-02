// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ShipmentManagement {

    string[] shipmentStates = ["CREATED", "IN_TRANSIT", "STORED", "DELIVERED"];
    string[] userRoles = ["ADMIN", "CUSTOMER", "TRANSPORTER", "WAREHOUSE"];

    struct Shipment {
        uint256 id;
        string name;
        string description;
        string origin;
        string destination;
        uint256 units;
        uint256 weight;
        string currentState;
        address currentOwner;
        uint256[] transferHistory;
    }

    struct Transfer {
        uint256 id;
        uint256 shipmentId;
        uint256 timestamp;
        address newShipmentOwner;
        string transferNotes;
    }

    struct User {
        address userAddress;
        string username;
        string[] roles;
        string email;
    }

    uint256 private nextShipmentId;
    uint256 private nextTransferId;

    mapping(uint256 => Shipment) private shipments;
    mapping(uint256 => Transfer[]) private transfersByShipment;
    mapping(address => User) private users;

    event ShipmentCreated(uint256 shipmentId, string productName, string description, string origin, string destination, uint256 units, uint256 weight, address indexed owner);
    event ShipmentTransfer(uint256 shipmentId, uint256 timestamp, address indexed previousOwner, address indexed newShipmentOwner, string newState, string transferNotes);
    event UserRegistered(address indexed userAddress, string name, string email, string[] roles);

    // Modifiers

    modifier onlyOwner(uint256 shipmentId) {
        require(shipments[shipmentId].currentOwner == msg.sender, "Only the current owner can perform this action.");
        _;
    }

    modifier onlyAuthorized(string memory requiredRole) {
        User storage user = users[msg.sender];
        require(user.userAddress != address(0), "User not registered.");

        bool hasRole = false;
        for (uint256 i = 0; i < user.roles.length; i++) {
            if (keccak256(abi.encodePacked(user.roles[i])) == keccak256(abi.encodePacked(requiredRole)) || keccak256(abi.encodePacked(user.roles[i])) == keccak256(abi.encodePacked("ADMIN"))) {
                hasRole = true;
                break;
            } 
        }
        
        require(hasRole, "Not authorized for this role.");
        _;
    }
    
    // Functions

    function registerUser(string memory name, string[] memory roles, string memory email) public {
        require(users[msg.sender].userAddress == address(0), "User already registered.");
        require(roles.length > 0, "User must have at least one role.");

        bool validRole = false;
        for (uint256 i = 0; i < roles.length; i++) {
            for (uint256 j = 0; j < userRoles.length; j++) {
                if (keccak256(abi.encodePacked(roles[i])) == keccak256(abi.encodePacked(userRoles[j]))) {
                    validRole = true;
                    break;
                }
            }
        }
        require(validRole, "Invalid role provided.");

        users[msg.sender] = User({
            userAddress: msg.sender,
            username: name,
            roles: roles,
            email: email
        });

        emit UserRegistered(msg.sender, name, email, roles);
    }

    // Create a new shipment
    function createShipment(
        string memory productName,
        string memory description,
        string memory origin,
        string memory destination,
        uint256 units,
        uint256 weight
    ) public onlyAuthorized("ADMIN") {
        uint256 shipmentId = nextShipmentId++;
        shipments[shipmentId] = Shipment({
            id: shipmentId,
            name: productName,
            description: description,
            origin: origin,
            destination: destination,
            units: units,
            weight: weight,
            currentState: "CREATED",
            currentOwner: msg.sender,
            transferHistory: new uint256[](0) 
        });
    
        emit ShipmentCreated(shipmentId, productName, description, origin, destination, units, weight, msg.sender);
    }

    // Transfer ownership of a shipment
    function shipmentTransfer(uint256 shipmentId, address newShipmentOwner, string memory newState, string memory transferNotes) public onlyOwner(shipmentId) {
        bool validState = false;
        for (uint256 i = 0; i < shipmentStates.length; i++) {
            if (keccak256(abi.encodePacked(newState)) == keccak256(abi.encodePacked(shipmentStates[i]))) {
                validState = true;
                break;
            }
        }
        require(validState, "Invalid shipment state.");
        
        Shipment storage shipment = shipments[shipmentId];
        
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
        shipment.transferHistory.push(transferId);

        emit ShipmentTransfer(shipmentId, block.timestamp, msg.sender, newShipmentOwner, newState, transferNotes);
    }

    // Get shipment details
    function getShipment(uint256 shipmentId) public view onlyAuthorized("ADMIN") returns (Shipment memory) {
        return shipments[shipmentId];
    }

    // Get transfer history for a shipment
    function getTransferHistory(uint256 shipmentId) public onlyAuthorized("CUSTOMER") view returns (Transfer[] memory) {
        return transfersByShipment[shipmentId];
    }

    // Get user details
    function getUser(address userAddress) public onlyAuthorized("ADMIN") view returns (User memory) {
        return users[userAddress];
    }
}