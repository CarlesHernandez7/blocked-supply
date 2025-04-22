const Web3 = require('web3');
const contractJson = require('../abi/ShipmentManagement.json');
require('dotenv').config();

const web3 = new Web3.default("http://0.0.0.0:8545");
const contractAddress = "0xe78a0f7e598cc8b0bb87894b0f60dd2a88d6a8ab";
const contract = new web3.eth.Contract(contractJson.abi, contractAddress);

module.exports = { web3, contract };
