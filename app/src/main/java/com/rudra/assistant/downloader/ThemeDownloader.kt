package com.rudra.assistant.downloader

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

data class Theme(
    val id: String,
    val name: String,
    val primaryColor: String,
    val backgroundColor: String,
    val accentColor: String,
    val downloadUrl: String
)

class ThemeDownloader(private val context: Context) {
    companion object {
        private const val TAG = "ThemeDownloader"
        private const val THEME_API_URL = "https://raw.githubusercontent.com/piashmsuf-eng/themes/main/themes.json"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun fetchAvailableThemes(): Result<List<Theme>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(THEME_API_URL)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Failed to fetch themes: ${response.code}"))
                }

                val jsonString = response.body?.string() ?: return@withContext Result.failure(Exception("Empty response"))
                val jsonArray = JSONArray(jsonString)
                val themes = mutableListOf<Theme>()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    themes.add(
                        Theme(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            primaryColor = obj.getString("primary_color"),
                            backgroundColor = obj.getString("background_color"),
                            accentColor = obj.getString("accent_color"),
                            downloadUrl = obj.getString("download_url")
                        )
                    )
                }

                Result.success(themes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching themes", e)
            Result.failure(e)
        }
    }

    suspend fun downloadTheme(theme: Theme): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(theme.downloadUrl)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Download failed: ${response.code}"))
                }

                val bytes = response.body?.bytes() ?: return@withContext Result.failure(Exception("Empty file"))
                
                val themeDir = File(context.filesDir, "themes")
                if (!themeDir.exists()) themeDir.mkdirs()
                
                val themeFile = File(themeDir, "${theme.id}.json")
                themeFile.writeBytes(bytes)
                
                Log.d(TAG, "Theme downloaded: ${theme.name}")
                Result.success(themeFile)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading theme", e)
            Result.failure(e)
        }
    }
}
