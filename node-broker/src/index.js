const fetch = require('node-fetch');

const PORT = process.env.PORT || 3001;
const GANACHE_URL = process.env.GANACHE_URL || "http://host.docker.internal:8545";

const express = require('express');
const app = express();

app.get('/', async (req, res) => {
    console.log(GANACHE_URL);
  try {
    const response = await fetch(GANACHE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        jsonrpc: '2.0',
        method: 'eth_blockNumber',
        params: [],
        id: 1
      }),
    });

    const data = await response.json();
    res.send(`Latest block: ${parseInt(data.result, 16)}`);
  } catch (err) {
    console.error('Error contacting Ganache:', err.message);
    res.status(500).send('Error contacting Ganache');
  }
});

app.listen(PORT, () => {
  console.log(`Broker running on http://localhost:${PORT}`);
});
