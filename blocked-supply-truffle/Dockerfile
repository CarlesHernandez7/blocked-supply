FROM node:18

# Instalar Truffle globalmente
RUN npm install -g truffle

# Crear y usar el directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto Truffle
COPY . .

# Instalar dependencias
RUN npm install

# Comando para desplegar contratos cuando el contenedor se ejecute
CMD ["truffle", "migrate", "--reset", "--network", "development"]
