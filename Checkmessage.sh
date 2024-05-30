#!/bin/bash

# Files
numbers_file="numbers.txt"
data_file="data.txt"

# Check if files exist
if [[ ! -f "$numbers_file" ]]; then
  echo "File $numbers_file does not exist."
  exit 1
fi

if [[ ! -f "$data_file" ]]; then
  echo "File $data_file does not exist."
  exit 1
fi

# Read numbers and search in data file
while read -r number; do
  if grep -q "$number" "$data_file"; then
    echo "Number $number is present in $data_file"
  else
    echo "Number $number is not present in $data_file"
  fi
done < "$numbers_file"
