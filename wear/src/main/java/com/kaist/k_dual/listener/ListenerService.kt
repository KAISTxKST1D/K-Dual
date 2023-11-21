package com.kaist.k_dual.listener

import android.content.Context
import android.util.Log
import com.kaist.k_dual.SETTINGS_SHARED_PREF_KEY
import com.kaist.k_dual.SHARED_PREF_NAME
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class ListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d("WearApp", "Message received, path: ${messageEvent.path}, data: ${messageEvent.data}")
        val sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(SETTINGS_SHARED_PREF_KEY, messageEvent.data.decodeToString())
        editor.apply()
    }
}