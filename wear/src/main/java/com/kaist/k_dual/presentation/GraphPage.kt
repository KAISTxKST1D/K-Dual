package com.kaist.k_dual.presentation

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kaist.k_dual.R
import com.kaist.k_dual.presentation.theme.KDualTheme
import com.patrykandpatrick.vico.compose.axis.axisGuidelineComponent
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.kaist.k_canvas.KCanvas
import com.kaist.k_canvas.KColor

@Composable
fun GraphPage(isFirst: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
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
                KCanvas.drawIconAndUserName(canvas, 1, isFirst.toString(), KColor.YELLOW, robotoMedium)
                KCanvas.drawDiffArrowBox(canvas, context, 1, false, null, 4, robotoRegular)
                KCanvas.drawBloodGlucose(canvas, 1, 144, robotoMedium)
            }
        }

        val chartEntryModel: ChartEntryModel = entryModelOf(80, 100, 77, 90, 100, 90, 80, 100, 77, 90, 100, 90)

        Chart(
            modifier = Modifier
                .padding(bottom = 30.dp, start = 25.dp, end = 26.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter),
            chart = lineChart(
                currentChartStyle.lineChart.lines.map {
                        defaultLines -> defaultLines.copy(
                    pointConnector = DefaultPointConnector(cubicStrength = 0f),
                    lineBackgroundShader = null,
                    lineColor = 0xFFFDD663.toInt(),
                    lineThicknessDp = 2.5f)
                },
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minY = 50f,
                    maxY = 200f,
                ),
                spacing = 18.dp,
                targetVerticalAxisPosition = AxisPosition.Vertical.End
            ),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                label = rememberStartAxisLabel(),
                axis = lineComponent(
                    color = Color(0x33FFFFFF),
                    thickness = 0.5.dp,
                    ),
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                guideline = axisGuidelineComponent(
                    thickness = 0.5.dp,
                    color = Color(0x33FFFFFF),
                ),
                tickLength = 0.dp),
            bottomAxis = rememberBottomAxis(
                label = null,
                guideline = axisGuidelineComponent(
                    thickness = 0.5.dp,
                    color = Color(0x33FFFFFF)
                ),
                tickLength = 0.dp),
            fadingEdges = rememberFadingEdges(),
            chartScrollState = rememberChartScrollState()
        )
        Column (
            modifier = Modifier
                .padding(start = 12.dp, bottom = 30.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .padding(top = 2.dp, bottom = 2.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StartAxisLabelBox(
                modifier = Modifier.padding(start = 0.dp),
                startLabel = "200")
            StartAxisLabelBox(
                modifier = Modifier.padding(start = 3.dp),
                startLabel = "150")
            StartAxisLabelBox(
                modifier = Modifier.padding(start = 12.dp),
                startLabel = "100")
            StartAxisLabelBox(
                modifier = Modifier.padding(start = 27.dp),
                startLabel = "50")
        }
    }
}

@Composable
fun StartAxisLabelBox(
    modifier: Modifier,
    startLabel: String
) {
    Box(
        modifier = modifier
            .background(color = Color(0xFF414141), RoundedCornerShape(4.dp))
            .padding(3.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = startLabel,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.wrapContentSize(unbounded = true)
        )
    }
}

@Composable
fun rememberStartAxisLabel() = axisLabelComponent(
    lineCount = 4,
    textSize = 0.sp,
    verticalMargin = 4.5.dp
)

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun GraphPagePreview() {
    KDualTheme {
        GraphPage(true)
    }
}

@Preview(showBackground = false)
@Composable
fun StartAxisLabelBoxPreview() {
    KDualTheme {
        StartAxisLabelBox(Modifier, "300")
    }
}