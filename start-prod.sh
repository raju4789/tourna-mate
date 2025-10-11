#!/bin/bash
# =============================================================================
# TOURNA-MATE - Start Production Environment
# =============================================================================
# Starts isolated production environment on ports 10xxx
# Database: 3308, UI: 10002, Gateway: 10080, Grafana: 3002
# ⚠️  PRODUCTION - Use with caution!
# =============================================================================

set -e

# Change to docker directory
cd "$(dirname "$0")/docker"

echo "=============================================="
echo "🚀 Starting TOURNA-MATE (Production)"
echo "=============================================="
echo "⚠️  WARNING: You are starting PRODUCTION!"
echo "=============================================="

# Check if .env.prod exists
if [ ! -f "secrets/.env.prod" ]; then
    echo "❌ ERROR: secrets/.env.prod not found!"
    echo ""
    echo "For production, you should:"
    echo "  1. Use AWS Secrets Manager / Azure Key Vault"
    echo "  2. OR create secrets/.env.prod with STRONG passwords"
    echo ""
    echo "To create .env.prod:"
    echo "  cd docker/secrets"
    echo "  cp .env.example .env.prod"
    echo "  # Generate strong passwords with: openssl rand -base64 32"
    echo "  # Replace ALL placeholders in .env.prod"
    echo ""
    exit 1
fi

echo "✅ Found secrets/.env.prod"
echo ""

# Validate production secrets
echo "🔍 Validating production secrets..."
if grep -q "REPLACE_WITH_" secrets/.env.prod; then
    echo "❌ ERROR: Production secrets contain placeholder values!"
    echo ""
    echo "Please replace all 'REPLACE_WITH_' values with strong passwords:"
    echo "  openssl rand -base64 32  # For each secret"
    echo ""
    exit 1
fi

echo "✅ Production secrets validated"
echo ""

# Load environment variables from .env.prod BEFORE the confirmation
set -a  # automatically export all variables
source secrets/.env.prod
set +a

export ENVIRONMENT=production

# Confirm production start
read -p "⚠️  Are you sure you want to start PRODUCTION? (yes/no): " confirm
if [ "$confirm" != "yes" ]; then
    echo "❌ Production start cancelled"
    exit 0
fi

echo ""
echo "📦 Starting services with docker-compose.prod.yml..."
echo ""

# Start services and wait for them to be healthy
docker-compose -f docker-compose.prod.yml up -d --wait

echo ""
echo "=============================================="
echo "✅ Production environment started!"
echo "=============================================="
echo ""
echo "📊 Service URLs:"
echo "  - Gateway:    http://localhost:10080"
echo "  - UI:         http://localhost:10002"
echo "  - Eureka:     http://localhost:10761"
echo "  - Vault:      http://localhost:8202"
echo "  - Grafana:    http://localhost:3002"
echo "  - Prometheus: http://localhost:9092"
echo "  - MySQL:      localhost:3308"
echo ""
echo "🔍 Check status:"
echo "  cd docker && docker-compose -f docker-compose.prod.yml ps"
echo ""
echo "📋 View logs:"
echo "  cd docker && docker-compose -f docker-compose.prod.yml logs -f [service-name]"
echo ""
echo "🛑 Stop services:"
echo "  cd docker && docker-compose -f docker-compose.prod.yml down"
echo "=============================================="
