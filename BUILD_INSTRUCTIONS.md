# Easy Build Instructions (No Android Studio Required)

## 🎯 Recommended: GitHub Actions (Easiest!)

### Step 1: Create GitHub Repository
1. Go to [github.com](https://github.com) and sign in
2. Click the "+" icon → "New repository"
3. Name it (e.g., "beep-counter")
4. Make it **Public** (required for free GitHub Actions)
5. Click "Create repository" 

### Step 2: Upload Files
**Option A: Using GitHub Web Interface**
1. In your new repository, click "uploading an existing file"
2. Drag and drop all files from this project folder
3. Click "Commit changes"

**Option B: Using Git (if you have it installed)**
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
git push -u origin main
```

### Step 3: Get Your APK
1. Go to the "Actions" tab in your GitHub repository
2. Wait for the build to complete (usually 2-3 minutes)
3. Click on the completed workflow run
4. Scroll down to "Artifacts"
5. Download `app-debug` - this is your APK!

**That's it!** You now have a built APK ready to install on your Android device.

---

## 🐳 Alternative: Docker Method

### Prerequisites
- Install Docker Desktop from [docker.com](https://www.docker.com/products/docker-desktop)

### Build Steps

**Windows:**
1. Open PowerShell or Command Prompt in this folder
2. Run: `docker-build.bat`
3. Wait for build to complete
4. Find `app-debug.apk` in the current folder

**Linux/Mac:**
1. Open terminal in this folder
2. Run:
   ```bash
   chmod +x docker-build.sh
   ./docker-build.sh
   ```
3. Wait for build to complete
4. Find `app-debug.apk` in the current folder

**Note:** First build will take longer as it downloads Android SDK (~1GB). Subsequent builds are faster.

---

## 📱 Installing the APK on Your Android Device

1. Transfer the `app-debug.apk` file to your Android device
2. On your device, go to Settings → Security
3. Enable "Install from Unknown Sources" or "Install Unknown Apps"
4. Open the APK file using a file manager
5. Tap "Install"
6. Open the app and grant microphone permission when prompted

---

## ❓ Troubleshooting

### GitHub Actions not working?
- Make sure your repository is **Public** (free accounts need public repos for Actions)
- Check the Actions tab for error messages
- Make sure all files were uploaded correctly

### Docker build fails?
- Make sure Docker is running
- Check you have enough disk space (~5GB free recommended)
- Try: `docker system prune` to clean up space

### APK won't install?
- Make sure "Install from Unknown Sources" is enabled
- Try a different file manager app
- Check that your device is Android 7.0 (API 24) or higher

---

## 🎉 That's All!

You don't need Android Studio at all. The GitHub Actions method is the easiest - just upload to GitHub and download your APK!

