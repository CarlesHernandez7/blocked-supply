#!/bin/sh

echo "🔁 Waiting for Ganache at $GANACHE_URL..."

# Wait for Ganache to be available
until curl --silent --fail "$GANACHE_URL" > /dev/null; do
  echo "Ganache not available yet. Retrying in 2 seconds..."
  sleep 2
done

echo "Ganache is up! Starting broker..."

# Start your app (replace with actual command if different)
exec node src/index.js
