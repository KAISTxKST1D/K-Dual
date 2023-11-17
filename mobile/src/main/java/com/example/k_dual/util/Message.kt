package com.example.k_dual.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DUAL_CAPABILITY_NAME = "dual_viewer_wear"
var connectedNodeID: String? = null

fun findWearableNode(
    capabilityClient: CapabilityClient,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val nodes = Tasks.await(
            capabilityClient.getCapability(
                DUAL_CAPABILITY_NAME,
                CapabilityClient.FILTER_REACHABLE
            )
        ).nodes

        val targetNodeId = nodes.find { it.isNearby }?.id

        if (targetNodeId != null) {
            connectedNodeID = targetNodeId
            onSuccess()
            Log.d("phoneApp", "Wearable node connected: $connectedNodeID")
        } else {
            onFailure()
            Log.e("phoneApp", "Wearable node not found")
            Handler(Looper.getMainLooper()).postDelayed({
                findWearableNode(capabilityClient, onSuccess, onFailure) // Recursion
            }, 5000)
        }
    }
}

fun sendMessageToWearable(context: Context, path: String, data: ByteArray?, onFailure: () -> Unit) {
    connectedNodeID?.let { nodeId ->
        Wearable.getMessageClient(context).sendMessage(nodeId, path, data)
            .addOnSuccessListener {
                Log.d("PhoneApp", "Message sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e("PhoneApp", "Failed to send message", e)
                onFailure();
            }
    } ?: run {
        Log.e("PhoneApp", "No connected node found")
    }
}