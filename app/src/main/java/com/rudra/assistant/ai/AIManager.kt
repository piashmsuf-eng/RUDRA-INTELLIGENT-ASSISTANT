package com.rudra.assistant.ai

import android.util.Log

class AIManager(
    private val lettaApiKey: String? = null,
    private val freedomGptUrl: String? = null,
    private val preferredProvider: AIProvider = AIProvider.LETTA
) {
    companion object {
        private const val TAG = "AIManager"
    }

    enum class AIProvider {
        LETTA,
        FREEDOMGPT
    }

    private var lettaClient: LettaAIClient? = null
    private var freedomClient: FreedomGPTClient? = null
    private val conversationHistory = mutableListOf<Map<String, String>>()

    init {
        if (lettaApiKey != null) {
            lettaClient = LettaAIClient(lettaApiKey)
        }
        if (freedomGptUrl != null) {
            freedomClient = FreedomGPTClient(freedomGptUrl)
        }
    }

    suspend fun chat(message: String): Result<String> {
        // Add user message to history
        conversationHistory.add(mapOf("role" to "user", "content" to message))

        val result = when (preferredProvider) {
            AIProvider.LETTA -> {
                lettaClient?.chat(message, conversationHistory)
                    ?: freedomClient?.chat(message, conversationHistory)
                    ?: Result.failure(Exception("No AI provider configured"))
            }
            AIProvider.FREEDOMGPT -> {
                freedomClient?.chat(message, conversationHistory)
                    ?: lettaClient?.chat(message, conversationHistory)
                    ?: Result.failure(Exception("No AI provider configured"))
            }
        }

        // Add assistant response to history
        result.getOrNull()?.let { response ->
            conversationHistory.add(mapOf("role" to "assistant", "content" to response))
            
            // Keep history manageable (last 20 messages)
            if (conversationHistory.size > 20) {
                conversationHistory.removeAt(0)
            }
        }

        return result
    }

    fun clearHistory() {
        conversationHistory.clear()
    }
}
