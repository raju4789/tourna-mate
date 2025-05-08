# ----------- Build Stage -----------
FROM node:18-alpine AS builder

WORKDIR /app

# Install dependencies using npm ci for clean, reproducible builds
COPY package*.json ./
RUN npm ci

# Copy the rest of your source code
COPY . .

# Build the Vite app for production
RUN npm run build

# ----------- Production Stage -----------
FROM nginx:1.25-alpine AS production

# Remove default nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy built assets from builder
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom nginx config for SPA routing
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]