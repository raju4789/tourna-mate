# Use an official Node runtime as a parent image
FROM node:18.19.0-slim

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package.json package-lock.json* ./

# Install project dependencies
RUN npm install
# If you use Yarn, replace the above line with:
# RUN yarn install --frozen-lockfile

# Copy the rest of your app's source code
COPY . .

# Copy the entrypoint script into the image
COPY entrypoint.sh /usr/local/bin/

# Make the entrypoint script executable
RUN chmod +x /usr/local/bin/entrypoint.sh

# Set the entrypoint script to run when the container starts
ENTRYPOINT ["entrypoint.sh"]

# Expose the port the app runs on
EXPOSE 8000