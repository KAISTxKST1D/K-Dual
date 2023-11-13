package kr.ac.kaist.k_canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface

private fun getUnitF(width: Int): Float { return width / 192f }
private fun getMHorizontal(unitF: Float): Float { return unitF * 12f }
private fun getMVertical(unitF: Float): Float { return unitF * 20.5f }
private fun getPHorizontal(unitF: Float): Float { return unitF * 17.5f }
private fun getPTop(unitF: Float, isDualMode: Boolean): Float {
    return if (isDualMode) { unitF * 8f }
    else { unitF * 20f }
}
private fun getPBottom(unitF: Float): Float { return unitF * 14f }
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
                arcTo(width / 2 - 40f, 20f, width / 2 + 40f, 40f, -150f, 120f, false)
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
            canvas.drawText(batteryText, width / 2 - textWidth / 2, height.toFloat() - 10, batteryPaint)
        }

        fun drawBackgroundBox(canvas: Canvas, position: String?, isAlertOn: Boolean, color: KColor) {
            val width = canvas.width
            val height = canvas.height
            val unitF = getUnitF(width)
            val isDualMode = position != null
            val mHorizontal = getMHorizontal(unitF)
            val mVertical = getMVertical(unitF)
            val gap = getGap(unitF)

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
                KPaint.backgroundPaint(color, rect)
            } else {
                KPaint.backgroundPaint(rect)
            }
            val path = Path().apply {
                addRoundRect(rect, corners, Path.Direction.CW)
            }
            canvas.drawPath(path, backgroundPaint)
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
            val mVertical = getMVertical(unitF)
            val mHorizontal = getMHorizontal(unitF)
            val pBottom = getPBottom(unitF)
            val pHorizontal = getPHorizontal(unitF)
            val gap = getGap(unitF)

            val textBounds = Rect()

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
                KPaint.arrowBoxPaint(color, unitF)
            } else {
                KPaint.arrowBoxPaint(null, unitF)
            }
            canvas.drawRoundRect(rectF, rectRoundness, rectRoundness, backgroundPaint)

            // Draw arrow source bitmap
            // TODO. apply real arrow image
            val arrowSrc: Bitmap
            var differenceText: String = difference.toString()

            val arrowUp = BitmapFactory.decodeResource(context.resources, R.drawable.arrow_up)

            if (difference > 0) {
                arrowSrc = arrowUp
                differenceText = "+$difference"
            } else if (difference == 0) {
                arrowSrc = arrowUp
            } else {
                arrowSrc = arrowUp
            }

            val arrowPaint = if (isAlertOn) {
                KPaint.arrowPaint(color!!)
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
            val mHorizontal = getMHorizontal(unitF)
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
