package com.example.k_dual.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Red400 = Color(0xFFCF6679)

internal val wearColorPalette: Colors = Colors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black
)
enum class CustomColor {
    RED, YELLOW, GREEN, BLUE, PURPLE
}

object Colors {
    fun icon(color: CustomColor): Color {
        return when (color) {
            CustomColor.RED -> TODO()
            CustomColor.YELLOW -> Color(0xFFFDD663)
            CustomColor.GREEN -> TODO()
            CustomColor.BLUE -> Color(0xFF1B6BD5)
            CustomColor.PURPLE -> TODO()
        }
    }

    fun name(color: CustomColor): Color {
        return when (color) {
            CustomColor.RED -> Color(0xFFFDE293)
            CustomColor.YELLOW -> Color(0xFFFDE293)
            CustomColor.GREEN -> TODO()
            CustomColor.BLUE -> Color(0xFFA1BBE5)
            CustomColor.PURPLE -> TODO()
        }
    }
}