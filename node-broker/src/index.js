const express = require('express');
const routes = require('./routes');

const PORT = process.env.PORT || 3001;

const app = express();

app.use(express.json());
app.use('/api', routes);

app.listen(PORT, () => {
  console.log(`Broker running on http://localhost:${PORT}`);
});
