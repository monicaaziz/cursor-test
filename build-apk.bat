@echo off
echo Building Beep Counter APK...
echo.

if not exist "gradlew.bat" (
    echo Error: gradlew.bat not found. Please use Android Studio to build the project.
    pause
    exit /b 1
)

call gradlew.bat assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo APK location: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo.
    echo Build failed. Please check the error messages above.
)

pause

