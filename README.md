# Beep Counter Android App

An Android application that counts beeps using the device microphone. The app detects beeps in the frequency range of 1-4 kHz and displays the count in real-time.

## Features

- Real-time beep detection using device microphone
- Frequency analysis to detect beeps in the 1-4 kHz range
- Simple and intuitive UI with start/stop controls
- Live beep counter display

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 24 (Android 7.0) or higher
- Gradle 8.0 or later

## Building the APK

### 🚀 Easiest Method: GitHub Actions (No Installation Required!)

**This is the easiest way if you don't have Android Studio:**

1. Create a GitHub account (if you don't have one)
2. Create a new repository on GitHub
3. Upload all the project files to the repository
4. GitHub Actions will automatically build the APK
5. Go to the "Actions" tab in your repository
6. Download the APK from the artifacts section

The workflow is already configured in `.github/workflows/build-apk.yml` - just push to GitHub!

### 🐳 Alternative: Using Docker (If you have Docker installed)

**If you have Docker installed, this is very easy:**

**On Windows:**
```bash
docker-build.bat
```

**On Linux/Mac:**
```bash
chmod +x docker-build.sh
./docker-build.sh
```

This will automatically:
- Download Android SDK
- Build the APK
- Extract it to `app-debug.apk` in the current directory

### Using Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to this directory and select it
4. Wait for Gradle sync to complete
5. Go to `Build > Build Bundle(s) / APK(s) > Build APK(s)`
6. The APK will be generated in `app/build/outputs/apk/debug/app-debug.apk`

### Using Command Line (Requires Android SDK)

1. Make sure you have Android SDK and Gradle installed
2. Navigate to the project directory
3. Run:
   ```bash
   ./gradlew assembleDebug
   ```
   (On Windows: `gradlew.bat assembleDebug`)
4. The APK will be generated in `app/build/outputs/apk/debug/app-debug.apk`

### For Release APK

To build a release APK, you'll need to:
1. Create a keystore file
2. Configure signing in `app/build.gradle`
3. Run: `./gradlew assembleRelease`

## Permissions

The app requires the following permission:
- `RECORD_AUDIO` - To access the device microphone for beep detection

The app will request this permission when you first tap "Start Listening".

## How It Works

1. The app uses Android's `AudioRecord` API to capture audio from the microphone
2. Audio samples are analyzed in real-time to detect beeps
3. Beep detection uses:
   - Amplitude threshold to detect sound above background noise
   - Frequency analysis to identify beeps in the 1-4 kHz range
   - Duration filtering to distinguish beeps from noise
4. Each detected beep increments the counter

## Usage

1. Launch the app
2. Grant microphone permission when prompted
3. Tap "Start Listening" to begin beep detection
4. The counter will increment each time a beep is detected
5. Tap "Stop Listening" to pause detection

## Technical Details

- **Sample Rate**: 44.1 kHz
- **Audio Format**: 16-bit PCM
- **Beep Frequency Range**: 1-4 kHz
- **Minimum Beep Duration**: 50ms
- **Maximum Beep Gap**: 200ms (to count as separate beeps)

## Troubleshooting

- If beeps are not detected, try adjusting the volume of the beep source
- Make sure the microphone is not blocked
- The app works best with clear, distinct beeps in the 1-4 kHz range
- Background noise may affect detection accuracy

