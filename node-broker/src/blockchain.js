const Web3 = require('web3');
const contractJson = require('../abi/ShipmentManagement.json');
require('dotenv').config();

const web3 = new Web3.default(process.env.BLOCKCHAIN_NODE_URL);
const contractAddress = process.env.CONTRACT_ADDRESS;

const contract = new web3.eth.Contract(contractJson.abi, contractAddress);

module.exports = { web3, contract };
