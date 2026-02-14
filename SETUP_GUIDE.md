# RUDRA AI Assistant - Setup Guide

## 🚀 Quick Start

1. **Download APK** from [GitHub Actions](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT/actions)
2. **Install** on your Android device (min SDK 26, Android 8.0+)
3. **Open app** and grant permissions (Microphone, Contacts, SMS, Phone)
4. **Tap Settings icon** (⚙️ top-right corner)

---

## ⚙️ Settings Configuration

### 1. AI Provider Setup

#### Option A: Letta AI (Recommended)
1. Go to [https://letta.com](https://letta.com)
2. Sign up and get your API key
3. In RUDRA Settings:
   - Select **"Letta AI"**
   - Enter your API key in **"Letta API Key"** field
   - Tap the eye icon to show/hide key

#### Option B: FreedomGPT (Local/Self-hosted)
1. Install FreedomGPT on your PC/server
2. Run: `freedomgpt --port 8889`
3. In RUDRA Settings:
   - Select **"FreedomGPT"**
   - Enter URL: `http://YOUR_PC_IP:8889/v1`
   - Example: `http://192.168.1.100:8889/v1`

---

### 2. Voice Settings

#### Enable/Disable Voice Assistant
- Toggle **"Enable Voice Assistant"** switch
- When OFF: AI will only respond via text
- When ON: Voice recognition + TTS active

#### TTS Provider

**Option A: Android TTS (Default - Free)**
- Uses your device's built-in Text-to-Speech
- Works offline
- Supports multiple languages if installed

**Option B: Cartesia (Premium - Better Quality)**
1. Go to [https://cartesia.ai](https://cartesia.ai)
2. Sign up and get API key
3. In RUDRA Settings:
   - Select **"Cartesia"**
   - Enter your **"Cartesia API Key"**
   - Tap eye icon to show/hide

**Cartesia Features:**
- High-quality, natural-sounding voice
- Low latency
- Multiple voice options
- Expressive speech

---

### 3. Theme Customization

Choose from 4 themes:

1. **Dark Red (Default)** 🔴
   - Black background
   - Red accents
   - Green text

2. **Cyberpunk** 💙
   - Black background
   - Cyan primary
   - Magenta highlights

3. **Matrix Green** 💚
   - Black background
   - All green terminal style
   - True hacker aesthetic

4. **Purple Haze** 💜
   - Dark purple background
   - Magenta primary
   - Cyan accents

**To change theme:**
- Tap any theme option
- Changes apply instantly
- Persists across app restarts

---

## 🎤 Using Voice Commands

### Basic Usage
1. Tap **"TAP TO SPEAK"** button
2. Speak your command in English or Bangla
3. Wait for response
4. App will automatically listen again (continuous mode)

### Example Commands

**Opening Apps:**
- "Open WhatsApp"
- "WhatsApp khulo"
- "Launch YouTube"
- "Bkash open koro"

**System Control:**
- "Turn on WiFi"
- "Disable Bluetooth"
- "Increase volume"
- "Set brightness to 50%"

**Communication:**
- "Call John"
- "Send SMS to +8801234567890"
- "Message Mom saying I'm coming home"

**AI Chat:**
- "What's the weather like?"
- "Tell me a joke"
- "Explain quantum physics"
- "Translate this to Bengali"

---

## 📱 Skills & Tools

### 2000 Skills Available
Check `skills.json` for complete list:
- 500+ App launchers
- 300+ Communication actions
- 200+ System commands
- 1000+ misc skills

### 1000 Tools Available
Check `tools.json` for complete list:
- System control (volume, brightness, etc.)
- App management
- Media playback
- File operations
- Network requests
- And much more!

---

## 🔧 Troubleshooting

### Voice Not Working?
1. Check microphone permission
2. Verify "Enable Voice Assistant" is ON
3. Test device microphone in other apps
4. Try restarting app

### AI Not Responding?
1. Check internet connection
2. Verify API key is correct
3. For Letta: Check API quota/billing
4. For FreedomGPT: Ensure server is running

### Cartesia TTS Silent?
1. Verify API key is correct
2. Check internet connection
3. Fallback: Switch to Android TTS

### Theme Not Changing?
1. Ensure you tapped "Back" to apply
2. Restart app if needed
3. Check that selection is saved

---

## 💡 Tips

1. **Save API Keys**: Settings are persisted locally
2. **Continuous Listening**: App auto-restarts listening after each command
3. **Stop Listening**: Tap "STOP" button
4. **Mix Languages**: Works with English + Bangla commands
5. **Custom Themes**: More themes coming soon!

---

## 🆘 Support

- **Issues**: [GitHub Issues](https://github.com/piashmsuf-eng/RUDRA-INTELLIGENT-ASSISTANT/issues)
- **Docs**: [README.md](README.md)

---

**Enjoy your AI assistant!** 🤖✨
