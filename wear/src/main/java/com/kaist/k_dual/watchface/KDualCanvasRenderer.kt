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
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.SurfaceHolder
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
import com.kaist.k_canvas.PREFERENCES_FILE_KEY
import com.kaist.k_canvas.SETTINGS_KEY
import com.kaist.k_canvas.Setting
import java.time.ZonedDateTime
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import com.kaist.k_dual.presentation.UseBloodGlucose

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
    private val gson = Gson()
    private var settings: Setting? = null

    private var isUser1ColorAlertOn: Boolean = false
    private var isUser2ColorAlertOn: Boolean = false

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

    private lateinit var watchRect: Rect

    // Shared Preferences
    private val sharedPref = context.getSharedPreferences(
        PREFERENCES_FILE_KEY,
        Context.MODE_PRIVATE
    )
    private val sharedPrefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            updateSettings()
            updateBloodGlucoseTask.run()
            invalidate()
        }
    private val updateSettings: () -> Unit = {
        val jsonString = sharedPref.getString(SETTINGS_KEY, null)
        settings = if (jsonString != null) {
            try {
                gson.fromJson(jsonString, Setting::class.java)
            } catch (e: JsonSyntaxException) {
                null
            } catch (e: JsonParseException) {
                null
            }
        } else {
            null
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5 * 60 * 1000L

    private val updateBloodGlucoseTask = object : Runnable {
        override fun run() {
            UseBloodGlucose.updateBloodGlucose(context)
            invalidate()
            handler.postDelayed(this, updateInterval)
        }
    }

    private val checkAlertConditionTask = object : Runnable {
        override fun run() {
            checkAlertCondition()
            handler.postDelayed(this, updateInterval)
        }
    }

    private fun checkAlertCondition() {
        settings?.let {
            val firstUserBGValue = UseBloodGlucose.firstUser.toIntOrNull()
            firstUserBGValue?.let { bgValue ->
                if (bgValue > it.firstUserSetting.highValue || bgValue < it.firstUserSetting.lowValue) {
                    if (it.firstUserSetting.vibrationEnabled) {
                        vibrateDevice()
                    }
                    if (it.firstUserSetting.colorBlinkEnabled) {
                        blinkEffect(1)
                    }
                }
            }
            if (it.enableDualMode) {
                val secondUserBGValue = UseBloodGlucose.secondUser.toIntOrNull()
                secondUserBGValue?.let { bgValue ->
                    if (bgValue > it.secondUserSetting.highValue || bgValue < it.secondUserSetting.lowValue) {
                        if (it.secondUserSetting.vibrationEnabled) {
                            vibrateDevice()
                        }
                        if (it.secondUserSetting.colorBlinkEnabled) {
                            blinkEffect(2)
                        }
                    }
                }
            }
        }
    }

    init {
        context.registerReceiver(batteryReceiver, intentFilter)
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)
        updateSettings()
        updateBloodGlucoseTask.run()
        checkAlertConditionTask.run()
        handler.post(updateBloodGlucoseTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(batteryReceiver)
        handler.removeCallbacks(updateBloodGlucoseTask)
        handler.removeCallbacks(checkAlertConditionTask)
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

        if (settings == null) {
            KCanvas.drawBackgroundBox(canvas, null, false, null)
            KCanvas.drawSetupInfo(canvas, context, robotoMedium)
            return
        }

        settings?.let {
            if (it.enableDualMode) {
                KCanvas.drawBackgroundBox(
                    canvas,
                    "up",
                    isUser1ColorAlertOn,
                    it.firstUserSetting.color
                )
                KCanvas.drawBackgroundBox(
                    canvas,
                    "down",
                    isUser2ColorAlertOn,
                    it.secondUserSetting.color
                )

                KCanvas.drawIconAndUserName(
                    canvas,
                    1,
                    it.firstUserSetting.name,
                    it.firstUserSetting.color,
                    robotoMedium
                )
                KCanvas.drawIconAndUserName(
                    canvas,
                    2,
                    it.secondUserSetting.name,
                    it.secondUserSetting.color,
                    robotoMedium
                )

                KCanvas.drawDiffArrowBox(
                    canvas,
                    context,
                    1,
                    isUser1ColorAlertOn,
                    it.firstUserSetting.color,
                    UseBloodGlucose.firstUserDiff,
                    it.glucoseUnits,
                    robotoRegular
                )
                KCanvas.drawDiffArrowBox(
                    canvas,
                    context,
                    2,
                    isUser2ColorAlertOn,
                    it.secondUserSetting.color,
                    UseBloodGlucose.secondUserDiff,
                    it.glucoseUnits,
                    robotoRegular
                )

                KCanvas.drawBloodGlucose(canvas, 1, UseBloodGlucose.firstUser, it.glucoseUnits, robotoMedium)
                KCanvas.drawBloodGlucose(canvas, 2, UseBloodGlucose.secondUser, it.glucoseUnits, robotoMedium)
            } else {
                KCanvas.drawBackgroundBox(
                    canvas,
                    null,
                    isUser1ColorAlertOn,
                    it.firstUserSetting.color
                )
                KCanvas.drawIconAndUserName(
                    canvas,
                    null,
                    it.firstUserSetting.name,
                    it.firstUserSetting.color,
                    robotoMedium
                )
                KCanvas.drawBloodGlucose(canvas, null, UseBloodGlucose.firstUser, it.glucoseUnits, robotoMedium)
                KCanvas.drawDiffArrowBox(
                    canvas,
                    context,
                    null,
                    isUser1ColorAlertOn,
                    it.firstUserSetting.color,
                    UseBloodGlucose.firstUserDiff,
                    it.glucoseUnits,
                    robotoRegular
                )
            }
        }
    }

    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
        // For test blink effect
        if (settings?.enableDualMode == true && tapEvent.yPos > watchRect.height() / 2) {
            if (tapType == TapType.UP) {
                openWearApp(2)
            }
        } else {
            if (tapType == TapType.UP) {
                openWearApp(1)
            }
        }
    }

    private fun vibrateDevice() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.VIBRATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            performVibration()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.VIBRATE),
                VIBRATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun performVibration() {
        val vibrator = getSystemService(context, Vibrator::class.java) as Vibrator

        val pattern = longArrayOf(0, 500, 200, 500, 200)
        val amplitude = VibrationEffect.DEFAULT_AMPLITUDE

        val effect = VibrationEffect.createWaveform(pattern, amplitude)

        if (vibrator.hasVibrator()) {
            vibrator.vibrate(effect)
        }
    }

    companion object {
        private const val VIBRATION_PERMISSION_REQUEST_CODE = 1
    }

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

    // Function for low/high color alert
    private fun blinkEffect(order: Number) = CoroutineScope(Dispatchers.Main).launch {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        // TODO. PowerManager flags are deprecated
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "KDualWatch:WakelockTag"
        )
        wakeLock?.acquire(10 * 1000L)

        repeat(3) {
            when (order) {
                1 -> isUser1ColorAlertOn = !isUser1ColorAlertOn
                else -> isUser2ColorAlertOn = !isUser2ColorAlertOn
            }
            delay(if (it % 2 == 0) 400L else 100L)
        }
        isUser1ColorAlertOn = false
        isUser2ColorAlertOn = false
    }
}

// TODO. K-Canvas library에서 사용하는 법?
class KDualAssets(context: Context) : Renderer.SharedAssets {
//    val arrowUp: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.arrow_up)

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
