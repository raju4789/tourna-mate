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

ARG BUILD_MODE=production

# Build the project (development mode by default)
RUN npm run build:${BUILD_MODE}

# Production stage
FROM nginx:stable-alpine as production-stage
COPY --from=BUILD_IMAGE /app/dist /usr/share/nginx/html
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
