#!/bin/bash

# Configuration
EMS_ADMIN_TOOL="/path/to/tibemsadmin"  # Update with the actual path to tibemsadmin
EMS_HOST="tcp://localhost:7222"        # EMS server URL
EMS_USER="admin"                       # EMS username
EMS_PASSWORD="password"                # EMS password
CHECK_INTERVAL=300                     # Time in seconds between size checks
SIZE_THRESHOLD_MB=500                  # Threshold size (in MB) to trigger compaction

# Function to fetch the current database size
get_db_size() {
    output=$($EMS_ADMIN_TOOL -server "$EMS_HOST" -user "$EMS_USER" -password "$EMS_PASSWORD" "show dbinfo" 2>/dev/null)
    if [[ $? -ne 0 ]]; then
        echo "Error: Failed to fetch database info."
        return 1
    fi
    echo "$output" | grep -i "Size" | awk -F':' '{print $2}' | tr -d '[:space:]' | tr -d 'MB'
}

# Function to compact the database
compact_db() {
    $EMS_ADMIN_TOOL -server "$EMS_HOST" -user "$EMS_USER" -password "$EMS_PASSWORD" "compact db" 2>/dev/null
    if [[ $? -eq 0 ]]; then
        echo "$(date): Database compaction triggered successfully."
    else
        echo "$(date): Error compacting database."
    fi
}

# Monitor and compact database asynchronously
monitor_db() {
    while true; do
        db_size=$(get_db_size)
        if [[ $? -eq 0 ]]; then
            echo "$(date): Current database size: ${db_size}MB"
            if [[ "$db_size" -ge "$SIZE_THRESHOLD_MB" ]]; then
                echo "$(date): Database size exceeds threshold. Compacting..."
                compact_db
            fi
        fi
        sleep "$CHECK_INTERVAL"
    done
}

# Start the monitoring in the background
echo "Starting database monitoring. Press Ctrl+C to stop."
monitor_db &
monitor_pid=$!

# Handle script termination
trap "echo 'Stopping monitoring...'; kill $monitor_pid; exit" SIGINT SIGTERM

# Keep the script running
wait $monitor_pid
