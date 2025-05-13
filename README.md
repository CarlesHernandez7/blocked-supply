
# 🔗 Blocked Supply: Blockchain-Based Supply Chain System

This project implements a blockchain-based supply chain management system composed of a **frontend**, **backend**, and **blockchain smart contracts**. It allows tracking and managing supply chain operations securely via smart contracts.

---

👉 Here you'll find detailed instructions for installing Node.js, Java, MySQL, and running each service individually. If you prefer a Docker-based setup, check out the [docker branch](https://github.com/CarlesHernandez7/blocked-supply/tree/docker).

## 📦 Project Components

- **Frontend**: Next.js application for the user interface
- **Backend**: Spring Boot application for business logic and REST APIs
- **Blockchain**: Smart contracts deployed on Ganache (local Ethereum blockchain)
- **Node Broker**: Middleware to bridge backend and blockchain

---

## ✅ Prerequisites

Before running the project, ensure the following are installed:

- **Node.js** ≥ 18.x  
- **Java JDK** ≥ 17  
- **MySQL** ≥ 8.x  
- **Truffle**  
  ```bash
  npm install -g truffle
  ```
- **Ganache CLI**  
  ```bash
  npm install -g ganache-cli
  ```

---

## 🚀 Running the Project

### 1️⃣ Blockchain Setup

```bash
# Start Ganache
ganache-cli --host 0.0.0.0 --port 8545
```

```bash
# Deploy Smart Contracts
cd blocked-supply-truffle
truffle migrate
```

---

### 2️⃣ Node Broker Setup

```bash
cd node-broker
npm install
```

Create a `.env` file with the following:

```env
BLOCKCHAIN_NODE_URL=http://localhost:8545
CONTRACT_ADDRESS=<address-from-truffle-migration>
PORT=3001
```

```bash
# Start the broker
node src/index.js
```

---

### 3️⃣ Backend Setup

#### 🛠️ MySQL Configuration

1. Create a database:
   - Name: `shipment_db`

2. Note your MySQL username and password.

---

#### 🔐 Generate Keys

- `JWT_SECRET_KEY`: 64+ character plain text string
- `ENCRYPTION_SECRET_KEY`: Format: Base64-encoded string. Length after decoding: Exactly 16 bytes (128 bits)

---

#### ⚙️ Configure Spring Boot

Copy `application-example.properties` to `application.properties` in the backend’s `resources` directory.

Update values with MySQL credentials and Generated Keys:
```properties
spring.application.name=blocked-supply-backend
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/shipment_db
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

application.security.jwt.secret-key=${JWT_SECRET_KEY}
application.security.jwt.expiration=86400000
application.security.jwt.refresh-token.expiration=604800000
application.security.encryption.secret-key=${ENCRYPTION_SECRET_KEY}

application.broker.address=http://localhost:3001
```

---

#### ▶️ Run the Backend

```bash
cd blocked-supply-backend
./mvnw spring-boot:run
```

---

### 4️⃣ Frontend Setup

```bash
cd blocked-supply-frontend
npm install
```

Create `.env.local` file:

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

```bash
npm run build
npm run start
```

---

## 🌐 Accessing the Application

| Component   | URL                     |
|-------------|--------------------------|
| Frontend    | http://localhost:3000    |
| Backend API | http://localhost:8080    |
| Node Broker | http://localhost:3001    |
| Ganache     | http://localhost:8545    |

---

## 🔒 Security Notes

- Never commit `.env` files or keys to version control
- Use strong credentials for your database and keys
- Use environment variables or secret managers in production

---

## 🛠️ Troubleshooting

### ❌ Ganache Connection Issues

- Ensure Ganache is running on `localhost:8545`
- Check that `BLOCKCHAIN_NODE_URL` in `.env` is correct

### ❌ Backend Fails to Start

- Verify MySQL is running and credentials are correct
- Ensure `JWT_SECRET_KEY` and `ENCRYPTION_SECRET_KEY` are defined
- Check broker address in `application.properties`

### ❌ Frontend Build Issues

```bash
rm -rf node_modules package-lock.json
npm install
```
- Ensure `.env.local` is properly configured

---

## 💬 Support

For issues or questions, feel free to [open an issue](https://github.com/your-repo-url/issues) in this repository.

---
