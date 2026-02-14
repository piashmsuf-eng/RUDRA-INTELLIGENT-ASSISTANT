package com.rudra.assistant.ai

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class FreedomGPTClient(
    private val baseUrl: String = "http://localhost:8889/v1",
    private val model: String = "Liberty"
) {
    companion object {
        private const val TAG = "FreedomGPTClient"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    suspend fun chat(message: String, conversationHistory: List<Map<String, String>> = emptyList()): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val messages = JSONArray()
                
                // System prompt
                messages.put(JSONObject().apply {
                    put("role", "system")
                    put("content", "You are RUDRA, a helpful AI assistant that speaks Bengali and English.")
                })
                
                // Add conversation history
                conversationHistory.forEach { msg ->
                    messages.put(JSONObject().apply {
                        put("role", msg["role"])
                        put("content", msg["content"])
                    })
                }
                
                // Add current message
                messages.put(JSONObject().apply {
                    put("role", "user")
                    put("content", message)
                })

                val requestBody = JSONObject().apply {
                    put("model", model)
                    put("messages", messages)
                    put("temperature", 0.7)
                    put("max_tokens", 500)
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$baseUrl/chat/completions")
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Request failed: ${response.code}")
                        return@withContext Result.failure(Exception("API error: ${response.code}"))
                    }

                    val responseBody = response.body?.string() ?: ""
                    val json = JSONObject(responseBody)
                    val content = json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")

                    Result.success(content)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Chat failed", e)
                Result.failure(e)
            }
        }
}
