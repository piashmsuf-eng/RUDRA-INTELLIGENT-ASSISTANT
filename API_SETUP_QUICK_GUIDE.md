# ⚡ API Setup - Quick Reference

RUDRA app e API keys configure korar **shortest guide**.

---

## 🎯 Required (Choose One)

### Option 1: Letta AI (Cloud) - Recommended ✅

1. **Visit**: [https://letta.com](https://letta.com)
2. **Sign Up**: Free account banao
3. **Get Key**: Dashboard → API Keys → Generate
4. **Copy**: `letta_xxxxxxxxxxxxxxxx`
5. **Paste in App**: Settings → Letta API Key

**Cost**: Free tier available

---

### Option 2: FreedomGPT (Local) - Privacy 🔒

1. **Download**: [https://freedomgpt.com](https://freedomgpt.com)
2. **Install** on your PC/laptop
3. **Run**: 
   ```bash
   freedomgpt --port 8889
   ```
4. **Get IP**: 
   - Windows: `ipconfig`
   - Mac/Linux: `ifconfig`
5. **Use URL**: `http://192.168.1.xxx:8889/v1`
6. **Paste in App**: Settings → FreedomGPT URL

**Cost**: Free, runs locally

---

## 🔊 Optional: Better Voice (Cartesia TTS)

Default TTS = Android built-in (free, works offline)  
Premium TTS = Cartesia (natural voice, needs internet)

### Get Cartesia Key:

1. **Visit**: [https://cartesia.ai](https://cartesia.ai)
2. **Sign Up**: Create account
3. **Get Key**: Dashboard → API section → Generate
4. **Copy**: `cartesia_xxxxxxxxxxxxxxxx`
5. **Paste in App**: Settings → TTS Provider → Cartesia → API Key

**Cost**: Pay per use (check pricing)

---

## 📝 In-App Configuration

### Method 1: Settings Screen (Easy) ✅

1. Open RUDRA app
2. Tap **⚙️ Settings** (top-right)
3. Enter keys:
   ```
   AI Provider: [Letta] or [FreedomGPT]
   Letta API Key: letta_xxx...
   Cartesia API Key: cartesia_xxx... (optional)
   ```
4. Done! Keys auto-save

### Method 2: Code Edit (Advanced)

Edit: `app/src/main/java/com/rudra/assistant/MainActivity.kt`

Line ~43:
```kotlin
val lettaKey = "letta_your_key_here"
val freedomUrl = "http://192.168.1.100:8889/v1"
```

Rebuild app:
```bash
./gradlew assembleDebug
```

---

## ✅ Verify Setup

1. **Open App**
2. **Tap "TAP TO SPEAK"**
3. **Say**: "Hello"
4. **Check**: Should respond via AI
5. **Success!** ✅

---

## 🆘 Troubleshooting

### AI not responding?
- ✅ Check internet (for Letta/Cartesia)
- ✅ Verify API key (no spaces)
- ✅ For FreedomGPT: PC running?
- ✅ Check Settings → AI Provider selected

### Voice not working?
- ✅ Grant Microphone permission
- ✅ Settings → Enable Voice Assistant = ON
- ✅ Test mic in other apps

### Cartesia TTS silent?
- ✅ Check internet
- ✅ Verify API key
- ✅ Fallback: Use Android TTS

---

## 💰 Cost Summary

| Service | Type | Cost |
|---------|------|------|
| **Letta AI** | Cloud | Free tier → Paid plans |
| **FreedomGPT** | Local | Free (self-hosted) |
| **Android TTS** | Device | Free (built-in) |
| **Cartesia TTS** | Cloud | Pay per use |

---

## 🔗 Quick Links

- **Letta**: [https://letta.com](https://letta.com)
- **Cartesia**: [https://cartesia.ai](https://cartesia.ai)
- **FreedomGPT**: [https://freedomgpt.com](https://freedomgpt.com)
- **Full Guide**: [SETUP_GUIDE.md](SETUP_GUIDE.md)
- **Build Guide**: [BUILD.md](BUILD.md)

---

**That's it! Enjoy RUDRA! 🚀**
