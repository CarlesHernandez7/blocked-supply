FROM node:18
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .

# Copy and prepare entrypoint script
COPY docker-entrypoint.sh .
RUN chmod +x docker-entrypoint.sh
EXPOSE 3001

# Run the entrypoint script
CMD ["./docker-entrypoint.sh"]
