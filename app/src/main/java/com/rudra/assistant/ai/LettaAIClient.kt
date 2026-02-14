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

class LettaAIClient(
    private val apiKey: String,
    private val baseUrl: String = "https://api.letta.com/v1"
) {
    companion object {
        private const val TAG = "LettaAIClient"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun chat(message: String, conversationHistory: List<Map<String, String>> = emptyList()): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val messages = JSONArray()
                
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
                    put("messages", messages)
                    put("model", "gpt-4")
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$baseUrl/chat/completions")
                    .header("Authorization", "Bearer $apiKey")
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
