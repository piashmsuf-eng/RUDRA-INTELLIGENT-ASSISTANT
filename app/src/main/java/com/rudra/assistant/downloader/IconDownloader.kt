package com.rudra.assistant.downloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

class IconDownloader(private val context: Context) {
    companion object {
        private const val TAG = "IconDownloader"
        // Free icon APIs
        private const val FLATICON_API = "https://api.flaticon.com/v3/item/icon/premium"
        private const val ICONS8_CDN = "https://img.icons8.com/color/512"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun downloadIcon(iconUrl: String, iconName: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(iconUrl)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Download failed: ${response.code}"))
                }

                val bytes = response.body?.bytes() ?: return@withContext Result.failure(Exception("Empty file"))
                
                val iconDir = File(context.filesDir, "icons")
                if (!iconDir.exists()) iconDir.mkdirs()
                
                val iconFile = File(iconDir, "$iconName.png")
                iconFile.writeBytes(bytes)
                
                Log.d(TAG, "Icon downloaded: $iconName")
                Result.success(iconFile)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading icon", e)
            Result.failure(e)
        }
    }

    fun getLocalIcon(iconName: String): File? {
        val iconFile = File(context.filesDir, "icons/$iconName.png")
        return if (iconFile.exists()) iconFile else null
    }
}
