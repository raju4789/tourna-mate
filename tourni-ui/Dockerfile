# Build stage
FROM node:18.19.0-slim as build-stage

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package.json .

# Install dependencies
RUN npm install

# Copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

# Build the app
CMD ["npm", "run", "dev"]
