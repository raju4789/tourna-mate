#!/bin/bash
# =============================================================================
# TOURNA-MATE - Start Staging Environment
# =============================================================================
# Starts isolated staging environment on ports 9xxx
# Database: 3307, UI: 9002, Gateway: 9080, Grafana: 3001
# =============================================================================

set -e

# Change to docker directory
cd "$(dirname "$0")/docker"

echo "=============================================="
echo "🚀 Starting TOURNA-MATE (Staging)"
echo "=============================================="

# Check if .env.staging exists
if [ ! -f "secrets/.env.staging" ]; then
    echo "❌ ERROR: secrets/.env.staging not found!"
    echo ""
    echo "Please create it from the template:"
    echo "  cd docker/secrets"
    echo "  cp .env.example .env.staging"
    echo "  # Edit .env.staging with your staging secrets"
    echo ""
    exit 1
fi

echo "✅ Found secrets/.env.staging"
echo ""

# Load environment variables from .env.staging
set -a  # automatically export all variables
source secrets/.env.staging
set +a

export ENVIRONMENT=staging

echo "📦 Starting services with docker-compose.staging.yml..."
echo ""

# Start services and wait for them to be healthy
docker-compose -f docker-compose.staging.yml up -d --wait

echo ""
echo "=============================================="
echo "✅ Staging environment started!"
echo "=============================================="
echo ""
echo "📊 Service URLs:"
echo "  - Gateway:    http://localhost:9080"
echo "  - UI:         http://localhost:9002"
echo "  - Eureka:     http://localhost:9761"
echo "  - Vault:      http://localhost:8201"
echo "  - Grafana:    http://localhost:3001"
echo "  - Prometheus: http://localhost:9091"
echo "  - MySQL:      localhost:3307"
echo ""
echo "🔍 Check status:"
echo "  cd docker && docker-compose -f docker-compose.staging.yml ps"
echo ""
echo "📋 View logs:"
echo "  cd docker && docker-compose -f docker-compose.staging.yml logs -f [service-name]"
echo ""
echo "🛑 Stop services:"
echo "  cd docker && docker-compose.staging.yml down"
echo "=============================================="
