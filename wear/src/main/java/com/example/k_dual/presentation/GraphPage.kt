package com.example.k_dual.presentation

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.k_dual.R
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kr.ac.kaist.k_canvas.KCanvas
import kr.ac.kaist.k_canvas.KColor

@Composable
fun GraphPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.graph_background),
            contentDescription = "background")

        val context = LocalContext.current
        val robotoMedium = Typeface.createFromAsset(context.assets, "Roboto-Medium.ttf")
        val robotoRegular = Typeface.createFromAsset(context.assets, "Roboto-Regular.ttf")

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawIntoCanvas {
                val canvas = it.nativeCanvas
                // TODO. show real time
                KCanvas.drawDigitalClock(canvas, 12, 33, robotoMedium)
                KCanvas.drawIconAndUserName(canvas, 1, "Minha", KColor.YELLOW, robotoMedium)
                KCanvas.drawDiffArrowBox(canvas, context, 1, false, null, 4, robotoRegular)
                KCanvas.drawBloodGlucose(canvas, 1, 144, robotoMedium)
            }
        }
    }
    val chartEntryModel = entryModelOf(4f, 12f, 8f, 16f)
//    Chart(
//        chart = lineChart(
//            currentChartStyle.lineChart.lines.map {
//                defaultLines -> defaultLines.copy(pointConnector = DefaultPointConnector(cubicStrength = 0f))
//            }
//        ),
//        model = chartEntryModel,
//        startAxis = rememberStartAxis(),
//        bottomAxis = rememberBottomAxis(label = null)
//    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun GraphPagePreview() {
    GraphPage()
}