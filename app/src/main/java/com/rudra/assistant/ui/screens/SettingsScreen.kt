package com.rudra.assistant.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    context: Context,
    onBack: () -> Unit
) {
    val prefs = remember { context.getSharedPreferences("rudra_prefs", Context.MODE_PRIVATE) }
    
    var lettaApiKey by remember { mutableStateOf(prefs.getString("letta_api_key", "") ?: "") }
    var freedomGptUrl by remember { mutableStateOf(prefs.getString("freedomgpt_url", "http://localhost:8889/v1") ?: "") }
    var cartesiaApiKey by remember { mutableStateOf(prefs.getString("cartesia_api_key", "") ?: "") }
    var aiProvider by remember { mutableStateOf(prefs.getString("ai_provider", "letta") ?: "letta") }
    var voiceEnabled by remember { mutableStateOf(prefs.getBoolean("voice_enabled", true)) }
    var ttsProvider by remember { mutableStateOf(prefs.getString("tts_provider", "android") ?: "android") }
    var theme by remember { mutableStateOf(prefs.getString("theme", "dark_red") ?: "dark_red") }
    
    var showLettaKey by remember { mutableStateOf(false) }
    var showCartesiaKey by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.Red)
            }
            Text(
                text = "SETTINGS",
                color = Color.Red,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // AI Provider Section
        Text("AI PROVIDER", color = Color.Green, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            FilterChip(
                selected = aiProvider == "letta",
                onClick = {
                    aiProvider = "letta"
                    prefs.edit().putString("ai_provider", "letta").apply()
                },
                label = { Text("Letta AI") },
                modifier = Modifier.padding(end = 8.dp)
            )
            FilterChip(
                selected = aiProvider == "freedomgpt",
                onClick = {
                    aiProvider = "freedomgpt"
                    prefs.edit().putString("ai_provider", "freedomgpt").apply()
                },
                label = { Text("FreedomGPT") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Letta AI Config
        if (aiProvider == "letta") {
            OutlinedTextField(
                value = lettaApiKey,
                onValueChange = {
                    lettaApiKey = it
                    prefs.edit().putString("letta_api_key", it).apply()
                },
                label = { Text("Letta API Key", color = Color.Green) },
                placeholder = { Text("letta_xxx...") },
                visualTransformation = if (showLettaKey) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showLettaKey = !showLettaKey }) {
                        Icon(
                            if (showLettaKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            "Toggle visibility",
                            tint = Color.Green
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Red,
                    unfocusedBorderColor = Color.Green,
                    focusedTextColor = Color.Green,
                    unfocusedTextColor = Color.Green
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // FreedomGPT Config
        if (aiProvider == "freedomgpt") {
            OutlinedTextField(
                value = freedomGptUrl,
                onValueChange = {
                    freedomGptUrl = it
                    prefs.edit().putString("freedomgpt_url", it).apply()
                },
                label = { Text("FreedomGPT URL", color = Color.Green) },
                placeholder = { Text("http://localhost:8889/v1") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Red,
                    unfocusedBorderColor = Color.Green,
                    focusedTextColor = Color.Green,
                    unfocusedTextColor = Color.Green
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Voice Settings
        Text("VOICE SETTINGS", color = Color.Green, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    voiceEnabled = !voiceEnabled
                    prefs.edit().putBoolean("voice_enabled", voiceEnabled).apply()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = voiceEnabled,
                onCheckedChange = {
                    voiceEnabled = it
                    prefs.edit().putBoolean("voice_enabled", it).apply()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Red,
                    checkedTrackColor = Color.DarkGray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Enable Voice Assistant", color = Color.Green)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TTS Provider
        Text("TTS PROVIDER", color = Color.Green, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            FilterChip(
                selected = ttsProvider == "android",
                onClick = {
                    ttsProvider = "android"
                    prefs.edit().putString("tts_provider", "android").apply()
                },
                label = { Text("Android TTS") },
                modifier = Modifier.padding(end = 8.dp)
            )
            FilterChip(
                selected = ttsProvider == "cartesia",
                onClick = {
                    ttsProvider = "cartesia"
                    prefs.edit().putString("tts_provider", "cartesia").apply()
                },
                label = { Text("Cartesia") }
            )
        }

        // Cartesia API Key
        if (ttsProvider == "cartesia") {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cartesiaApiKey,
                onValueChange = {
                    cartesiaApiKey = it
                    prefs.edit().putString("cartesia_api_key", it).apply()
                },
                label = { Text("Cartesia API Key", color = Color.Green) },
                placeholder = { Text("cartesia_xxx...") },
                visualTransformation = if (showCartesiaKey) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showCartesiaKey = !showCartesiaKey }) {
                        Icon(
                            if (showCartesiaKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            "Toggle visibility",
                            tint = Color.Green
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Red,
                    unfocusedBorderColor = Color.Green,
                    focusedTextColor = Color.Green,
                    unfocusedTextColor = Color.Green
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Theme Settings
        Text("THEME", color = Color.Green, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        
        Column {
            ThemeOption("Dark Red (Default)", "dark_red", theme, Color.Red) {
                theme = "dark_red"
                prefs.edit().putString("theme", "dark_red").apply()
            }
            ThemeOption("Cyberpunk", "cyberpunk", theme, Color.Cyan) {
                theme = "cyberpunk"
                prefs.edit().putString("theme", "cyberpunk").apply()
            }
            ThemeOption("Matrix Green", "matrix", theme, Color.Green) {
                theme = "matrix"
                prefs.edit().putString("theme", "matrix").apply()
            }
            ThemeOption("Purple Haze", "purple", theme, Color.Magenta) {
                theme = "purple"
                prefs.edit().putString("theme", "purple").apply()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Section
        Text("ABOUT", color = Color.Green, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("RUDRA Intelligent Assistant", color = Color.Gray, fontSize = 14.sp)
        Text("Version 1.0.0", color = Color.Gray, fontSize = 12.sp)
        Text("2000 Skills | 1000 Tools", color = Color.Gray, fontSize = 12.sp)
        Text("Powered by Letta AI & FreedomGPT", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun ThemeOption(
    label: String,
    value: String,
    currentTheme: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentTheme == value,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = accentColor,
                unselectedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = if (currentTheme == value) accentColor else Color.Gray)
    }
}
