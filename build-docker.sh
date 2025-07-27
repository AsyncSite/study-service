#!/bin/bash

# Build script for study-service Docker image
# This script builds the application locally first, then creates Docker image

echo "🔨 Building study-service JAR..."
export JAVA_HOME=$(/usr/libexec/java_home)
./gradlew clean build -x test

if [ $? -ne 0 ]; then
    echo "❌ Gradle build failed!"
    exit 1
fi

echo "🐳 Building Docker image..."
docker build -t asyncsite/study-service:latest .

if [ $? -ne 0 ]; then
    echo "❌ Docker build failed!"
    exit 1
fi

echo "✅ Build complete! Image: asyncsite/study-service:latest"
echo ""
echo "To run the service locally with Docker Compose:"
echo "  docker-compose up -d"
echo ""
echo "To run the service standalone:"
echo "  docker run -p 8083:8083 -e SPRING_PROFILES_ACTIVE=docker asyncsite/study-service:latest"