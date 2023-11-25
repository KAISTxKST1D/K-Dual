package com.kaist.k_dual.presentation

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.kaist.k_canvas.DefaultSetting
import com.kaist.k_canvas.PREFERENCES_FILE_KEY
import com.kaist.k_canvas.SETTINGS_KEY
import com.kaist.k_canvas.Setting

object UseSetting {
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    var settings by mutableStateOf(DefaultSetting)

    private fun updateSettings() {
        val jsonString = sharedPreferences.getString(SETTINGS_KEY, null)
        Log.d("updateSettings", jsonString ?: "")
        settings = if (jsonString != null) {
            try {
                gson.fromJson(jsonString, Setting::class.java)
            } catch (e: JsonSyntaxException) {
                DefaultSetting
            } catch (e: JsonParseException) {
                DefaultSetting
            }
        } else {
            DefaultSetting
        }
    }

    fun initialize(
        context: Context,
    ) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, _ ->
            updateSettings()
        }
        updateSettings()
    }
}
