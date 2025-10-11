#!/bin/bash
# =============================================================================
# TOURNA-MATE - Check All Environments
# =============================================================================
# Shows which environments are running and their service status
# =============================================================================

echo ""
echo "╔══════════════════════════════════════════════════════════════════════╗"
echo "║          🔍 TOURNA-MATE Environment Status Check                     ║"
echo "╚══════════════════════════════════════════════════════════════════════╝"
echo ""

# Function to check if any containers from an environment are running
check_env() {
    local env=$1
    local container_pattern=$2
    local running_count=$(docker ps --filter "name=${container_pattern}" --format "{{.Names}}" | wc -l | tr -d ' ')
    echo "$running_count"
}

# Check each environment
dev_count=$(check_env "Development" "-dev")
staging_count=$(check_env "Staging" "-staging")
prod_count=$(check_env "Production" "-prod")

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  ENVIRONMENT SUMMARY"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ "$dev_count" -gt 0 ]; then
    echo "  ✅ Development   : $dev_count containers running"
else
    echo "  ⭕ Development   : Not running"
fi

if [ "$staging_count" -gt 0 ]; then
    echo "  ✅ Staging       : $staging_count containers running"
else
    echo "  ⭕ Staging       : Not running"
fi

if [ "$prod_count" -gt 0 ]; then
    echo "  ✅ Production    : $prod_count containers running"
else
    echo "  ⭕ Production    : Not running"
fi

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Show details for running environments
if [ "$dev_count" -gt 0 ]; then
    echo "┌──────────────────────────────────────────────────────────────────────┐"
    echo "│  🔷 DEVELOPMENT ENVIRONMENT (Ports: 8xxx)                            │"
    echo "└──────────────────────────────────────────────────────────────────────┘"
    echo ""
    docker ps --filter "name=-dev" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | head -15
    echo ""
    echo "  📊 URLs:"
    echo "    • Gateway:    http://localhost:8080"
    echo "    • UI:         http://localhost:8002"
    echo "    • Eureka:     http://localhost:8761"
    echo "    • Grafana:    http://localhost:3000"
    echo "    • MySQL:      localhost:3306"
    echo ""
fi

if [ "$staging_count" -gt 0 ]; then
    echo "┌──────────────────────────────────────────────────────────────────────┐"
    echo "│  🔶 STAGING ENVIRONMENT (Ports: 9xxx)                                │"
    echo "└──────────────────────────────────────────────────────────────────────┘"
    echo ""
    docker ps --filter "name=-staging" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | head -15
    echo ""
    echo "  📊 URLs:"
    echo "    • Gateway:    http://localhost:9080"
    echo "    • UI:         http://localhost:9002"
    echo "    • Eureka:     http://localhost:9761"
    echo "    • Grafana:    http://localhost:3001"
    echo "    • MySQL:      localhost:3307"
    echo ""
fi

if [ "$prod_count" -gt 0 ]; then
    echo "┌──────────────────────────────────────────────────────────────────────┐"
    echo "│  🔴 PRODUCTION ENVIRONMENT (Ports: 10xxx)                            │"
    echo "└──────────────────────────────────────────────────────────────────────┘"
    echo ""
    docker ps --filter "name=-prod" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | head -15
    echo ""
    echo "  📊 URLs:"
    echo "    • Gateway:    http://localhost:10080"
    echo "    • UI:         http://localhost:10002"
    echo "    • Eureka:     http://localhost:10761"
    echo "    • Grafana:    http://localhost:3002"
    echo "    • MySQL:      localhost:3308"
    echo ""
fi

# Show quick actions
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  QUICK ACTIONS"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "  Start environments:"
echo "    ./start-dev.sh        # Start development"
echo "    ./start-staging.sh    # Start staging"
echo "    ./start-prod.sh       # Start production"
echo ""
echo "  Stop environments:"
echo "    cd docker && docker-compose -f docker-compose.dev.yml down"
echo "    cd docker && docker-compose -f docker-compose.staging.yml down"
echo "    cd docker && docker-compose -f docker-compose.prod.yml down"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

