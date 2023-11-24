package com.kaist.k_dual.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import sendMessageToWearable

object ManageSetting {
    private const val PREFERENCES_FILE_KEY = "com.kaist.k_dual.settings_preferences"
    private const val SETTINGS_KEY = "settings"
    private const val MESSAGE_PATH = "/settings"
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    var settings by mutableStateOf(DefaultSetting)

    fun initialize(
        context: Context,
        onSendMessageFailed: () -> Unit = {},
        onSendMessageSuccess: () -> Unit = {}
    ) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        getSettings()
        val settingsJson = gson.toJson(settings)

        sendMessageToWearable(
            context = context,
            path = MESSAGE_PATH,
            data = settingsJson.toByteArray(),
            onFailure = onSendMessageFailed,
            onSuccess = onSendMessageSuccess
        )
    }

    fun saveSettings(
        settings: Setting,
        context: Context,
        onSendMessageFailed: () -> Unit = {},
        onSendMessageSuccess: () -> Unit = {}
    ) {
        val settingsJson = gson.toJson(settings)

        sendMessageToWearable(
            context = context,
            path = MESSAGE_PATH,
            data = settingsJson.toByteArray(),
            onFailure = onSendMessageFailed,
            onSuccess = {
                onSendMessageSuccess()
                this.settings = settings
                sharedPreferences.edit().apply {
                    putString(SETTINGS_KEY, settingsJson)
                    apply()
                }
                Log.d("phoneApp", "Settings changed:$settingsJson")
            }
        )

    }

    fun getSettings() {
        val settingsJson = sharedPreferences.getString(SETTINGS_KEY, null)
        settings = settingsJson?.let { gson.fromJson(it, Setting::class.java) } ?: DefaultSetting
    }
}
