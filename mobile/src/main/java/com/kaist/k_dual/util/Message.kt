import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val DUAL_CAPABILITY_NAME = "dual_viewer_wear"
private const val RETRY_DELAY_SECONDS = 5L
private val executorService = Executors.newSingleThreadScheduledExecutor()
var connectedNodeID: String? = null

fun findWearableNode(
    context: Context,
    capabilityClient: CapabilityClient,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    isFirstTrial: Boolean = true,
) {
    val handleFailure = {
        if (isFirstTrial) {
            onFailure()
        }
        Log.e("phoneApp", "Wearable node not found")
        scheduleRetry(
            capabilityClient = capabilityClient,
            onSuccess = onSuccess,
            onFailure = onFailure,
            context = context,
        )
    }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val nodes = Tasks.await(
                capabilityClient.getCapability(
                    DUAL_CAPABILITY_NAME,
                    CapabilityClient.FILTER_REACHABLE
                )
            ).nodes

            val targetNodeId = nodes.find { it.isNearby }?.id

            if (targetNodeId != null) {
                connectedNodeID = targetNodeId
                sendMessageToWearable(
                    context = context,
                    path = "",
                    data = null,
                    onFailure = { handleFailure() },
                    onSuccess = {
                        if (!isFirstTrial) {
                            onSuccess()
                        }
                        Log.d("phoneApp", "Wearable node connected: $connectedNodeID")
                    },
                )
            } else {
                handleFailure()
            }
        } catch (_: Exception) {
            handleFailure()
        }
    }
}

private fun scheduleRetry(
    capabilityClient: CapabilityClient,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    context: Context,
) {
    executorService.schedule({
        findWearableNode(
            capabilityClient = capabilityClient,
            onSuccess = onSuccess,
            onFailure = onFailure,
            isFirstTrial = false,
            context = context
        )
    }, RETRY_DELAY_SECONDS, TimeUnit.SECONDS)
}

fun sendMessageToWearable(
    context: Context,
    path: String,
    data: ByteArray?,
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {}
) {
    connectedNodeID?.let { nodeId ->
        try {
            Wearable.getMessageClient(context).sendMessage(nodeId, path, data)
                .addOnSuccessListener {
                    Log.d("PhoneApp", "Message sent successfully")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("PhoneApp", "Failed to send message", e)
                    onFailure()
                }
        } catch (_: Exception) {
            onFailure()
            Log.e("PhoneApp", "No connected node found")
        }
    } ?: run {
        onFailure()
        Log.e("PhoneApp", "No connected node found")
    }
}
