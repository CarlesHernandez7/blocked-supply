FROM node:18-alpine

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --frozen-lockfile

COPY . .
EXPOSE 3001
CMD ["node", "src/index.js"]