#!/bin/bash

# Extract version from pom.xml
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

echo "Current project version: $VERSION"

# Define the file to update
README_FILE="README.md"

# Check if README exists
if [ ! -f "$README_FILE" ]; then
    echo "Error: $README_FILE not found!"
    exit 1
fi

# Update the version in the README badge
# This looks for the pattern "version-X.X.X-" and replaces it
if [[ "$OSTYPE" == "darwin"* ]]; then
    # MacOS sed requires an empty string for -i
    sed -i '' "s/version-[0-9.]*-[a-zA-Z]*/version-$VERSION-blue/" "$README_FILE"
else
    # Linux sed
    sed -i "s/version-[0-9.]*-[a-zA-Z]*/version-$VERSION-blue/" "$README_FILE"
fi

echo "Updated $README_FILE with version $VERSION"
