const Web3 = require('web3');
const contractJson = require('../abi/ShipmentManagement.json');
require('dotenv').config();

const web3 = new Web3.default("http://0.0.0.0:8545");
const contractAddress = "0xe78A0F7E598Cc8b0Bb87894B0F60dD2a88d6a8Ab";
const contract = new web3.eth.Contract(contractJson.abi, contractAddress);

module.exports = { web3, contract };
