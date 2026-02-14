package com.rudra.assistant

import android.app.Application
import android.util.Log
import com.rudra.assistant.skills.SkillManager
import com.rudra.assistant.tools.ToolRegistry

class RudraApp : Application() {
    
    companion object {
        private const val TAG = "RudraApp"
        lateinit var instance: RudraApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        Log.d(TAG, "Initializing RUDRA AI Assistant...")
        
        // Initialize Skills and Tools
        SkillManager.loadSkills(this)
        ToolRegistry.loadTools(this)
        
        Log.d(TAG, "RUDRA initialized successfully")
    }
}
