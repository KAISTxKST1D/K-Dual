package com.kaist.k_dual.service

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.kaist.k_canvas.MESSAGE_PATH
import com.kaist.k_canvas.PREFERENCES_FILE_KEY
import com.kaist.k_canvas.SETTINGS_KEY


class ListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d("WearApp", "Message received, path: ${messageEvent.path}, data: ${messageEvent.data}")
        if (messageEvent.path == MESSAGE_PATH) {
            val sharedPref = getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(SETTINGS_KEY, messageEvent.data.decodeToString())
            editor.apply()
        }
    }
}