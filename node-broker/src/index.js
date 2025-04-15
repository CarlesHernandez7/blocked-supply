const express = require('express');
require('dotenv').config();

const app = express();
app.use(express.json());

const routes = require('./routes');
app.use('/api', routes);

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
    console.log(`node-broker running on http://localhost:${PORT}`);
});
