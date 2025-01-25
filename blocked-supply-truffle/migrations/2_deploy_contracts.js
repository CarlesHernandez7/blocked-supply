const ShipmentManager = artifacts.require("ShipmentManager");

module.exports = function (deployer) {
    deployer.deploy(ShipmentManager);
};
