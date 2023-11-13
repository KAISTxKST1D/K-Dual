package com.example.k_dual.presentation.components

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Typeface
import com.example.k_dual.watchface.CustomPaint

fun DigitalClockCanvas(canvas: Canvas, hour: Int, minute: Int, typeface: Typeface) {
        val width = canvas.width
        val unitF = width / 192f

        val paint = CustomPaint.clockAndBatteryPaint(unitF, typeface)
        val timeText = String.format("%02d:%02d", 12, 33)
        val path = Path().apply {
            arcTo(width / 2 - 40f, 20f, width / 2 + 40f, 40f, -150f, 120f, false)
        }
        val arcLength = PathMeasure(path, false).length
        val textWidth = paint.measureText(timeText)
        val hOffset = (arcLength - textWidth) / 2

        canvas.drawTextOnPath(timeText, path, hOffset, unitF * 5f, paint)
}
