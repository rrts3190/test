#!/bin/bash

# Directory containing the files
directory="path/to/your/files"

# Loop through files matching the pattern with the date 17
for file in "$directory"/*17*.txt.gz; 
do
    # Check if the file exists to avoid issues if no files match the pattern
    if [ -e "$file" ]; then
        # Create the new filename by replacing 17 with 27
        new_file="${file/17/27}"
        
        # Rename the file
        mv "$file" "$new_file"
        
        # Print the renaming action
        echo "Renamed: $file -> $new_file"
    else
        echo "No files found with the date 17 in the filename."
    fi
done
