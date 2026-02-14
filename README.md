# RUDRA - INTELLIGENT ASSISTANT

> 🤖 Advanced Android AI Assistant with 2000+ skills, 1000+ tools, and customizable AI backends

An intelligent voice-controlled assistant that combines the power of Letta AI and FreedomGPT with extensive automation capabilities, Bengali/English voice support, and beautiful Material 3 UI themes.

---

## ✨ Features

### 🤖 **Dual AI Integration**
- **Letta AI**: Cloud-based, powerful language model
- **FreedomGPT**: Self-hosted, privacy-focused alternative
- Switch between providers instantly via Settings

### 🎙️ **Voice Control**
- **Bangla/Banglish Support**: Mixed language commands
- **Continuous Listening**: Hands-free operation
- **Dual TTS Options**:
  - Android TTS (Free, Offline)
  - Cartesia TTS (Premium, Natural Voice)

### 🚀 **2000 Skills**
- 500+ App launchers (WhatsApp, YouTube, Bkash, etc.)
- 300+ Communication actions (SMS, calls)
- 200+ System commands (WiFi, Bluetooth, volume)
- 1000+ misc skills

### 🛠️ **1000 Tools**
- System control (volume, brightness, etc.)
- App management (launch, close, clear cache)
- Media playback control
- File operations (read, write, delete)
- Network requests (HTTP GET/POST)
- Location & navigation
- And much more!

### 🎨 **4 Custom Themes**
1. **Dark Red** - Classic hacker aesthetic
2. **Cyberpunk** - Cyan & Magenta future vibes
3. **Matrix Green** - Terminal-style interface
4. **Purple Haze** - Mystical purple tones

### ⚙️ **Settings Panel**
- Configure AI provider & API keys
- Toggle voice assistant on/off
- Choose TTS provider
- Customize app theme
- All saved locally & securely

---

## 🚀 Quick Start

### Option 1: Download Pre-built APK (Recommended)

1. Go to [GitHub Actions](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT/actions)
2. Click latest workflow run
3. Download **RUDRA-Debug-APK** from Artifacts
4. Install on Android device (min SDK 26, Android 8.0+)
5. Open app → Settings → Configure API keys

### Option 2: Build from Source

See **[BUILD.md](BUILD.md)** for detailed build instructions.

**Quick build:**
```bash
git clone https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT.git
cd RUDRA-INTELLIGENT-ASSISTANT
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

---

## ⚙️ Configuration

### 1️⃣ Get API Keys

**Letta AI** (Recommended for cloud AI)
1. Visit [https://letta.com](https://letta.com)
2. Sign up → Dashboard → Generate API Key
3. Copy key (format: `letta_xxx...`)

**Cartesia TTS** (Optional - Premium Voice)
1. Visit [https://cartesia.ai](https://cartesia.ai)
2. Sign up → Generate API Key
3. Copy key (format: `cartesia_xxx...`)

**FreedomGPT** (Optional - Local AI)
1. Install FreedomGPT on PC
2. Run: `freedomgpt --port 8889`
3. Use URL: `http://YOUR_IP:8889/v1`

### 2️⃣ Configure in App

1. Open RUDRA app
2. Tap **Settings** icon (⚙️ top-right)
3. Enter API keys:
   - **AI Provider**: Select Letta or FreedomGPT
   - **Letta API Key**: Paste your key
   - **Cartesia API Key**: (Optional) Paste for premium TTS
4. Settings auto-save!

See **[SETUP_GUIDE.md](SETUP_GUIDE.md)** for detailed setup instructions.

---

## 📱 Usage

### Voice Commands

Tap **"TAP TO SPEAK"** and say:

**Opening Apps:**
- "Open WhatsApp"
- "WhatsApp khulo"
- "Launch Bkash"

**System Control:**
- "Turn on WiFi"
- "Set volume to 50%"
- "Increase brightness"

**AI Chat:**
- "What's the weather?"
- "Tell me a joke"
- "Translate this to Bengali"

**Communication:**
- "Call Mom"
- "Send SMS to +8801234567890"

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Networking**: OkHttp + Retrofit
- **AI Integration**: Letta AI & FreedomGPT APIs
- **Voice**: Android SpeechRecognizer + TTS / Cartesia
- **Build**: Gradle 8.2, Android SDK 34
- **CI/CD**: GitHub Actions

---

## 📂 Project Structure

```
RUDRA-INTELLIGENT-ASSISTANT/
├── app/
│   ├── src/main/
│   │   ├── java/com/rudra/assistant/
│   │   │   ├── MainActivity.kt          # Main UI + Voice handling
│   │   │   ├── RudraApp.kt              # Application class
│   │   │   ├── ai/                      # AI client implementations
│   │   │   │   ├── LettaAIClient.kt
│   │   │   │   ├── FreedomGPTClient.kt
│   │   │   │   └── AIManager.kt
│   │   │   ├── voice/                   # Voice & TTS
│   │   │   │   └── CartesiaTTS.kt
│   │   │   ├── skills/                  # 2000 skills system
│   │   │   │   └── SkillManager.kt
│   │   │   ├── tools/                   # 1000 tools system
│   │   │   │   └── ToolRegistry.kt
│   │   │   ├── downloader/              # Theme/icon downloaders
│   │   │   └── ui/screens/              # UI screens
│   │   │       └── SettingsScreen.kt
│   │   ├── assets/
│   │   │   ├── skills.json              # 2000 skills (509KB)
│   │   │   └── tools.json               # 1000 tools
│   │   └── res/                         # Resources
│   └── build.gradle.kts
├── .github/workflows/
│   └── android.yml                      # CI/CD pipeline
├── BUILD.md                             # Build instructions
├── SETUP_GUIDE.md                       # Setup & configuration guide
└── README.md                            # This file
```

---

## 🔒 Permissions

The app requires the following permissions:

- **Microphone** - Voice input
- **Internet** - AI API calls
- **SMS** - Send messages via voice
- **Phone** - Make calls via voice
- **Contacts** - Access contacts for calling/messaging
- **System Alert Window** - Overlay features
- **Query All Packages** - Detect installed apps

All permissions are requested at runtime and can be revoked anytime.

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open source and available under the MIT License.

---

## 🆘 Support & Documentation

- **Build Guide**: [BUILD.md](BUILD.md)
- **Setup Guide**: [SETUP_GUIDE.md](SETUP_GUIDE.md)
- **Issues**: [GitHub Issues](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT/issues)
- **Repository**: [GitHub](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT)

---

## 🎯 Roadmap

- [ ] Add more skills (target: 5000+)
- [ ] Implement smart home integration
- [ ] Add widget support
- [ ] Offline AI mode
- [ ] Custom skill creation UI
- [ ] Multi-language expansion

---

**Built with ❤️ | Powered by Letta AI & FreedomGPT**
