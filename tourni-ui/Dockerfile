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

ARG BUILD_MODE

# Build the project (development mode by default)
RUN npm run build:${BUILD_MODE}

FROM node:18-alpine AS PRODUCTION_IMAGE

WORKDIR /app

COPY --from=BUILD_IMAGE /app/dist/ /app/dist/

COPY package.json ./

COPY vite.config.ts ./

RUN npm install typescript

EXPOSE 8000

CMD ["npm", "run", "preview"]



