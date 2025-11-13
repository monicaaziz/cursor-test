#!/bin/bash

echo "Building Beep Counter APK..."
echo ""

if [ ! -f "gradlew" ]; then
    echo "Error: gradlew not found. Please use Android Studio to build the project."
    exit 1
fi

chmod +x gradlew
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful!"
    echo "APK location: app/build/outputs/apk/debug/app-debug.apk"
else
    echo ""
    echo "Build failed. Please check the error messages above."
    exit 1
fi

