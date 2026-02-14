package com.rudra.assistant

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.rudra.assistant.ai.AIManager
import com.rudra.assistant.skills.SkillManager
import com.rudra.assistant.tools.ToolRegistry
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var aiManager: AIManager
    private var speechRecognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private var isListening by mutableStateOf(false)
    private var statusText by mutableStateOf("Ready")

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            initializeVoice()
        } else {
            Toast.makeText(this, "Permissions required", Toast.LENGTH_SHORT).show()
        }
    }

    private var showSettings by mutableStateOf(false)
    private lateinit var cartesiaTTS: com.rudra.assistant.voice.CartesiaTTS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("rudra_prefs", Context.MODE_PRIVATE)
        
        // Initialize AI Manager with saved API keys
        val lettaKey = prefs.getString("letta_api_key", null)
        val freedomUrl = prefs.getString("freedomgpt_url", "http://localhost:8889/v1")
        val aiProvider = prefs.getString("ai_provider", "letta") ?: "letta"
        
        aiManager = AIManager(
            lettaApiKey = lettaKey,
            freedomGptUrl = freedomUrl,
            preferredProvider = if (aiProvider == "letta") AIManager.AIProvider.LETTA else AIManager.AIProvider.FREEDOMGPT
        )

        // Initialize Cartesia TTS if API key exists
        val cartesiaKey = prefs.getString("cartesia_api_key", null)
        if (cartesiaKey != null) {
            cartesiaTTS = com.rudra.assistant.voice.CartesiaTTS(cartesiaKey, this)
        }

        setContent {
            val currentTheme = prefs.getString("theme", "dark_red") ?: "dark_red"
            
            RudraTheme(theme = currentTheme) {
                if (showSettings) {
                    com.rudra.assistant.ui.screens.SettingsScreen(
                        context = this,
                        onBack = { showSettings = false }
                    )
                } else {
                    MainScreen(
                        isListening = isListening,
                        statusText = statusText,
                        onStartListening = { checkPermissionsAndStart() },
                        onStopListening = { stopListening() },
                        onOpenSettings = { showSettings = true }
                    )
                }
            }
        }
    }

    private fun checkPermissionsAndStart() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS
        )
        
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            initializeVoice()
            startListening()
        } else {
            requestPermissions.launch(permissions)
        }
    }

    private fun initializeVoice() {
        if (tts == null) {
            tts = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.ENGLISH
                }
            }
        }

        if (speechRecognizer == null && SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        statusText = "Listening..."
                    }
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        isListening = false
                        statusText = "Processing..."
                    }
                    override fun onError(error: Int) {
                        isListening = false
                        statusText = "Error"
                        if (isListening) startListening()
                    }
                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        matches?.firstOrNull()?.let { processCommand(it) }
                        if (isListening) startListening()
                    }
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        }
    }

    private fun startListening() {
        if (!isListening) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            }
            speechRecognizer?.startListening(intent)
            isListening = true
            statusText = "Listening..."
        }
    }

    private fun stopListening() {
        speechRecognizer?.stopListening()
        isListening = false
        statusText = "Ready"
    }

    private fun processCommand(command: String) {
        statusText = "Processing: $command"
        Log.d("RUDRA", "Command: $command")

        // Check for skill match
        val skill = SkillManager.findSkill(command)
        if (skill != null) {
            executeSkill(skill, command)
            return
        }

        // Fallback to AI chat
        kotlinx.coroutines.GlobalScope.launch {
            val response = aiManager.chat(command)
            response.getOrNull()?.let { text ->
                speak(text)
                statusText = "Response: ${text.take(50)}..."
            } ?: run {
                speak("Sorry, I couldn't process that.")
                statusText = "Error"
            }
        }
    }

    private fun executeSkill(skill: SkillManager.Skill, command: String) {
        when (skill.action) {
            "open_app" -> {
                val intent = packageManager.getLaunchIntentForPackage(skill.target)
                if (intent != null) {
                    startActivity(intent)
                    speak("Opening ${skill.id.replace("_", " ")}")
                } else {
                    speak("App not found")
                }
            }
            "deep_link" -> {
                val intent = Intent(Intent.ACTION_VIEW).setData(android.net.Uri.parse(skill.target))
                startActivity(intent)
                speak("Done")
            }
            else -> speak("Action not implemented")
        }
        statusText = "Executed: ${skill.id}"
    }

    private fun speak(text: String) {
        val prefs = getSharedPreferences("rudra_prefs", Context.MODE_PRIVATE)
        val ttsProvider = prefs.getString("tts_provider", "android") ?: "android"
        
        when (ttsProvider) {
            "cartesia" -> {
                if (::cartesiaTTS.isInitialized) {
                    kotlinx.coroutines.GlobalScope.launch {
                        cartesiaTTS.speak(text)
                    }
                } else {
                    // Fallback to Android TTS
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
            else -> {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
        tts?.shutdown()
    }
}

@Composable
fun RudraTheme(theme: String = "dark_red", content: @Composable () -> Unit) {
    val colorScheme = when (theme) {
        "cyberpunk" -> darkColorScheme(
            primary = Color.Cyan,
            background = Color.Black,
            surface = Color(0xFF1A1A1A),
            onPrimary = Color.Black,
            onBackground = Color.Cyan,
            onSurface = Color.Magenta
        )
        "matrix" -> darkColorScheme(
            primary = Color.Green,
            background = Color.Black,
            surface = Color(0xFF001100),
            onPrimary = Color.Black,
            onBackground = Color.Green,
            onSurface = Color.Green
        )
        "purple" -> darkColorScheme(
            primary = Color.Magenta,
            background = Color(0xFF1A001A),
            surface = Color(0xFF2A002A),
            onPrimary = Color.White,
            onBackground = Color.Magenta,
            onSurface = Color.Cyan
        )
        else -> darkColorScheme( // "dark_red"
            primary = Color.Red,
            background = Color.Black,
            surface = Color(0xFF1A1A1A),
            onPrimary = Color.White,
            onBackground = Color.Green,
            onSurface = Color.Green
        )
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun MainScreen(
    isListening: Boolean,
    statusText: String,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onOpenSettings: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Settings button (top-right)
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RUDRA",
                color = Color.Red,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "INTELLIGENT ASSISTANT",
            color = Color.Green,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Breathing circle
        Surface(
            modifier = Modifier
                .size(120.dp)
                .scale(if (isListening) scale else 1f),
            shape = CircleShape,
            color = if (isListening) Color.Red else Color.DarkGray
        ) {}

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = statusText,
            color = Color.Green,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { if (isListening) onStopListening() else onStartListening() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(if (isListening) "STOP" else "TAP TO SPEAK")
        }
        }
    }
}
