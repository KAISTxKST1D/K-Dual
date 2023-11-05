package com.example.k_dual.watchface

import android.R.attr.x
import android.R.attr.y
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.view.SurfaceHolder
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.example.k_dual.R
import java.time.ZonedDateTime


class KDualCanvasRender (
    context: Context,
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    canvasType: Int,
    interactiveDrawModeUpdateDelayMillis: Long = 16L
): Renderer.CanvasRenderer2<KDualAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = true
) {
    private var width : Float = 0F
    private var height : Float = 0F
    private var unitF: Float = 0f
    private var mHorizontal : Float = 0F
    private var mVertical : Float = 0F
    private var pHorizontal : Float = 0F
    private var pTop : Float = 0F
    private var pBottom : Float = 0F
    private var gap: Float = 0F

    private val textBounds = Rect()

    // Resources for arrow image files
    private val r = context.resources
    private val arrowUp: Bitmap = BitmapFactory.decodeResource(r, R.drawable.arrow_up)

    // Font assets
    private val assetManager = context.assets
    private val robotoMedium = Typeface.createFromAsset(assetManager, "Roboto-Medium.ttf")
    private val robotoRegular = Typeface.createFromAsset(assetManager, "Roboto-Regular.ttf")

    override suspend fun createSharedAssets(): KDualAssets {
        return KDualAssets()
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: KDualAssets
    ) {
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: KDualAssets
    ) {
        if (bounds.width().toFloat() != width) {
            width = bounds.width().toFloat()
            height = bounds.height().toFloat()
            unitF = width / 192f
            mHorizontal = unitF * 12f
            mVertical = unitF * 20.5f
            pHorizontal = unitF * 17.5f
            pTop = unitF * 8f
            pBottom = unitF * 14f
            gap = unitF * 1.5f
        }

        canvas.drawColor(Color.BLACK)

        drawDigitalClock(canvas, zonedDateTime.hour, zonedDateTime.minute)
        drawRemainingBattery(canvas)

        drawBackgroundBox(canvas, "up")
        drawBackgroundBox(canvas, "down")

        drawIconAndUserName(canvas, 1, "Minha", CustomColor.YELLOW)
        drawIconAndUserName(canvas, 2, "Jaewon", CustomColor.BLUE)

        drawDiffArrowBox(canvas, 1, 84)
        drawDiffArrowBox(canvas, 2, -8)

        drawBloodGlucose(canvas, 1, 144)
        drawBloodGlucose(canvas, 2, 94)
    }

    private fun drawDigitalClock(canvas: Canvas, hour: Int, minute: Int) {
        val clockPaint = CustomPaint.clockAndBatteryPaint(unitF, robotoMedium)

        val timeText = String.format("%02d:%02d", hour, minute)
        val path = Path().apply {
            arcTo(width/2 - 40f, 20f, width/2 + 40f, 40f, -150f, 120f, false) }
        val arcLength = PathMeasure(path, false).length
        val textWidth = clockPaint.measureText(timeText)
        val hOffset = (arcLength - textWidth) / 2

        canvas.drawTextOnPath(timeText, path, hOffset, 5f, clockPaint)
    }

    private fun drawRemainingBattery(canvas: Canvas) {
        val batteryPaint = CustomPaint.clockAndBatteryPaint(unitF, robotoMedium)
        // TODO. get real battery status
        val remainingBattery = 39
        val batteryText = "$remainingBattery%"
        val textWidth = batteryPaint.measureText(batteryText)
        canvas.drawText(batteryText, width/2-textWidth/2, height - 10, batteryPaint)
    }

    private fun drawBackgroundBox(canvas: Canvas, position: String) {
        val smallR = unitF * 24f
        val bigR = unitF * 120f

        var rect = RectF(mHorizontal, mVertical, width-mHorizontal, height/2 - gap)
        var corners = floatArrayOf(
            bigR, bigR,
            bigR, bigR,
            smallR, smallR,
            smallR, smallR
        )
        if (position == "down") {
            corners = corners.reversedArray()
            rect = RectF(mHorizontal, height/2 + gap, width-mHorizontal, height-mVertical)
        }

        val backgroundPaint = CustomPaint.backgroundPaint(rect)
        val path = Path().apply {
            addRoundRect(rect, corners, Path.Direction.CW)
        }
        canvas.drawPath(path, backgroundPaint)
    }

    private fun drawIconAndUserName(canvas: Canvas, order: Number, name: String, color: CustomColor) {
        val iconPaint = CustomPaint.iconPaint(color)
        val namePaint = CustomPaint.namePaint(color, unitF, robotoMedium)

        val centerX = width / 2f
        val centerY = if (order == 1) {
            mVertical + pTop*2
        } else {
            height - mVertical - pTop*2
        }

        val icWidth = unitF * 11f
        val space = unitF * 4f

        // Draw name text
        namePaint.getTextBounds(name, 0, name.length, textBounds)
        val totalWidth = icWidth + space + textBounds.width()

        val nameStartX = centerX - totalWidth/2 + icWidth + space
        canvas.drawText(name, nameStartX, centerY - textBounds.exactCenterY(), namePaint)

        val icCenterX = centerX - totalWidth/2 + icWidth/2
        val icCenterY = centerY

        // Draw waterDrop icon
        val originalWidth = 11f // The original width of the SVG
        val scale: Float = icWidth / originalWidth // Calculate the scale factor based on the new width

        val waterDropPath = Path().apply {
            moveTo(icCenterX + scale * (11 - originalWidth / 2), icCenterY + scale * (9.45087f - 7.5f))
            cubicTo(
                icCenterX + scale * (11 - originalWidth / 2), icCenterY + scale * (12.5156f - 7.5f),
                icCenterX + scale * (8.53757f - originalWidth / 2), icCenterY + scale * (15 - 7.5f),
                icCenterX + scale * (5.5f - originalWidth / 2), icCenterY + scale * (15 - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (2.46243f - originalWidth / 2), icCenterY + scale * (15 - 7.5f),
                icCenterX + scale * (0 - originalWidth / 2), icCenterY + scale * (12.5156f - 7.5f),
                icCenterX + scale * (0 - originalWidth / 2), icCenterY + scale * (9.45087f - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (0 - originalWidth / 2), icCenterY + scale * (6.38617f - 7.5f),
                icCenterX + scale * (5.5f - originalWidth / 2), icCenterY + scale * (0 - 7.5f),
                icCenterX + scale * (5.5f - originalWidth / 2), icCenterY + scale * (0 - 7.5f)
            )
            cubicTo(
                icCenterX + scale * (5.5f - originalWidth / 2), icCenterY + scale * (0 - 7.5f),
                icCenterX + scale * (11 - originalWidth / 2), icCenterY + scale * (6.38617f - 7.5f),
                icCenterX + scale * (11 - originalWidth / 2), icCenterY + scale * (9.45087f - 7.5f)
            )
            close()
        }

        canvas.drawPath(waterDropPath, iconPaint)
    }

    private fun drawDiffArrowBox(canvas: Canvas, order: Number, difference: Int) {
        // Draw rounded rect
        val rectHeight = unitF * 27f
        val rectWidth = if (difference < 10 && difference > -10) { unitF * 56f } else { unitF * 61f }
        val rectRoundness = unitF * 10f

        val rectTop = if (order == 1) {height/2 - gap - pBottom - rectHeight} else {height/2 + gap + pBottom}
        val rectLeft = mHorizontal + pHorizontal
        val rectF = RectF(rectLeft, rectTop, rectLeft + rectWidth, rectTop + rectHeight)

        val backgroundPaint = CustomPaint.arrowBoxPaint()
        canvas.drawRoundRect(rectF, rectRoundness, rectRoundness, backgroundPaint)

        // Draw arrow source bitmap
        // TODO. apply real arrow image
        var arrowSrc: Bitmap
        var differenceText: String = difference.toString()

        if (difference > 0) {
            arrowSrc = arrowUp
            differenceText = "+$difference"
        } else if (difference == 0) {
            arrowSrc = arrowUp
        } else {
            arrowSrc = arrowUp
        }

        // TODO. png resolution issue
        val boxPaddingHorizontal = unitF * 8f
        val rectCenterY = rectTop + rectHeight/2
        val arrowSize = unitF * 15f
        val arrowRect = RectF(rectLeft + boxPaddingHorizontal, rectCenterY - arrowSize/2, rectLeft + boxPaddingHorizontal + arrowSize, rectCenterY + arrowSize/2)
        canvas.drawBitmap(arrowSrc, null, arrowRect, null)

        // Draw difference value
        val paint = CustomPaint.differenceTextPaint(unitF, robotoRegular)
        paint.getTextBounds(differenceText, 0, differenceText.length, textBounds)
        canvas.drawText(differenceText, rectLeft+rectWidth-boxPaddingHorizontal-textBounds.width(), rectCenterY-textBounds.exactCenterY(), paint)
    }

    private fun drawBloodGlucose(canvas: Canvas, order: Number, value: Int) {
        val paint = CustomPaint.bloodGlucoseTextPaint(unitF, robotoMedium)
        val valueText = value.toString()
        paint.getTextBounds(valueText, 0, valueText.length, textBounds)

        val rectHalfHeight = unitF * 27f / 2f
        val textX = width - mHorizontal - pHorizontal - unitF*69f/2
        val textY = if (order == 1) { height/2 - gap - pBottom - rectHalfHeight + textBounds.height()/2 } else { height/2 + gap + pBottom + textBounds.height() - rectHalfHeight + textBounds.height()/2 }
        canvas.drawText(valueText, textX, textY, paint)
    }

}
class KDualAssets: Renderer.SharedAssets {
    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
