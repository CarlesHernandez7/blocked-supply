
# 🔗 Blocked Supply: Blockchain-Based Supply Chain System <br> (🐳 Dockerized Version)

This project implements a blockchain-based supply chain management system composed of a **frontend**, **backend**, and **blockchain smart contracts**. It allows tracking and managing supply chain operations securely via smart contracts.

This version of the project leverages **Docker** and **Docker Compose** to containerize and orchestrate all components of the blockchain-based supply chain system. It includes containers for the frontend, backend, blockchain broker, and MySQL database.

---

## 📦 Project Components

- **Frontend**: Next.js application container
- **Backend**: Spring Boot service container
- **Blockchain Broker**: Node.js service container
- **Database**: MySQL container
- **Blockchain**: Requires local Ganache (outside Docker)

---

## ✅ Prerequisites

Make sure the following are installed on your system:

- **Docker** ≥ 20.x  
- **Docker Compose** ≥ 1.29  
- **Node.js** ≥ 18.x (for blockchain setup)  
- **Truffle**  
  ```bash
  npm install -g truffle
  ```
- **Ganache CLI**  
  ```bash
  npm install -g ganache-cli
  ```

> ℹ️ **Ganache is run locally outside Docker** so `host.docker.internal` can reference it from inside containers.

---

## 🚀 Running the Project

### 1️⃣ Start Ganache (Local Blockchain)

Open a terminal and start Ganache:

```bash
ganache-cli --host 0.0.0.0 --port 8545
```

---

### 2️⃣ Deploy Smart Contracts

In another terminal:

```bash
cd blocked-supply-truffle
truffle migrate
```

> 📌 Note the contract address output from the migration – it's already added in `docker-compose.yml` under `CONTRACT_ADDRESS` in the `broker` service. Update it if needed.

---

### 3️⃣ Start All Services with Docker Compose

From the project root directory:

```bash
docker-compose up --build
```

> 🐢 First-time builds may take a few minutes.

---

## 🌐 Accessing the Application

| Component   | URL                     |
|-------------|--------------------------|
| Frontend    | http://localhost:3000    |
| Backend API | http://localhost:8080    |
| Node Broker | http://localhost:3001    |
| Ganache     | http://localhost:8545    |

---

## 🔐 Security Notes

- Secrets such as `JWT_SECRET_KEY` and `ENCRYPTION_SECRET_KEY` are hardcoded in `docker-compose.yml` for simplicity in development. **Do not use these in production.**
- Use `.env` files or Docker secrets for secure production deployments.

---

## 🛠️ Troubleshooting

### ❌ Blockchain Connection Issues

- Ensure Ganache is running on `localhost:8545`
- Check `BLOCKCHAIN_NODE_URL` in the `broker` service uses `host.docker.internal`

### ❌ Backend Container Won't Start

- Make sure `mysql` container is healthy and ready
- Check port `3307` on your host isn't blocked or in use

### ❌ Frontend Not Reaching Backend

- Ensure `NEXT_PUBLIC_API_URL=http://backend:8080` is set in frontend container
- Containers communicate over the Docker network via service names (e.g., `backend`, `broker`)

---

## 📦 Stopping and Cleaning Up

```bash
docker-compose down -v
```

> 🧹 This stops containers and removes named volumes (like MySQL data).

---

## 💬 Support

For help or bug reports, [open an issue](https://github.com/your-repo-url/issues).

---

