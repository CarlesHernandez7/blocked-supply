// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ShipmentManagement {

    enum ShipmentState { CREATED, IN_TRANSIT, STORED, DELIVERED }
    enum UserRoles { ADMIN, CUSTOMER, TRANSPORTER, WAREHOUSE }

    struct Shipment {
        uint256 id;
        string name;
        string description;
        string origin;
        string destination;
        uint256 units;
        uint256 weight;
        ShipmentState currentState;
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
        UserRoles[] roles;
        string email;
    }

    uint256 private nextShipmentId;
    uint256 private nextTransferId;

    mapping(uint256 => Shipment) private shipments;
    mapping(uint256 => Transfer[]) private transfersByShipment;
    mapping(address => User) private users;

    event ShipmentCreated(uint256 shipmentId, string productName, string description, string origin, string destination, uint256 units, uint256 weight, address indexed owner);
    event ShipmentTransfer(uint256 shipmentId, uint256 timestamp, address indexed previousOwner, address indexed newShipmentOwner, ShipmentState newState, string transferNotes);
    event UserRegistered(address indexed userAddress, string name, string email, UserRoles[] roles);

    // Modifiers

    modifier onlyOwner(uint256 shipmentId) {
        require(shipments[shipmentId].currentOwner == msg.sender, "Only the current owner can perform this action.");
        _;
    }

    modifier onlyAuthorized(UserRoles requiredRole) {
        User storage user = users[msg.sender];
        require(user.userAddress != address(0), "User not registered.");

        bool hasRole = false;
        for (uint256 i = 0; i < user.roles.length; i++) {
            if (user.roles[i] == requiredRole || user.roles[i] == UserRoles.ADMIN) {
                hasRole = true;
                break;
            } 
        }
        
        require(hasRole, "Not authorized for this role.");
        _;
    }


    // Functions

    function registerUser(string memory name, UserRoles[] memory roles, string memory email) public {
        require(users[msg.sender].userAddress == address(0), "User already registered.");
        require(roles.length > 0, "User must have at least one role.");

        for (uint256 i = 0; i < roles.length; i++) {
            require(
                roles[i] == UserRoles.ADMIN ||
                roles[i] == UserRoles.CUSTOMER ||
                roles[i] == UserRoles.TRANSPORTER ||
                roles[i] == UserRoles.WAREHOUSE,
                "Invalid role provided."
            );
        }

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
    ) public onlyAuthorized(UserRoles.ADMIN) {

        uint256 shipmentId = nextShipmentId++;
        shipments[shipmentId] = Shipment({
            id: shipmentId,
            name: productName,
            description: description,
            origin: origin,
            destination: destination,
            units: units,
            weight: weight,
            currentState: ShipmentState.CREATED,
            currentOwner: msg.sender,
            transferHistory: new uint256[](0) 
        });
    
        emit ShipmentCreated(shipmentId, productName, description, origin, destination, units, weight, msg.sender);
    }


    // Transfer ownership of a shipment
    function shipmentTransfer(uint256 shipmentId, address newShipmentOwner, ShipmentState newState, string memory transferNotes) public onlyOwner(shipmentId) {
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
    function getShipment(uint256 shipmentId) public view onlyAuthorized(UserRoles.ADMIN) returns (Shipment memory) {
        return shipments[shipmentId];
    }

    // Get transfer history for a shipment
    function getTransferHistory(uint256 shipmentId) public onlyAuthorized(UserRoles.CUSTOMER) view returns (Transfer[] memory) {
        return transfersByShipment[shipmentId];
    }

    // Get user details
    function getUser(address userAddress) public onlyAuthorized(UserRoles.ADMIN) view returns (User memory) {
        return users[userAddress];
    }
}
