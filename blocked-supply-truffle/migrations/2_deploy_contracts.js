const ShipmentManagement = artifacts.require("ShipmentManagement");

module.exports = function (deployer) {
    deployer.deploy(ShipmentManagement);
};
