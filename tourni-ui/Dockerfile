# Use an official Node runtime as a parent image
FROM node:18.19.0-slim

# Set the working directory
WORKDIR /app

# Define the build argument for the build mode
ARG BUILD_MODE

# Copy package.json and package-lock.json
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the rest of your app's source code
COPY . .

# Build your app for the specified build mode
RUN npm run build:${BUILD_MODE}

# Install a simple http server for serving static content
RUN npm install -g serve

# Start the server using the build directory
CMD ["serve", "-s", "dist"]