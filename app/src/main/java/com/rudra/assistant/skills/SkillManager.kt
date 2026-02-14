package com.rudra.assistant.skills

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object SkillManager {
    private const val TAG = "SkillManager"
    private val skills = mutableListOf<Skill>()

    data class Skill(
        val id: String,
        val keywords: List<String>,
        val action: String,
        val target: String,
        val params: List<String> = emptyList(),
        val category: String = ""
    )

    fun loadSkills(context: Context) {
        try {
            val inputStream = context.assets.open("skills.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val action = obj.getString("action")
                val target = obj.optString("target", "")
                val category = obj.optString("category", "")
                
                val keywordsJson = obj.getJSONArray("keywords")
                val keywords = mutableListOf<String>()
                for (j in 0 until keywordsJson.length()) {
                    keywords.add(keywordsJson.getString(j))
                }

                val paramsJson = obj.optJSONArray("params")
                val params = mutableListOf<String>()
                if (paramsJson != null) {
                    for (j in 0 until paramsJson.length()) {
                        params.add(paramsJson.getString(j))
                    }
                }

                skills.add(Skill(id, keywords, action, target, params, category))
            }
            Log.d(TAG, "Loaded ${skills.size} skills")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading skills", e)
        }
    }

    fun findSkill(text: String): Skill? {
        val lowerText = text.lowercase()
        return skills
            .filter { skill -> skill.keywords.any { keyword -> lowerText.contains(keyword.lowercase()) } }
            .maxByOrNull { skill -> 
                skill.keywords.count { keyword -> lowerText.contains(keyword.lowercase()) }
            }
    }

    fun getAllSkills(): List<Skill> = skills

    fun getSkillsByCategory(category: String): List<Skill> = 
        skills.filter { it.category.equals(category, ignoreCase = true) }
}
