package com.kaist.k_dual.watchface

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.os.BatteryManager
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.TapEvent
import androidx.wear.watchface.TapType
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.kaist.k_dual.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

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
    private val isDualMode: Boolean = false
    private var isUser1AlertOn: Boolean = false
    private var isUser2AlertOn: Boolean = false

    private var width: Float = 0F
    private var height: Float = 0F
    private var unitF: Float = 0f
    private var mHorizontal: Float = 0F
    private var mVertical: Float = 0F
    private var pHorizontal: Float = 0F
    private var pTop: Float = 0F
    private var pBottom: Float = 0F
    private var gap: Float = 0F

    private val textBounds = Rect()

    // TODO. paint 여기서 한번만 불러 오는 게 낫나?
    // TODO. singleton for paint objects

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
        if (bounds.width().toFloat() != width) {
            width = bounds.width().toFloat()
            height = bounds.height().toFloat()
            unitF = width / 192f
            mHorizontal = unitF * 12f
            mVertical = unitF * 20.5f
            pHorizontal = unitF * 17.5f
            pTop = if (isDualMode) {
                unitF * 8f
            } else {
                unitF * 20f
            }
            pBottom = unitF * 14f
            gap = unitF * 1.5f
        }

        canvas.drawColor(Color.BLACK)

        drawDigitalClock(canvas, zonedDateTime.hour, zonedDateTime.minute)
        drawRemainingBattery(canvas)

        if (isDualMode) {
            drawBackgroundBox(canvas, "up", isUser1AlertOn, CustomColor.YELLOW)
            drawBackgroundBox(canvas, "down", isUser2AlertOn, CustomColor.BLUE)

            drawIconAndUserName(canvas, 1, "Minha", CustomColor.YELLOW)
            drawIconAndUserName(canvas, 2, "Jaewon", CustomColor.BLUE)

            drawDiffArrowBox(canvas, sharedAssets, 1, isUser1AlertOn, CustomColor.YELLOW, 84)
            drawDiffArrowBox(canvas, sharedAssets, 2, isUser2AlertOn, CustomColor.BLUE, -8)

            drawBloodGlucose(canvas, 1, 144)
            drawBloodGlucose(canvas, 2, 94)
        } else {
            drawBackgroundBox(canvas, null, isUser1AlertOn, CustomColor.PURPLE)
            drawIconAndUserName(canvas, null, "Minha", CustomColor.PURPLE)
            drawBloodGlucose(canvas, null, 144)
            drawDiffArrowBox(canvas, sharedAssets, null, isUser1AlertOn, CustomColor.PURPLE, 4)
        }
    }

    private fun drawDigitalClock(canvas: Canvas, hour: Int, minute: Int) {
        val clockPaint = CustomPaint.clockAndBatteryPaint(unitF, robotoMedium)

        val timeText = String.format("%02d:%02d", hour, minute)
        val path = Path().apply {
            arcTo(width / 2 - 40f, 20f, width / 2 + 40f, 40f, -150f, 120f, false)
        }
        val arcLength = PathMeasure(path, false).length
        val textWidth = clockPaint.measureText(timeText)
        val hOffset = (arcLength - textWidth) / 2

        canvas.drawTextOnPath(timeText, path, hOffset, unitF * 5f, clockPaint)
    }

    private fun drawRemainingBattery(canvas: Canvas) {
        val batteryPaint = CustomPaint.clockAndBatteryPaint(unitF, robotoMedium)
        val batteryText = "${batteryReceiver.batteryStat}%"
        val textWidth = batteryPaint.measureText(batteryText)
        canvas.drawText(batteryText, width / 2 - textWidth / 2, height - 10, batteryPaint)
    }

    private fun drawBackgroundBox(
        canvas: Canvas,
        position: String?,
        isAlertOn: Boolean,
        color: CustomColor
    ) {
        val smallR = if (isDualMode) {
            unitF * 24f
        } else {
            unitF * 120f
        }
        val bigR = unitF * 120f

        var corners = floatArrayOf(
            bigR, bigR, bigR, bigR, smallR, smallR, smallR, smallR
        )

        val rect: RectF = when (position) {
            "up" -> RectF(mHorizontal, mVertical, width - mHorizontal, height / 2 - gap)
            "down" -> RectF(mHorizontal, height / 2 + gap, width - mHorizontal, height - mVertical)
            else -> RectF(mHorizontal, mVertical, width - mHorizontal, height - mVertical)
        }
        if (position == "down") {
            corners = corners.reversedArray()
        }

        val backgroundPaint = if (isAlertOn) {
            CustomPaint.backgroundPaint(color, rect)
        } else {
            CustomPaint.backgroundPaint(rect)
        }
        val path = Path().apply {
            addRoundRect(rect, corners, Path.Direction.CW)
        }
        canvas.drawPath(path, backgroundPaint)
    }

    private fun drawIconAndUserName(
        canvas: Canvas,
        order: Number?,
        name: String,
        color: CustomColor
    ) {
        val iconPaint = CustomPaint.iconPaint(color)
        val namePaint = CustomPaint.namePaint(color, unitF, isDualMode, robotoMedium)

        namePaint.getTextBounds(name, 0, name.length, textBounds)
        val centerX = width / 2f
        val centerY = if (order == null || order == 1) {
            mVertical + pTop + textBounds.height() / 2
        } else {
            height - mVertical - pTop - textBounds.height() / 2
        }

        val icWidth = if (isDualMode) {
            unitF * 11f
        } else {
            unitF * 13f
        }
        val space = if (isDualMode) {
            unitF * 4f
        } else {
            unitF * 6.57f
        }
        val totalWidth = icWidth + space + textBounds.width()

        // Draw name text
        val nameStartX = centerX - totalWidth / 2 + icWidth + space
        canvas.drawText(name, nameStartX, centerY - textBounds.exactCenterY(), namePaint)

        // Draw waterDrop icon
        val icCenterX = centerX - totalWidth / 2 + icWidth / 2
        val icCenterY = centerY

        val originalWidth = 11f // The original width of the SVG
        val scale: Float =
            icWidth / originalWidth // Calculate the scale factor based on the new width

        val waterDropPath = Path().apply {
            moveTo(
                icCenterX + scale * (11 - originalWidth / 2),
                icCenterY + scale * (9.45087f - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (11 - originalWidth / 2),
                icCenterY + scale * (12.5156f - 7.5f),
                icCenterX + scale * (8.53757f - originalWidth / 2),
                icCenterY + scale * (15 - 7.5f),
                icCenterX + scale * (5.5f - originalWidth / 2),
                icCenterY + scale * (15 - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (2.46243f - originalWidth / 2),
                icCenterY + scale * (15 - 7.5f),
                icCenterX + scale * (0 - originalWidth / 2),
                icCenterY + scale * (12.5156f - 7.5f),
                icCenterX + scale * (0 - originalWidth / 2),
                icCenterY + scale * (9.45087f - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (0 - originalWidth / 2),
                icCenterY + scale * (6.38617f - 7.5f),
                icCenterX + scale * (5.5f - originalWidth / 2),
                icCenterY + scale * (0 - 7.5f),
                icCenterX + scale * (5.5f - originalWidth / 2),
                icCenterY + scale * (0 - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (5.5f - originalWidth / 2),
                icCenterY + scale * (0 - 7.5f),
                icCenterX + scale * (11 - originalWidth / 2),
                icCenterY + scale * (6.38617f - 7.5f),
                icCenterX + scale * (11 - originalWidth / 2),
                icCenterY + scale * (9.45087f - 7.5f)
            )
            close()
        }

        canvas.drawPath(waterDropPath, iconPaint)
    }

    private fun drawDiffArrowBox(
        canvas: Canvas,
        sharedAssets: KDualAssets,
        order: Number?,
        isAlertOn: Boolean,
        color: CustomColor,
        difference: Int
    ) {
        // Draw rounded rect
        val rectHeight = unitF * 27f
        val rectWidth = if (difference < 10 && difference > -10) {
            unitF * 56f
        } else {
            unitF * 61f
        }
        val rectRoundness = unitF * 10f

        val rectTop: Float
        val rectLeft: Float

        when (order) {
            1 -> {
                rectTop = height / 2 - gap - pBottom - rectHeight
                rectLeft = mHorizontal + pHorizontal
            }

            2 -> {
                rectTop = height / 2 + gap + pBottom
                rectLeft = mHorizontal + pHorizontal
            }

            else -> {
                rectTop = height - mVertical - pBottom - rectHeight
                rectLeft = width / 2 - rectWidth / 2
            }
        }

        val rectF = RectF(rectLeft, rectTop, rectLeft + rectWidth, rectTop + rectHeight)
        val backgroundPaint = if (isAlertOn) {
            CustomPaint.arrowBoxPaint(color, unitF)
        } else {
            CustomPaint.arrowBoxPaint(null, unitF)
        }
        canvas.drawRoundRect(rectF, rectRoundness, rectRoundness, backgroundPaint)

        // Draw arrow source bitmap
        // TODO. apply real arrow image
        val arrowSrc: Bitmap
        var differenceText: String = difference.toString()

        if (difference > 0) {
            arrowSrc = sharedAssets.arrowUp
            differenceText = "+$difference"
        } else if (difference == 0) {
            arrowSrc = sharedAssets.arrowUp
        } else {
            arrowSrc = sharedAssets.arrowUp
        }

        val arrowPaint = if (isAlertOn) {
            CustomPaint.arrowPaint(color)
        } else {
            null
        }

        val boxPaddingHorizontal = unitF * 8f
        val rectCenterY = rectTop + rectHeight / 2
        val arrowSize = unitF * 15f
        val arrowRect = RectF(
            rectLeft + boxPaddingHorizontal,
            rectCenterY - arrowSize / 2,
            rectLeft + boxPaddingHorizontal + arrowSize,
            rectCenterY + arrowSize / 2
        )
        canvas.drawBitmap(arrowSrc, null, arrowRect, arrowPaint)

        // Draw difference value
        val paint = if (isAlertOn) {
            CustomPaint.differenceTextPaint(unitF, robotoRegular, color)
        } else {
            CustomPaint.differenceTextPaint(unitF, robotoRegular, null)
        }
        paint.getTextBounds(differenceText, 0, differenceText.length, textBounds)
        canvas.drawText(
            differenceText,
            rectLeft + rectWidth - boxPaddingHorizontal - textBounds.width(),
            rectCenterY - textBounds.exactCenterY(),
            paint
        )
    }

    private fun drawBloodGlucose(canvas: Canvas, order: Number?, value: Int) {
        val paint = CustomPaint.bloodGlucoseTextPaint(unitF, isDualMode, robotoMedium)
        val valueText = value.toString()
        paint.getTextBounds(valueText, 0, valueText.length, textBounds)

        val textX: Float
        val textY: Float

        if (isDualMode) {
            val rectHalfHeight = unitF * 27f / 2f
            textX = width - mHorizontal - pHorizontal - unitF * 69f / 2
            textY = if (order == 1) {
                height / 2 - gap - pBottom - rectHalfHeight + textBounds.height() / 2
            } else {
                height / 2 + gap + pBottom + textBounds.height() - rectHalfHeight + textBounds.height() / 2
            }
        } else {
            textX = width / 2
            textY = height / 2 + textBounds.height() / 2
        }

        canvas.drawText(valueText, textX, textY, paint)
    }

    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
        // For test blink effect
        if (tapType == TapType.DOWN) {
            blinkEffect(1)
        }
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

class KDualAssets(context: Context) : Renderer.SharedAssets {
    val arrowUp: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.arrow_up)

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
