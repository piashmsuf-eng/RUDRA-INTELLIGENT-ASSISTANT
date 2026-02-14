# 🔨 Build Guide - RUDRA AI Assistant

Source code theke app build korar complete guide.

---

## 📋 Prerequisites

### 1. Install Required Software

#### **Java Development Kit (JDK 17)**
- Download: [https://adoptium.net/](https://adoptium.net/)
- Install JDK 17 or higher
- Verify: `java -version` (should show 17+)

#### **Android Studio**
- Download: [https://developer.android.com/studio](https://developer.android.com/studio)
- Install latest version (Hedgehog or newer)
- During setup, install Android SDK

#### **Git**
- Download: [https://git-scm.com/](https://git-scm.com/)
- Verify: `git --version`

---

## 🚀 Build Steps

### Step 1: Clone Repository

```bash
git clone https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT.git
cd RUDRA-INTELLIGENT-ASSISTANT
```

### Step 2: Setup Android SDK Path

**Option A: Using Android Studio (Recommended)**
1. Open Android Studio
2. Click "Open" and select `RUDRA-INTELLIGENT-ASSISTANT` folder
3. Android Studio will automatically create `local.properties`
4. Wait for Gradle sync to complete

**Option B: Manual Setup**
1. Copy the example file:
   ```bash
   cp local.properties.example local.properties
   ```

2. Edit `local.properties` and set your SDK path:
   
   **Windows:**
   ```properties
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   ```
   
   **Mac:**
   ```properties
   sdk.dir=/Users/YourUsername/Library/Android/sdk
   ```
   
   **Linux:**
   ```properties
   sdk.dir=/home/YourUsername/Android/Sdk
   ```

### Step 3: Build APK

**Using Android Studio:**
1. Open project
2. Wait for Gradle sync
3. Go to: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
4. APK location: `app/build/outputs/apk/debug/app-debug.apk`

**Using Command Line:**

**Windows:**
```bash
gradlew.bat assembleDebug
```

**Mac/Linux:**
```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

---

## ⚙️ API Configuration

Build korar **age** ba **pore** Settings screen diye API keys configure korben.

### Option 1: Build Time Configuration (Code Edit)

Edit: `app/src/main/java/com/rudra/assistant/MainActivity.kt`

Line 43-48 change korun:

```kotlin
// Initialize AI Manager with saved API keys
val lettaKey = "your_letta_api_key_here"  // ← Your Letta key
val freedomUrl = "http://192.168.1.100:8889/v1"  // ← Your FreedomGPT URL
val aiProvider = "letta"  // or "freedomgpt"

aiManager = AIManager(
    lettaApiKey = lettaKey,
    freedomGptUrl = freedomUrl,
    preferredProvider = if (aiProvider == "letta") AIManager.AIProvider.LETTA else AIManager.AIProvider.FREEDOMGPT
)
```

**Then rebuild:**
```bash
./gradlew assembleDebug
```

### Option 2: Runtime Configuration (App Settings)

1. Install APK without editing code
2. Open RUDRA app
3. Tap **Settings** icon (⚙️ top-right)
4. Enter API keys in Settings screen:
   - **Letta API Key**: Get from [https://letta.com](https://letta.com)
   - **Cartesia API Key** (optional): Get from [https://cartesia.ai](https://cartesia.ai)
   - **FreedomGPT URL**: `http://YOUR_IP:8889/v1`
5. Keys are saved in SharedPreferences

---

## 🔑 Getting API Keys

### Letta AI (Recommended)
1. Visit: [https://letta.com](https://letta.com)
2. Sign up for free account
3. Go to Dashboard → API Keys
4. Generate new key (format: `letta_xxx...`)
5. Copy and paste in RUDRA Settings

### Cartesia TTS (Optional - Premium Voice)
1. Visit: [https://cartesia.ai](https://cartesia.ai)
2. Sign up for account
3. Go to API section
4. Generate API key (format: `cartesia_xxx...`)
5. Copy and paste in RUDRA Settings → TTS Provider → Cartesia

### FreedomGPT (Local/Self-hosted)
1. Install FreedomGPT on PC/Server
2. Run: `freedomgpt --port 8889`
3. Find your PC's IP address:
   - Windows: `ipconfig`
   - Mac/Linux: `ifconfig`
4. Use URL: `http://YOUR_IP:8889/v1`
   - Example: `http://192.168.1.100:8889/v1`

---

## 🐛 Troubleshooting

### Build Errors

**Error: "SDK location not found"**
- **Fix**: Create `local.properties` with correct SDK path (see Step 2)

**Error: "Gradle sync failed"**
- **Fix**: Check internet connection, try: `./gradlew --refresh-dependencies`

**Error: "Java version mismatch"**
- **Fix**: Install JDK 17, set `JAVA_HOME` environment variable

**Error: "Permission denied: ./gradlew"**
- **Fix** (Linux/Mac): `chmod +x gradlew`

### Runtime Errors

**"API key invalid" / AI not responding**
- Check API key is correct (no spaces)
- Verify internet connection
- For Letta: Check account has API credits
- For FreedomGPT: Ensure server is running

**Voice not working**
- Grant microphone permission
- Enable "Voice Assistant" in Settings
- Test mic in other apps

**Cartesia TTS silent**
- Check API key is correct
- Fallback to Android TTS if needed
- Verify internet connection

---

## 📱 Installation

### Install on Device

**Via USB (ADB):**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Via File Transfer:**
1. Copy APK to phone
2. Open file manager
3. Tap APK → Install
4. Enable "Install from Unknown Sources" if prompted

---

## 🧪 Testing

### Test Voice Commands
1. Tap "TAP TO SPEAK"
2. Try: "Open WhatsApp" or "WhatsApp khulo"
3. Check app opens

### Test AI Chat
1. Say: "What's the weather?"
2. Wait for AI response
3. Should hear TTS reply

### Test Settings
1. Open Settings (⚙️)
2. Change theme → verify colors change
3. Toggle voice → verify switch works
4. Enter API keys → verify they save

---

## 📦 Release Build

For production/release APK:

```bash
./gradlew assembleRelease
```

**Note**: Requires signing key setup in `app/build.gradle.kts`

---

## 🆘 Need Help?

- **Issues**: [GitHub Issues](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT/issues)
- **Setup Guide**: [SETUP_GUIDE.md](SETUP_GUIDE.md)
- **Source Code**: [GitHub Repository](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT)

---

**Happy Building!** 🚀
