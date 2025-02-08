#!/bin/bash

set -e  # Exit immediately if a command exits with a non-zero status
set -u  # Treat unset variables as an error

# Set the source file and class name
SOURCE_FILE="src/main/java/com/example/Main.java"
CLASS_NAME="com.example.Main"
JAR_FILE="target/main.jar"

# Create the target directory if it doesn't exist
mkdir -p target

# Copy config.properties and file system input to the target directory
cp src/main/resources/config.properties target/
cp src/main/resources/input.txt target/input.txt

# Compile the Java code
javac -d target "$SOURCE_FILE"

# Create a temporary directory for the manifest file
TEMP_DIR=$(mktemp -d)
MANIFEST_FILE="$TEMP_DIR/manifest.txt"

# Create the manifest file
echo "Main-Class: $CLASS_NAME" > "$MANIFEST_FILE"

# Create the JAR file
jar cvfm "$JAR_FILE" "$MANIFEST_FILE" -C target .

# Clean up the manifest file
rm -rf "$TEMP_DIR"

# Run the JAR file
java -jar "$JAR_FILE"