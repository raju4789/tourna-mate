#!/bin/bash
# =============================================================================
# TOURNA-MATE - Start Development Environment
# =============================================================================
# Starts isolated development environment on ports 8xxx
# Database: 3306, UI: 8002, Gateway: 8080, Grafana: 3000
# =============================================================================

set -e

# Change to docker/dev directory
cd "$(dirname "$0")/docker/dev"

echo "=============================================="
echo "🚀 Starting TOURNA-MATE (Development)"
echo "=============================================="

# Check if .env.dev exists
if [ ! -f "../secrets/.env.dev" ]; then
    echo "❌ ERROR: docker/secrets/.env.dev not found!"
    echo ""
    echo "Please create it from the template:"
    echo "  cd docker/secrets"
    echo "  cp .env.example .env.dev"
    echo "  # Edit .env.dev with your development secrets"
    echo ""
    exit 1
fi

echo "✅ Found secrets/.env.dev"
echo ""

# Load environment variables from .env.dev
set -a  # automatically export all variables
source ../secrets/.env.dev
set +a

export ENVIRONMENT=development

echo "📦 Starting services with docker-compose.yml..."
echo ""

# Start services and wait for them to be healthy
docker-compose --env-file ../secrets/.env.dev up -d --wait

echo ""
echo "=============================================="
echo "✅ Development environment started!"
echo "=============================================="
echo ""
echo "📊 Service URLs:"
echo "  - Gateway:    http://localhost:8080"
echo "  - UI:         http://localhost:8002"
echo "  - Eureka:     http://localhost:8761"
echo "  - Vault:      http://localhost:8200"
echo "  - Grafana:    http://localhost:3000"
echo "  - Prometheus: http://localhost:9090"
echo "  - phpMyAdmin: http://localhost:8090 (user: tournament_admin)"
echo "  - MySQL:      localhost:3306"
echo ""
echo "🔍 Check status:"
echo "  cd docker/dev && docker-compose ps"
echo ""
echo "📋 View logs:"
echo "  cd docker/dev && docker-compose logs -f [service-name]"
echo ""
echo "🛑 Stop services:"
echo "  cd docker/dev && docker-compose down"
echo "=============================================="
