@echo off
echo Building APK using Docker...
echo.

docker build -t beep-counter-builder .

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Docker build successful!
    echo Extracting APK from container...
    
    for /f "tokens=*" %%i in ('docker create beep-counter-builder') do set CONTAINER_ID=%%i
    docker cp %CONTAINER_ID%:/app/app/build/outputs/apk/debug/app-debug.apk ./app-debug.apk
    docker rm %CONTAINER_ID%
    
    echo.
    echo APK extracted to: .\app-debug.apk
    echo You can now install this APK on your Android device!
) else (
    echo.
    echo Docker build failed. Please check the error messages above.
    pause
    exit /b 1
)

pause

