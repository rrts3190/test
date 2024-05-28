#!/bin/bash

# Specify the file
file="path/to/your/file/emsqueue-PRD.NM.AXOTC.Consumer.NFPSGateway.RT-2024.05.17.txt.gz"

# Check if the file exists and was modified more than 5 minutes ago
if [ -e "$file" ] && [ $(find "$file" -mmin +5) ]; then
    # Create the new filename by replacing 17 with 27
    new_file="${file/17/27}"
    
    # Rename the file
    mv "$file" "$new_file"
    
    # Print the renaming action
    echo "Renamed: $file -> $new_file"
else
    echo "File does not exist or was modified less than 5 minutes ago."
fi
