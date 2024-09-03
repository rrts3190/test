#!/bin/bash

# Path to the file containing server names/IPs
SERVER_LIST="servers.txt"

# SSH credentials
USERNAME="axotcuat"
PASSWORD="axotc123"

# Path to the Oracle client directory
ORACLE_CLIENT_PATH="/path/to/oracle/client/19.5.0"

# File to be removed
FILE_TO_REMOVE="rt.jar"

# Loop through each server
while IFS= read -r SERVER; do
    echo "Connecting to $SERVER..."

    # Remove the file rt.jar
    sshpass -p "$PASSWORD" ssh -o StrictHostKeyChecking=no "$USERNAME@$SERVER" "rm -f $ORACLE_CLIENT_PATH/lib/$FILE_TO_REMOVE"

    # Check if the file was successfully removed
    sshpass -p "$PASSWORD" ssh -o StrictHostKeyChecking=no "$USERNAME@$SERVER" "if [ ! -f $ORACLE_CLIENT_PATH/lib/$FILE_TO_REMOVE ]; then echo 'File $FILE_TO_REMOVE removed successfully on $SERVER'; else echo 'Failed to remove $FILE_TO_REMOVE on $SERVER'; fi"

    # Prune Docker containers
    sshpass -p "$PASSWORD" ssh -o StrictHostKeyChecking=no "$USERNAME@$SERVER" "docker container prune -f"

    echo "Pruned unused Docker containers on $SERVER"
done < "$SERVER_LIST"

echo "Process completed."
