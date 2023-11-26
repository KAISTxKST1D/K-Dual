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
import com.kaist.k_dual.model.ManageSetting
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun WatchFacePreview(
    modifier: Modifier = Modifier,
    isFirstHighLight: Boolean,
    isSecondHighLight: Boolean
) {
    Box(
        modifier = modifier
            .width(192.dp)
            .height(192.dp)
            .background(color = Color.Black, shape = CircleShape)
    ) {
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

                val firstUserColor = ManageSetting.settings.firstUserSetting.color
                val secondUserColor = ManageSetting.settings.secondUserSetting.color

                if (ManageSetting.settings.enableDualMode) {
                    KCanvas.drawBackgroundBox(canvas, "up", isFirstHighLight, firstUserColor)
                    KCanvas.drawIconAndUserName(
                        canvas,
                        1,
                        ManageSetting.settings.firstUserSetting.name,
                        firstUserColor,
                        robotoMedium
                    )
                    KCanvas.drawDiffArrowBox(
                        canvas,
                        context,
                        1,
                        isFirstHighLight,
                        firstUserColor,
                        "0",
                        ManageSetting.settings.glucoseUnits,
                        robotoRegular
                    )
                    KCanvas.drawBloodGlucose(
                        canvas,
                        1,
                        "000",
                        ManageSetting.settings.glucoseUnits,
                        robotoMedium
                    )
                    KCanvas.drawBackgroundBox(canvas, "down", isSecondHighLight, secondUserColor)
                    KCanvas.drawIconAndUserName(
                        canvas,
                        2,
                        ManageSetting.settings.secondUserSetting.name,
                        secondUserColor,
                        robotoMedium
                    )
                    KCanvas.drawDiffArrowBox(
                        canvas,
                        context,
                        2,
                        isSecondHighLight,
                        secondUserColor,
                        "0",
                        ManageSetting.settings.glucoseUnits,
                        robotoRegular
                    )
                    KCanvas.drawBloodGlucose(
                        canvas,
                        2,
                        "000",
                        ManageSetting.settings.glucoseUnits,
                        robotoMedium
                    )
                } else {
                    KCanvas.drawBackgroundBox(canvas, null, isFirstHighLight, firstUserColor)
                    KCanvas.drawIconAndUserName(
                        canvas,
                        null,
                        ManageSetting.settings.firstUserSetting.name,
                        firstUserColor,
                        robotoMedium
                    )
                    KCanvas.drawBloodGlucose(
                        canvas,
                        null,
                        "000",
                        ManageSetting.settings.glucoseUnits,
                        robotoMedium
                    )
                    KCanvas.drawDiffArrowBox(
                        canvas,
                        context,
                        null,
                        isFirstHighLight,
                        firstUserColor,
                        "0",
                        ManageSetting.settings.glucoseUnits,
                        robotoRegular
                    )
                }
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