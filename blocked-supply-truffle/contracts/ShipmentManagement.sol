// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ShipmentManagement {

    enum ShipmentState { CREATED, IN_TRANSIT, STORED, DELIVERED }
    enum UserRoles { ADMIN, MANAGER, OPERATOR, VIEWER }

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
        string[] roles;
        string email;
    }

    uint256 private nextShipmentId;
    uint256 private nextTransferId;

    mapping(uint256 => Shipment) private shipments;
    mapping(uint256 => Transfer[]) private transfersByShipment;
    mapping(address => User) private users;

    event ShipmentCreated(uint256 shipmentId, string productName, address indexed owner);
    event ShipmentTransfer(uint256 shipmentId, address indexed previousOwner, address indexed newShipmentOwner, ShipmentState newState);
    event UserRegistered(address indexed userAddress, string name, string[] roles);

    // Modifiers

    modifier onlyRegistered() {
        require(users[msg.sender].userAddress != address(0), "User not registered.");
        _;
    }

    modifier onlyOwner(uint256 shipmentId) {
        require(shipments[shipmentId].currentOwner == msg.sender, "Only the current owner can perform this action.");
        _;
    }

    modifier onlyAuthorized(string memory requiredRole) {
        User memory user = users[msg.sender];
        require(user.userAddress != address(0), "User not registered.");
        
        bool hasRole = false;
        for (uint256 i = 0; i < user.roles.length; i++) {
            if (keccak256(abi.encodePacked(user.roles[i])) == keccak256(abi.encodePacked(requiredRole))) {
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

        // Validate roles
        for (uint256 i = 0; i < roles.length; i++) {
            require(
                keccak256(abi.encodePacked(roles[i])) == keccak256("ADMIN") ||
                keccak256(abi.encodePacked(roles[i])) == keccak256("MANAGER") ||
                keccak256(abi.encodePacked(roles[i])) == keccak256("OPERATOR") ||
                keccak256(abi.encodePacked(roles[i])) == keccak256("VIEWER"),
                "Invalid role provided."
            );
        }

        users[msg.sender] = User({
            userAddress: msg.sender,
            username: name,
            roles: roles,
            email: email
        });

        emit UserRegistered(msg.sender, name, roles);
    }

    // Create a new shipment
    function createShipment(
        string memory productName,
        string memory description,
        string memory origin,
        string memory destination,
        uint256 units,
        uint256 weight
    ) public onlyAuthorized("MANAGER") {
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
        emit ShipmentCreated(shipmentId, productName, msg.sender);
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

        emit ShipmentTransfer(shipmentId, msg.sender, newShipmentOwner, newState);
    }

    // Get shipment details
    function getShipment(uint256 shipmentId) public onlyRegistered() view returns (
        uint256 id,
        string memory productName,
        string memory description,
        string memory origin,
        string memory destination,
        uint256 units,
        uint256 weight,
        ShipmentState currentState,
        address currentOwner,
        uint256[] memory transferHistory
    ) {
        Shipment memory shipment = shipments[shipmentId];
        return (
            shipment.id,
            shipment.name,
            shipment.description,
            shipment.origin,
            shipment.destination,
            shipment.units,
            shipment.weight,
            shipment.currentState,
            shipment.currentOwner,
            shipment.transferHistory
        );
    }

    // Get transfer history for a shipment
    function getTransferHistory(uint256 shipmentId) public onlyRegistered() view returns (Transfer[] memory) {
        return transfersByShipment[shipmentId];
    }

    // Get user details
    function getUser(address userAddress) public onlyRegistered() view returns (User memory) {
        return users[userAddress];
    }
}
