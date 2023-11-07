package com.example.k_dual.watchface

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface

enum class CustomColor {
    RED, YELLOW, GREEN, BLUE, PURPLE
}

class CustomPaint {
    companion object {
        fun clockAndBatteryPaint(unitF: Float, font: Typeface) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            typeface = font
            textSize = unitF * 13
        }

        private fun createGradientPaint(bound: RectF, colors: IntArray): Paint {
            val positions = floatArrayOf(0.0f, 1.3f)
            val angle = 111.73f

            val matrix = Matrix()
            matrix.setRotate(angle-90f, bound.width()/2, bound.top+bound.height()/2)
            
            val gradientShader = LinearGradient(bound.left, bound.top, bound.right, bound.bottom, colors, positions, Shader.TileMode.CLAMP)
            gradientShader.setLocalMatrix(matrix)

            val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
                shader = gradientShader
            }
            return backgroundPaint
        }

        fun backgroundPaint(bound: RectF): Paint {
            val colors = intArrayOf(
                0x33DADCDD, 0x218FC1C6
            )
            return createGradientPaint(bound, colors)
        }

        fun backgroundPaint(colorName: CustomColor, bound: RectF): Paint {
            val colors =  when (colorName) {
                CustomColor.RED -> intArrayOf(
                    Color.argb(54, 254, 156, 148),
                    Color.argb(164, 254, 156, 148)
                )
                CustomColor.YELLOW -> intArrayOf(
                    Color.argb(48, 255, 233, 164),
                    Color.argb(128, 244, 210, 106)
                )
                CustomColor.GREEN -> intArrayOf(
                    Color.argb(61, 103, 205, 125),
                    Color.argb(147, 100, 214, 125)
                )
                CustomColor.BLUE -> intArrayOf(
                    Color.argb(77, 159, 201, 255),
                    Color.argb(140, 101, 165, 247)
                )
                CustomColor.PURPLE -> intArrayOf(
                    Color.argb(61, 206, 147, 250),
                    Color.argb(140, 207, 148, 254)
                )
            }
            return createGradientPaint(bound, colors)
        }

        fun iconPaint(colorName: CustomColor): Paint {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.style = Paint.Style.FILL

            when (colorName) {
                CustomColor.RED -> paint.color = Color.parseColor("#EE675C")
                CustomColor.YELLOW -> paint.color = Color.parseColor("#FDD663")
                CustomColor.GREEN -> paint.color = Color.parseColor("#2AAB46")
                CustomColor.BLUE -> paint.color = Color.parseColor("#1B6BD5")
                CustomColor.PURPLE -> paint.color = Color.parseColor("#AE5CEE")
            }
            return paint
        }

        fun namePaint(colorName: CustomColor, unitF: Float, isDualMode: Boolean, font: Typeface): Paint {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = if (isDualMode) { unitF * 14f } else { unitF * 18f }
            paint.typeface = font

            when (colorName) {
                CustomColor.RED -> paint.color = Color.parseColor("#FE9C94")
                CustomColor.YELLOW -> paint.color = Color.parseColor("#FDE293")
                CustomColor.GREEN -> paint.color = Color.parseColor("#7FD892")
                CustomColor.BLUE -> paint.color = Color.parseColor("#A1BBE5")
                CustomColor.PURPLE -> paint.color = Color.parseColor("#CF94FE")
            }

            return paint
        }

        fun arrowBoxPaint(colorName: CustomColor?, unitF: Float): Paint {
            return if (colorName != null) {
                val paint = iconPaint(colorName)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = unitF
                paint
            } else {
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#33FFFFFF")
                    style = Paint.Style.FILL
                }
            }
        }

        fun arrowPaint(colorName: CustomColor): Paint {
            val color = when (colorName) {
                CustomColor.RED -> Color.parseColor("#EE675C")
                CustomColor.YELLOW -> Color.parseColor("#FDD663")
                CustomColor.GREEN -> Color.parseColor("#2AAB46")
                CustomColor.BLUE -> Color.parseColor("#1B6BD5")
                CustomColor.PURPLE -> Color.parseColor("#AE5CEE")
            }
            val filter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            return Paint(Paint.ANTI_ALIAS_FLAG).apply {
                colorFilter = filter
            }
        }

        fun differenceTextPaint(unitF: Float, font: Typeface, colorName: CustomColor?): Paint {
            val paint = if (colorName != null) {
                iconPaint(colorName)
            } else {
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#BDC1C6")
                }
            }
            paint.typeface = font
            paint.textSize = unitF * 16f
            return paint
        }

        fun bloodGlucoseTextPaint(unitF: Float, isDualMode: Boolean, font: Typeface): Paint {
            return Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                typeface = font
                textSize = if (isDualMode) { unitF * 40f } else { unitF * 56f }
                textAlign = Paint.Align.CENTER
            }
        }
    }
}