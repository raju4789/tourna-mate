# Use an official Node runtime as a parent image
FROM node:18.19.0-slim

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json (or yarn.lock if you use Yarn)
COPY package*.json ./
# If you use Yarn, also copy yarn.lock and uncomment the next line
# COPY yarn.lock ./

# Install project dependencies
RUN npm install
# If you use Yarn, replace the above line with:
# RUN yarn install --frozen-lockfile

# Copy the rest of your app's source code
COPY . .

# Build the React application using Vite
RUN npm run build
# If you use Yarn, replace the above line with:
# RUN yarn build

# Install a simple HTTP server to serve static content
RUN npm install -g http-server
# If you use Yarn, replace the above line with:
# RUN yarn global add http-server

# Set the command to start the server
CMD ["http-server", "dist", "-p", "3000"]

# Expose the port the app runs on
EXPOSE 3000