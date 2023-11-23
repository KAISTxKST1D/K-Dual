package com.kaist.k_dual.component

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaist.k_canvas.KCanvas
import com.kaist.k_canvas.KColor
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun WatchFacePreview(
    modifier: Modifier = Modifier,
    isFirstHighLight: Boolean,
    isSecondHighLight: Boolean) {
    Box(
        modifier = modifier
            .width(192.dp)
            .height(192.dp)
            .background(color = Color.Black, shape = CircleShape)
    ){
        val context = LocalContext.current
        val robotoMedium = Typeface.createFromAsset(context.assets, "Roboto-Medium.ttf")
        val robotoRegular = Typeface.createFromAsset(context.assets, "Roboto-Regular.ttf")

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            drawIntoCanvas {
                val canvas = it.nativeCanvas
                KCanvas.drawDigitalClock(canvas, 12, 33, robotoMedium)
                KCanvas.drawRemainingBattery(canvas, 100, robotoMedium)

                KCanvas.drawBackgroundBox(canvas, "up", isFirstHighLight, KColor.YELLOW)
                KCanvas.drawIconAndUserName(canvas, 1, "Minha", KColor.YELLOW, robotoMedium)
                KCanvas.drawDiffArrowBox(canvas, context, 1, isFirstHighLight, KColor.YELLOW, 4, robotoRegular)
                KCanvas.drawBloodGlucose(canvas, 1, 144, robotoMedium)

                // dual모드인 것도 check
                KCanvas.drawBackgroundBox(canvas, "down", isSecondHighLight, KColor.BLUE)
                KCanvas.drawIconAndUserName(canvas, 2, "Jaewon", KColor.BLUE, robotoMedium)
                KCanvas.drawDiffArrowBox(canvas, context, 2, isSecondHighLight, KColor.BLUE, 4, robotoRegular)
                KCanvas.drawBloodGlucose(canvas, 2, 144, robotoMedium)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun WatchFacePreviewPreview() {
    KDualTheme {
        WatchFacePreview(isFirstHighLight = true, isSecondHighLight = false)
    }
}