package com.kaist.k_canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import kotlin.math.abs

private fun getUnitF(width: Int): Float { return width / 192f }
private fun getMHorizontal(unitF: Float, isDualMode: Boolean): Float {
    return if (isDualMode) { unitF * 6f }
    else { unitF * 3f }
}
private fun getMVertical(unitF: Float): Float { return unitF * 20f }
private fun getPHorizontal(unitF: Float): Float { return unitF * 17.5f }
private fun getPTop(unitF: Float, isDualMode: Boolean): Float {
    return if (isDualMode) { unitF * 11f }
    else { unitF * 22.8f }
}
private fun getPBottom(unitF: Float): Float { return unitF * 11f }
private fun getGap(unitF: Float): Float { return unitF * 1.5f }

// TODO. singleton for paint objects
// TODO. singleton for bitmap images

class KCanvas {
    companion object {
        fun drawDigitalClock(canvas: Canvas, hour: Int, minute: Int, typeface: Typeface) {
            val width = canvas.width
            val unitF = getUnitF(width)

            val paint = KPaint.clockAndBatteryPaint(unitF, typeface)
            val timeText = String.format("%02d:%02d", hour, minute)
            val path = Path().apply {
                arcTo(width / 2 - 40f, unitF * 10f, width / 2 + 40f, unitF * 20f, -150f, 120f, false)
            }
            val arcLength = PathMeasure(path, false).length
            val textWidth = paint.measureText(timeText)
            val hOffset = (arcLength - textWidth) / 2

            canvas.drawTextOnPath(timeText, path, hOffset, unitF * 5f, paint)
        }

        fun drawRemainingBattery(canvas: Canvas, remainingBattery: Int, typeface: Typeface) {
            val width = canvas.width
            val height = canvas.height
            val unitF = getUnitF(width)

            val batteryPaint = KPaint.clockAndBatteryPaint(unitF, typeface)
            val batteryText = "$remainingBattery%"
            val textWidth = batteryPaint.measureText(batteryText)
            canvas.drawText(batteryText, width / 2 - textWidth / 2, height.toFloat() - unitF * 6.5f, batteryPaint)
        }

        fun drawBackgroundBox(canvas: Canvas, position: String?, isAlertOn: Boolean, color: KColor?) {
            val width = canvas.width
            val height = canvas.height
            val unitF = getUnitF(width)
            val isDualMode = position != null
            val mHorizontal = getMHorizontal(unitF, isDualMode)
            val mVertical = getMVertical(unitF)
            val gap = getGap(unitF)

            val smallR = if (isDualMode) {
                unitF * 25.7f
            } else {
                unitF * 128f
            }
            val bigR = unitF * 128f

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

            val backgroundPaint = if (isAlertOn && color != null) {
                KPaint.backgroundPaint(color, rect)
            } else {
                KPaint.backgroundPaint(rect)
            }
            val path = Path().apply {
                addRoundRect(rect, corners, Path.Direction.CW)
            }
            canvas.drawPath(path, backgroundPaint)
        }

        fun drawSetupInfo(
            canvas: Canvas,
            context: Context,
            typeface: Typeface
        ) {
            val width = canvas.width
            val unitF = getUnitF(width)
            val mVertical = getMVertical(unitF)

            // Draw logo icon
            val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
            val pTop = unitF * 8.69f
            val logoWidth = unitF * 17.92f
            val logoHeight = unitF * 20.31f
            val logoRect = RectF(
                width/2 - logoWidth/2,
                mVertical + pTop,
                width/2 + logoWidth/2,
                mVertical + pTop + logoHeight
            )
            canvas.drawBitmap(logoBitmap, null, logoRect, null)

            // Draw info text
            val textBounds = Rect()
            val info1 = "Requires\nUser Setup"
            val info2 = "Return to the mobile app\non your smartphone\nto complete setup."
            val info1Paint = KPaint.setupInfoPaint(1, unitF, typeface)
            val info2Paint = KPaint.setupInfoPaint(2, unitF, typeface)
            val info1LineHeight = unitF * 24f
            val info2LineHeight = unitF * 12.5f

            var yPos = mVertical + unitF * (37f + 20f)

            for (line in info1.split("\n")) {
                info1Paint.getTextBounds(info1, 0, line.length, textBounds)
                canvas.drawText(line, width/2f, yPos, info1Paint)
                yPos += info1LineHeight
            }

            yPos = mVertical + unitF * (91f + 10f)
            for (line in info2.split("\n")) {
                info2Paint.getTextBounds(info2, 0, line.length, textBounds)
                canvas.drawText(line, width/2f, yPos, info2Paint)
                yPos += info2LineHeight
            }
        }

        fun drawIconAndUserName(
            canvas: Canvas,
            order: Number?,
            name: String,
            color: KColor,
            typeface: Typeface
        ) {
            val width = canvas.width
            val height = canvas.height
            val unitF = getUnitF(width)
            val isDualMode = order != null
            val mVertical = getMVertical(unitF)
            val pTop = getPTop(unitF, isDualMode)

            val textBounds = Rect()

            val iconPaint = KPaint.iconPaint(color)
            val namePaint = KPaint.namePaint(color, unitF, isDualMode, typeface)

            namePaint.getTextBounds(name, 0, name.length, textBounds)
            val textHeight = when {
                name.isEmpty() && order == null -> unitF * 18f
                name.isEmpty() -> unitF * 14f
                else -> textBounds.height().toFloat()
            }
            val centerX = width / 2f
            val centerY = if (order == null || order == 1) {
                mVertical + pTop + textHeight / 2
            } else {
                height - mVertical - pTop - textHeight / 2
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

        fun drawDiffArrowBox(
            canvas: Canvas,
            context: Context,
            order: Number?,
            isAlertOn: Boolean,
            color: KColor?,
            difference: Int,
            typeface: Typeface
        ) {
            val width = canvas.width
            val height = canvas.height
            val unitF = getUnitF(width)
            val isDualMode = order != null
            val mVertical = getMVertical(unitF)
            val mHorizontal = getMHorizontal(unitF, isDualMode)
            val pBottom = getPBottom(unitF)
            val pHorizontal = getPHorizontal(unitF)
            val gap = getGap(unitF)

            val textBounds = Rect()

            // Draw rounded rect
            val rectHeight = unitF * 27f
            val rectWidth = if (abs(difference) < 10) {
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
                KPaint.arrowBoxPaint(color, unitF)
            } else {
                KPaint.arrowBoxPaint(null, unitF)
            }
            canvas.drawRoundRect(rectF, rectRoundness, rectRoundness, backgroundPaint)

            // Draw arrow source bitmap (Use rule of nightscout)
            val arrowSrc: Bitmap = when {
                abs(difference) <= 5 ->
                    BitmapFactory.decodeResource(context.resources, R.drawable.arrow_flat)
                abs(difference) <= 9 -> {
                    if (difference > 0) BitmapFactory.decodeResource(context.resources, R.drawable.arrow_forty_five_up)
                    else BitmapFactory.decodeResource(context.resources, R.drawable.arrow_forty_five_down)
                }
                abs(difference) <= 19 -> {
                    if (difference > 0) BitmapFactory.decodeResource(context.resources, R.drawable.arrow_single_up)
                    else BitmapFactory.decodeResource(context.resources, R.drawable.arrow_single_down)
                }
                else -> {
                    if (difference > 0) BitmapFactory.decodeResource(context.resources, R.drawable.arrow_double_up)
                    else BitmapFactory.decodeResource(context.resources, R.drawable.arrow_double_down)
                }
            }
            val differenceText: String = if (difference > 0) {"+$difference"} else {"$difference"}

            val arrowPaint = if (isAlertOn && color != null) {
                KPaint.arrowPaint(color)
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
                KPaint.differenceTextPaint(unitF, typeface, color)
            } else {
                KPaint.differenceTextPaint(unitF, typeface, null)
            }
            paint.getTextBounds(differenceText, 0, differenceText.length, textBounds)
            canvas.drawText(
                differenceText,
                rectLeft + rectWidth - boxPaddingHorizontal - textBounds.width(),
                rectCenterY - textBounds.exactCenterY(),
                paint
            )
        }

        fun drawBloodGlucose(canvas: Canvas, order: Number?, value: Int, typeface: Typeface) {
            val width = canvas.width
            val height = canvas.height
            val unitF = getUnitF(width)
            val isDualMode = order != null
            val mHorizontal = getMHorizontal(unitF, isDualMode)
            val pBottom = getPBottom(unitF)
            val pHorizontal = getPHorizontal(unitF)
            val gap = getGap(unitF)

            val textBounds = Rect()
            val paint = KPaint.bloodGlucoseTextPaint(unitF, isDualMode, typeface)
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
                textX = width / 2f
                textY = height / 2f + textBounds.height() / 2f
            }
            canvas.drawText(valueText, textX, textY, paint)
        }
    }
}
