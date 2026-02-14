package com.rudra.assistant.tools

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object ToolRegistry {
    private const val TAG = "ToolRegistry"
    private val tools = mutableMapOf<String, Tool>()

    data class Tool(
        val name: String,
        val displayName: String,
        val description: String,
        val category: String,
        val params: List<ToolParam>,
        val requiresPermission: String? = null
    )

    data class ToolParam(
        val name: String,
        val type: String,
        val description: String,
        val required: Boolean
    )

    fun loadTools(context: Context) {
        try {
            val inputStream = context.assets.open("tools.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val name = obj.getString("name")
                val displayName = obj.getString("display_name")
                val description = obj.getString("description")
                val category = obj.getString("category")
                val permission = obj.optString("requires_permission", null)
                
                val paramsJson = obj.getJSONArray("params")
                val params = mutableListOf<ToolParam>()
                for (j in 0 until paramsJson.length()) {
                    val paramObj = paramsJson.getJSONObject(j)
                    params.add(
                        ToolParam(
                            name = paramObj.getString("name"),
                            type = paramObj.getString("type"),
                            description = paramObj.getString("description"),
                            required = paramObj.getBoolean("required")
                        )
                    )
                }

                tools[name] = Tool(name, displayName, description, category, params, permission)
            }
            Log.d(TAG, "Loaded ${tools.size} tools")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading tools", e)
        }
    }

    fun getTool(name: String): Tool? = tools[name]

    fun getAllTools(): List<Tool> = tools.values.toList()

    fun getToolsByCategory(category: String): List<Tool> =
        tools.values.filter { it.category.equals(category, ignoreCase = true) }
}
