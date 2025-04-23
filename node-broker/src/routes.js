const express = require('express');
const router = express.Router();
const { web3, contract } = require('./blockchain');

//FETCH NEXT IDS
router.get('/shipments/next-id', async (req, res) => {
    try {
        const nextShipmentId = await contract.methods.getNextShipmentId().call();
        res.json({ nextShipmentId: nextShipmentId.toString() });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});
router.get('/transfers/next-id', async (req, res) => {
    try {
        const nextTransferId = await contract.methods.getNextTransferId().call();
        res.json({ nextTransferId: nextTransferId.toString() });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// CREATE SHIPMENT
router.post('/shipments', async (req, res) => {
    const {
        productName,
        description,
        origin,
        destination,
        deliveryDate,
        units,
        weight,
        from
    } = req.body;

    if (from == null) {
        return res.status(400).json({ error: "'from' address is required" });
    }

    if (productName == null || description == null || origin == null || destination == null || deliveryDate == null || units == null || weight == null) {
        return res.status(400).json({ error: 'Missing required fields' });
    }

    if (isNaN(units) || isNaN(weight)) {
        return res.status(400).json({ error: 'Units and weight must be numbers' });
    }

    try {
        await contract.methods.createShipment(
            productName,
            description,
            origin,
            destination,
            deliveryDate,
            units,
            weight
        ).send({ from, gas: 3000000 });

        const nextId = await contract.methods.getNextShipmentId().call();
        const createdId = Number(nextId) - 1;

        const shipment = await contract.methods.getShipment(createdId).call();

        res.json({
            id: shipment[0].toString(),
            currentOwner: shipment[9],
            deliveryDate: shipment[5]
        });

    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// GET SHIPMENT
router.get('/shipments/:shipmentId', async (req, res) => {
    const { shipmentId } = req.params;

    try {
        const shipment = await contract.methods.getShipment(shipmentId).call();

        res.json({
            id: shipment[0].toString(),
            name: shipment[1],
            description: shipment[2],
            origin: shipment[3],
            destination: shipment[4],
            deliveryDate: shipment[5],
            units: shipment[6].toString(),
            weight: shipment[7].toString(),
            currentState: shipment[8].toString(),
            currentOwner: shipment[9],
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// TRANSFER SHIPMENT
router.post('/shipments/:shipmentId/transfer', async (req, res) => {
    const { shipmentId } = req.params;
    const { 
        newShipmentOwner, 
        newState, 
        location, 
        transferNotes,
        from 
    } = req.body;

    if (from == null) {
        return res.status(400).json({ error: "'from' address is required" });
    }

    if (shipmentId == null || newShipmentOwner == null || newState == null || location == null || transferNotes == null) {
        return res.status(400).json({ error: 'Missing required fields' });
    }

    try {
        await contract.methods.shipmentTransfer(
            shipmentId,
            newShipmentOwner,
            newState,
            location,
            transferNotes
        ).send({
            from,
            gas: 3000000,
        });

        const shipment = await contract.methods.getShipment(shipmentId).call();

        res.json({
            shipmentId: shipment[0].toString(),
            newOwner: shipment[9],
            newState: shipment[8].toString(),
        });

    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// GET SHIPMENT TRANSFERS
router.get('/shipments/:shipmentId/transfers', async (req, res) => {
    const { shipmentId } = req.params;

    try {
        const transfers = await contract.methods.getTransfers(shipmentId).call();

        const parsedTransfers = transfers.map(t => ({
            id: t.id.toString(),
            shipmentId: t.shipmentId.toString(),
            timestamp: t.timestamp.toString(),
            newState: t.newState.toString(),
            location: t.location,
            newShipmentOwner: t.newShipmentOwner,
            transferNotes: t.transferNotes
        }));

        res.json(parsedTransfers);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// GET ALL GANACHE ACCOUNTS
router.get('/accounts', async (req, res) => {
    try {
        const accounts = await web3.eth.getAccounts();
        res.json({ accounts });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

module.exports = router;
