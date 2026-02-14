package com.rudra.assistant.voice

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CartesiaTTS(
    private val apiKey: String,
    private val context: Context
) {
    companion object {
        private const val TAG = "CartesiaTTS"
        private const val API_URL = "https://api.cartesia.ai/tts/bytes"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun speak(text: String, voice: String = "79a125e8-cd45-4c13-8a67-188112f4dd22"): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val requestBody = JSONObject().apply {
                    put("model_id", "sonic-english")
                    put("transcript", text)
                    put("voice", JSONObject().apply {
                        put("mode", "id")
                        put("id", voice)
                    })
                    put("output_format", JSONObject().apply {
                        put("container", "raw")
                        put("encoding", "pcm_s16le")
                        put("sample_rate", 44100)
                    })
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url(API_URL)
                    .header("X-API-Key", apiKey)
                    .header("Cartesia-Version", "2024-06-10")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Cartesia TTS failed: ${response.code}")
                        return@withContext Result.failure(Exception("TTS error: ${response.code}"))
                    }

                    val audioData = response.body?.bytes()
                        ?: return@withContext Result.failure(Exception("Empty audio data"))

                    // Play audio
                    playAudio(audioData)

                    Result.success(Unit)
                }
            } catch (e: Exception) {
                Log.e(TAG, "TTS error", e)
                Result.failure(e)
            }
        }

    private fun playAudio(audioData: ByteArray) {
        val sampleRate = 44100
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT

        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(audioFormat)
                    .setChannelMask(channelConfig)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack.play()
        audioTrack.write(audioData, 0, audioData.size)
        audioTrack.stop()
        audioTrack.release()
    }
}
