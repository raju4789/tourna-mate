#!/bin/bash
# wait-for-it.sh

# Usage: ./wait-for-it.sh host:port [-s] [-t timeout] [-- command args]
#   -s: Strict mode, exit with non-zero status if the host is unreachable
#   -t TIMEOUT: Timeout in seconds, zero for no timeout
#   -- COMMAND ARGS: Execute the specified command with arguments after the wait completes

host_and_port=$1
shift

strict=false
timeout=0

while [[ $# -gt 0 ]]
do
    case "$1" in
        -s)
            strict=true
            shift
            ;;
        -t)
            timeout=$2
            if [[ ! "$timeout" =~ ^[0-9]+$ ]]; then
                echo "Error: Invalid timeout value: $timeout"
                exit 1
            fi
            shift 2
            ;;
        --)
            shift
            break
            ;;
        *)
            echo "Error: Unknown argument: $1"
            exit 1
            ;;
    esac
done

IFS=':' read -r host port <<< $(echo "$host_and_port" | sed 's/:/ /')

wait_for_service() {
    local host=$1
    local port=$2
    local timeout=$3

    local start_time=$(date +%s)

    while true; do
        nc -z -w 1 "$host" "$port" >/dev/null 2>&1
        result=$?

        if [ $result -eq 0 ]; then
            break
        fi

        sleep 1

        local current_time=$(date +%s)
        local elapsed_time=$((current_time - start_time))

        if [ $timeout -gt 0 ] && [ $elapsed_time -ge $timeout ]; then
            echo "Timeout reached, service not available"
            exit 1
        fi
    done
}

if [ "$strict" = true ]; then
    wait_for_service "$host" "$port" "$timeout"
else
    # In non-strict mode, just try to wait without exiting on failure
    wait_for_service "$host" "$port" "$timeout" || true
fi

# Execute the specified command with arguments
exec "$@"
