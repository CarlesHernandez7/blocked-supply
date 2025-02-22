FROM node:18
RUN npm install -g truffle
WORKDIR /app
COPY . /app/
RUN npm install
CMD ["tail", "-f", "/dev/null"]