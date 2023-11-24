package com.kaist.k_dual.watchface

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.BatteryManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.SurfaceHolder
//import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.TapEvent
import androidx.wear.watchface.TapType
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.kaist.k_canvas.KCanvas
import com.kaist.k_canvas.KColor
import java.time.ZonedDateTime
import androidx.core.app.ActivityCompat


class KDualCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    canvasType: Int,
    interactiveDrawModeUpdateDelayMillis: Long = 16L
) : Renderer.CanvasRenderer2<KDualAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = true
), WatchFace.TapListener {
    private val isDualMode: Boolean = true
    private var isUser1AlertOn: Boolean = false
    private var isUser2AlertOn: Boolean = false

    // Font assets
    private val assetManager = context.assets
    private val robotoMedium = Typeface.createFromAsset(assetManager, "Roboto-Medium.ttf")
    private val robotoRegular = Typeface.createFromAsset(assetManager, "Roboto-Regular.ttf")

    // Get watch battery stat
    private class BatteryReceiver : BroadcastReceiver() {
        var batteryStat: Int = 0
        override fun onReceive(p0: Context?, intent: Intent?) {
            batteryStat = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 0
        }
    }
    private val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    private val batteryReceiver = BatteryReceiver()

    // Shared Preferences
    private val sharedPref = context.getSharedPreferences(
        "MyPrefs",
        Context.MODE_PRIVATE
    )
    private val sharedPrefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            invalidate()
        }
    private lateinit var watchRect: Rect

    init {
        context.registerReceiver(batteryReceiver, intentFilter)
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(batteryReceiver)
    }

    override suspend fun createSharedAssets(): KDualAssets {
        return KDualAssets(context)
    }

    override fun renderHighlightLayer(
        canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime, sharedAssets: KDualAssets
    ) {
    }

    override fun render(
        canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime, sharedAssets: KDualAssets
    ) {
        if (!::watchRect.isInitialized) {
            watchRect = bounds
        }
        canvas.drawColor(Color.BLACK)

        KCanvas.drawDigitalClock(canvas, zonedDateTime.hour, zonedDateTime.minute, robotoMedium)
        KCanvas.drawRemainingBattery(canvas, batteryReceiver.batteryStat, robotoMedium)

        if (isDualMode) {
            KCanvas.drawBackgroundBox(canvas, "up", isUser1AlertOn, KColor.YELLOW)
            KCanvas.drawBackgroundBox(canvas, "down", isUser2AlertOn, KColor.BLUE)

            KCanvas.drawIconAndUserName(canvas, 1, "Minha", KColor.YELLOW, robotoMedium)
            KCanvas.drawIconAndUserName(canvas, 2, "Jaewon", KColor.BLUE, robotoMedium)

            KCanvas.drawDiffArrowBox(canvas, context, 1, isUser1AlertOn, KColor.YELLOW, 84, robotoRegular)
            KCanvas.drawDiffArrowBox(canvas, context, 2, isUser2AlertOn, KColor.BLUE, -8, robotoRegular)

            KCanvas.drawBloodGlucose(canvas, 1, 144, robotoMedium)
            KCanvas.drawBloodGlucose(canvas, 2, 94, robotoMedium)
        } else {
            KCanvas.drawBackgroundBox(canvas, null, isUser1AlertOn, KColor.PURPLE)
            KCanvas.drawIconAndUserName(canvas, null, "Minha", KColor.PURPLE, robotoMedium)
            KCanvas.drawBloodGlucose(canvas, null, 144, robotoMedium)
            KCanvas.drawDiffArrowBox(canvas, context,null, isUser1AlertOn, KColor.PURPLE, 4, robotoRegular)
        }

    }

    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
        // For test blink effect
        if (isDualMode && tapEvent.yPos > watchRect.height()/2 ) {
            if (tapType == TapType.DOWN) {
                blinkEffect(2)
                vibrateDevice()
            } else if (tapType == TapType.UP) {
                openWearApp(2)
            }
        } else {
            if (tapType == TapType.DOWN) {
                blinkEffect(1)
            } else if (tapType == TapType.UP) {
                openWearApp(1)
            }
        }
    }

    private fun vibrateDevice() {
        // Check if vibration permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with vibration
            performVibration()
        } else {
            // Permission is not granted, request for permission
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.VIBRATE), VIBRATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun performVibration() {
        val vibrator = getSystemService(context, Vibrator::class.java) as Vibrator

        val effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(effect)
        }
    }

    companion object {
        private const val VIBRATION_PERMISSION_REQUEST_CODE = 1
    }

    // Override this method in the Activity that hosts your watch face
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when (requestCode) {
//            VIBRATION_PERMISSION_REQUEST_CODE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // Vibration permission was granted, proceed with vibration
//                    performVibration()
//                } else {
//                    // Permission was denied, handle the case where the user denies permission
//                }
//                return
//            }
//            // Add other 'when' lines to check for other permissions this app might request
//            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//    }

    private fun openWearApp(userId: Int) {
        val packageName = "com.kaist.k_dual"
        val className = "com.kaist.k_dual.presentation.MainActivity"

        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setClassName(packageName, className)
        intent.putExtra("routeGraph", userId)

        startActivity(context, intent, null)
    }

    // Function for low/high alert highlights
    private fun blinkEffect(order: Number?) = CoroutineScope(Dispatchers.Main).launch {
        repeat(3) {
            when (order) {
                2 -> isUser2AlertOn = !isUser2AlertOn
                else -> isUser1AlertOn = !isUser1AlertOn
            }
            delay(if (it % 2 == 0) 400L else 100L)
        }
        isUser1AlertOn = false
        isUser2AlertOn = false
    }
}

// TODO. K-Canvas library에서 사용하는 법?
class KDualAssets(context: Context) : Renderer.SharedAssets {
//    val arrowUp: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.arrow_up)

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
