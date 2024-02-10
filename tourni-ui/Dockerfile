# Use an official Node runtime as a parent image
FROM node:18.19.0-slim

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package.json .

# Install project dependencies
RUN npm install
# If you use Yarn, replace the above line with:
# RUN yarn install --frozen-lockfile

# Copy the rest of your app's source code
COPY . .

# Build the app
CMD ["npm", "run", "dev"]
