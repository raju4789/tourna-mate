# Use an official Node runtime as a parent image
FROM node:18-alpine AS BUILD_IMAGE

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json ./

# Install project dependencies
RUN npm install

# Copy the rest of your app's source code
COPY . .

EXPOSE 8000

CMD ["npm", "run", "dev"]
