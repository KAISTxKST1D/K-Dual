package com.kaist.k_dual.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import com.kaist.k_canvas.KColor

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

// TODO. merge to K-canvas library
object Colors {
    fun icon(color: KColor): Color {
        return when (color) {
            KColor.RED -> Color(0xFFEE675C)
            KColor.YELLOW -> Color(0xFFFDD663)
            KColor.GREEN -> Color(0xFF2AAB46)
            KColor.BLUE -> Color(0xFF1B6BD5)
            KColor.PURPLE -> Color(0xFFAE5CEE)
        }
    }

    fun name(color: KColor): Color {
        return when (color) {
            KColor.RED -> Color(0xFFFE9C94)
            KColor.YELLOW -> Color(0xFFFDE293)
            KColor.GREEN -> Color(0xFF7FD892)
            KColor.BLUE -> Color(0xFFA1BBE5)
            KColor.PURPLE -> Color(0xFFCF94FE)
        }
    }
}