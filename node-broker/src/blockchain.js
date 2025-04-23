const Web3 = require('web3');
const contractJson = require('../abi/ShipmentManagement.json');
require('dotenv').config();

const web3 = new Web3.default("http://0.0.0.0:8545");
const contractAddress = "0x5b1869D9A4C187F2EAa108f3062412ecf0526b24";
const contract = new web3.eth.Contract(contractJson.abi, contractAddress);

module.exports = { web3, contract };
