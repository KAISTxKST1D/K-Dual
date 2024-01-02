package com.kaist.k_dual.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kaist.k_dual.model.UseBloodGlucose
import kotlinx.coroutines.*

class GlucoseUpdateService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            while (true) {
                UseBloodGlucose.updateBloodGlucose(applicationContext)
                delay(30 * 1000L)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // cancel the job when the service is destroyed
    }
}