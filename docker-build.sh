#!/bin/bash

echo "Building APK using Docker..."
echo ""

# Build Docker image
docker build -t beep-counter-builder .

if [ $? -eq 0 ]; then
    echo ""
    echo "Docker build successful!"
    echo "Extracting APK from container..."
    
    # Create a temporary container and copy the APK
    CONTAINER_ID=$(docker create beep-counter-builder)
    docker cp $CONTAINER_ID:/app/app/build/outputs/apk/debug/app-debug.apk ./app-debug.apk
    docker rm $CONTAINER_ID
    
    echo ""
    echo "APK extracted to: ./app-debug.apk"
    echo "You can now install this APK on your Android device!"
else
    echo ""
    echo "Docker build failed. Please check the error messages above."
    exit 1
fi

