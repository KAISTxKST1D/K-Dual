package com.kaist.k_dual.presentation

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.kaist.k_canvas.KCanvas

@Composable
fun SetupInfoPage() {
    val context = LocalContext.current
    val robotoMedium = Typeface.createFromAsset(context.assets, "Roboto-Medium.ttf")

    Canvas(
        modifier = Modifier.fillMaxSize()) {
        drawIntoCanvas {
            val canvas = it.nativeCanvas
            KCanvas.drawBackgroundBox(canvas, null, false, null)
            KCanvas.drawSetupInfo(canvas, context, robotoMedium)
        }
    }
}

@Preview
@Composable
fun SetupInfoPagePreview() {
    SetupInfoPage()
}