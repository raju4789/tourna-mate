# ========== Build Stage ==========
FROM node:18-alpine AS builder

WORKDIR /app

# Copy package files first for better layer caching
COPY package*.json ./

# Install all dependencies (including dev dependencies needed for build)
RUN npm ci && \
    npm cache clean --force

# Copy source code
COPY . .

# Build the Vite app for production
RUN npm run build

# ========== Production Stage ==========
FROM nginx:1.25-alpine AS production

# Add metadata labels
LABEL maintainer="tourna-mate-team" \
      version="1.0" \
      description="Tournament Management UI"

# Remove default nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy built assets from builder
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom nginx config template for SPA routing
COPY nginx.conf /etc/nginx/nginx.conf.template

# Set proper permissions for nginx user (already exists in base image)
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chown -R nginx:nginx /var/cache/nginx && \
    chown -R nginx:nginx /var/log/nginx && \
    chown -R nginx:nginx /etc/nginx/conf.d && \
    chown nginx:nginx /etc/nginx/nginx.conf.template && \
    touch /var/run/nginx.pid && \
    chown -R nginx:nginx /var/run/nginx.pid && \
    # Allow nginx user to write the final nginx.conf
    touch /etc/nginx/nginx.conf && \
    chown nginx:nginx /etc/nginx/nginx.conf

# Switch to non-root user
USER nginx

# Expose port 8000 (matching nginx.conf)
EXPOSE 8000

# Set default GATEWAY_HOST if not provided
ENV GATEWAY_HOST=tourni-gateway

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8000/ || exit 1

# Start nginx with environment variable substitution
CMD ["/bin/sh", "-c", "envsubst '${GATEWAY_HOST}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf && nginx -g 'daemon off;'"]