#!/bin/bash
set -e

echo "=============================================="
echo "🔐 TOURNA-MATE Vault Initialization"
echo "=============================================="
echo "Environment: ${ENVIRONMENT:-unknown}"
echo "Vault Address: ${VAULT_ADDR}"
echo "=============================================="

# Wait for Vault to be ready
echo "⏳ Waiting for Vault to be ready..."
MAX_RETRIES=30
RETRY_COUNT=0

until vault status 2>/dev/null; do
    RETRY_COUNT=$((RETRY_COUNT + 1))
    if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
        echo "❌ ERROR: Vault failed to start after ${MAX_RETRIES} attempts"
        exit 1
    fi
    echo "   Attempt ${RETRY_COUNT}/${MAX_RETRIES}..."
    sleep 2
done

echo "✅ Vault is ready!"
echo ""

# Enable KV secrets engine (ignore error if already enabled)
echo "📝 Enabling KV secrets engine..."
vault secrets enable -path=secret kv-v2 2>/dev/null || echo "   (already enabled)"
echo ""

# Validate that required environment variables are set
echo "🔍 Validating environment variables..."

if [ -z "$MYSQL_ROOT_PASSWORD" ]; then
    echo "❌ ERROR: MYSQL_ROOT_PASSWORD is not set"
    exit 1
fi

if [ -z "$MYSQL_PASSWORD" ]; then
    echo "❌ ERROR: MYSQL_PASSWORD is not set"
    exit 1
fi

if [ -z "$JWT_SECRET" ]; then
    echo "❌ ERROR: JWT_SECRET is not set"
    exit 1
fi

if [ -z "$GRAFANA_PASSWORD" ]; then
    echo "❌ ERROR: GRAFANA_PASSWORD is not set"
    exit 1
fi

if [ -z "$ADMIN_PASSWORD" ]; then
    echo "❌ ERROR: ADMIN_PASSWORD is not set"
    exit 1
fi

echo "✅ All required environment variables are set"
echo ""

# Store secrets in Vault
echo "💾 Storing secrets in Vault..."
vault kv put secret/mysql_root_password value="${MYSQL_ROOT_PASSWORD}"
echo "   ✓ mysql_root_password"

vault kv put secret/mysql_password value="${MYSQL_PASSWORD}"
echo "   ✓ mysql_password"

vault kv put secret/jwt_secret value="${JWT_SECRET}"
echo "   ✓ jwt_secret"

vault kv put secret/grafana_password value="${GRAFANA_PASSWORD}"
echo "   ✓ grafana_password"

vault kv put secret/admin_password value="${ADMIN_PASSWORD}"
echo "   ✓ admin_password"

echo ""
echo "✅ All secrets stored in Vault successfully!"
echo ""

# Create secret files for applications
echo "📁 Creating secret files in /vault/secrets/..."
mkdir -p /vault/secrets

printf "%s" "${MYSQL_ROOT_PASSWORD}" > /vault/secrets/mysql_root_password
printf "%s" "${MYSQL_PASSWORD}" > /vault/secrets/mysql_password
printf "%s" "${JWT_SECRET}" > /vault/secrets/jwt_secret
printf "%s" "${GRAFANA_PASSWORD}" > /vault/secrets/grafana_password
printf "%s" "${ADMIN_PASSWORD}" > /vault/secrets/admin_password

# Set permissions readable by all users (644)
# This allows MySQL (user 999) and Grafana (user 472) to read the files
chmod 644 /vault/secrets/*

echo "✅ Secret files created with permissions (644 - readable by all)"
echo ""

# Verify secrets
echo "🔍 Verifying secrets..."
for file in mysql_root_password mysql_password jwt_secret grafana_password admin_password; do
    if [ -f "/vault/secrets/$file" ] && [ -s "/vault/secrets/$file" ]; then
        SIZE=$(wc -c < "/vault/secrets/$file" | tr -d ' ')
        echo "   ✓ $file (${SIZE} bytes)"
    else
        echo "   ❌ $file - MISSING OR EMPTY!"
        exit 1
    fi
done

echo ""
echo "=============================================="
echo "✅ Vault initialization complete!"
echo "=============================================="
echo "Environment: ${ENVIRONMENT}"
echo "Secrets location: /vault/secrets/"
echo "=============================================="
echo ""

# Keep container running
echo "🔄 Vault Agent running... (keeping container alive)"
sleep infinity
