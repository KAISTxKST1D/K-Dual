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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kaist.k_canvas.DeviceType
import com.kaist.k_canvas.GlucoseUnits
import com.kaist.k_dual.R
import com.kaist.k_dual.presentation.theme.KDualTheme
import com.patrykandpatrick.vico.compose.axis.axisGuidelineComponent
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
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
import com.kaist.k_dual.model.mgdlToMmol
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.kaist.k_dual.presentation.theme.Colors
import kotlinx.coroutines.isActive

@Composable
fun GraphPage(isFirst: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        val settings = UseSetting.settings ?: return

        Image(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.graph_background),
            contentDescription = "background"
        )

        val context = LocalContext.current
        val robotoMedium = Typeface.createFromAsset(context.assets, "Roboto-Medium.ttf")
        val robotoRegular = Typeface.createFromAsset(context.assets, "Roboto-Regular.ttf")

        val userSetting =
            if (isFirst) settings.firstUserSetting else settings.secondUserSetting

        val currentHour = remember { mutableStateOf(0) }
        val currentMinute = remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            while (isActive) {
                val date = Date()
                currentHour.value = SimpleDateFormat("HH", Locale.getDefault()).format(date).toInt()
                currentMinute.value =
                    SimpleDateFormat("mm", Locale.getDefault()).format(date).toInt()
                delay(1000)
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawIntoCanvas {
                val canvas = it.nativeCanvas
                KCanvas.drawDigitalClock(
                    canvas,
                    currentHour.value,
                    currentMinute.value,
                    robotoMedium
                )
                KCanvas.drawIconAndUserName(
                    canvas,
                    1,
                    userSetting.name,
                    userSetting.color,
                    robotoMedium
                )
                var currentBloodGlucoseDifference: String = ""
                if (isFirst) {
                    currentBloodGlucoseDifference = UseBloodGlucose.firstUserDiff
                } else {
                    currentBloodGlucoseDifference = UseBloodGlucose.secondUserDiff
                }
                KCanvas.drawDiffArrowBox(
                    canvas,
                    context,
                    1,
                    false,
                    null,
                    currentBloodGlucoseDifference,
                    robotoRegular
                )
                var currentBloodGlucose: String = ""
                if (isFirst) {
                    currentBloodGlucose = UseBloodGlucose.firstUser
                } else {
                    currentBloodGlucose = UseBloodGlucose.secondUser
                }
                KCanvas.drawBloodGlucose(canvas, 1, currentBloodGlucose, robotoMedium)
            }
        }

        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp // Width in dp
        var chartEntryModel: ChartEntryModel = entryModelOf(listOf())
        // TODO. Initialize graph when url changed
        if (isFirst) {
            when (settings.firstUserSetting.deviceType) {
                DeviceType.Nightscout -> {
                    val graphData = UseBloodGlucose.firstUserGraphNightScoutData
                    if (graphData.size == 36) {
                        when (settings.glucoseUnits) {
                            GlucoseUnits.mg_dL -> {
                                val graphValues = graphData.takeLast(36).map { it.sgv }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }

                            GlucoseUnits.mmol_L -> {
                                val graphValues = graphData.takeLast(36).map { mgdlToMmol (it.sgv) }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }
                        }
                    }
                }

                DeviceType.Dexcom -> {
                    val graphData = UseBloodGlucose.firstUserGraphDexcomData
                    if (graphData.size == 36) {
                        when (settings.glucoseUnits) {
                            GlucoseUnits.mg_dL -> {
                                val graphValues = graphData.takeLast(36).map { it.mgdl }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }

                            GlucoseUnits.mmol_L -> {
                                val graphValues = graphData.takeLast(36).map { it.mmol }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }
                        }
                    }
                }
            }
        } else {
            when (settings.secondUserSetting.deviceType) {
                DeviceType.Nightscout -> {
                    val graphData = UseBloodGlucose.secondUserGraphNightScoutData
                    if (graphData.size == 36) {
                        when (settings.glucoseUnits) {
                            GlucoseUnits.mg_dL -> {
                                val graphValues = graphData.takeLast(36).map { it.sgv }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }
                            GlucoseUnits.mmol_L -> {
                                val graphValues = graphData.takeLast(36).map { mgdlToMmol (it.sgv) }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }
                        }
                    }
                }

                DeviceType.Dexcom -> {
                    val graphData = UseBloodGlucose.secondUserGraphDexcomData
                    if (graphData.size == 36) {
                        when (settings.glucoseUnits) {
                            GlucoseUnits.mg_dL -> {
                                val graphValues = graphData.takeLast(36).map { it.mgdl }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }

                            GlucoseUnits.mmol_L -> {
                                val graphValues = graphData.takeLast(36).map { it.mmol }
                                chartEntryModel = entryModelOf(*graphValues.toTypedArray())
                            }
                        }
                    }
                }
            }
        }
        var miny = 0f
        var maxy = 300f
        if(settings.glucoseUnits==GlucoseUnits.mmol_L) {
            maxy = 15f
        }
        Chart(
            modifier = Modifier
                .padding(
                    bottom = 30.dp,
                    start = (screenWidthDp * 0.15).dp,
                    end = (screenWidthDp * 0.15).dp
                )
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter),
            chart = lineChart(
                currentChartStyle.lineChart.lines.map { defaultLines ->
                    defaultLines.copy(
                        pointConnector = DefaultPointConnector(cubicStrength = 0f),
                        lineBackgroundShader = null,
                        lineColor = Colors.icon(userSetting.color).hashCode(),
                        lineThicknessDp = 2.5f
                    )
                },
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minY = miny,
                    maxY = maxy,
                ),
                spacing = 18.dp,
                targetVerticalAxisPosition = AxisPosition.Vertical.End
            ),
            model = chartEntryModel,
            fadingEdges = rememberFadingEdges(),
            chartScrollSpec = rememberChartScrollSpec(
                isScrollEnabled = false
            )
        )
        var firstBox = "300"
        var secondBox = "200"
        var thirdBox = "100"
        var fourthBox = "0"
        if (settings.glucoseUnits == GlucoseUnits.mmol_L) {
            firstBox = "15"
            secondBox = "10"
            thirdBox = "5"
            fourthBox = "0"
        }
        // Fake chart to draw background grid
        val fakeChartEntryModel: ChartEntryModel = entryModelOf(0, 0, 0, 0, 0, 0, 0, 0)
        Chart(
            modifier = Modifier
                .padding(bottom = 30.dp, start = 26.dp, end = 26.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(bottomStartPercent = 40, bottomEndPercent = 40)),
            chart = lineChart(
                currentChartStyle.lineChart.lines.map { defaultLines ->
                    defaultLines.copy(
                        lineBackgroundShader = null,
                        lineColor = 0x00FFFFFF.toInt()
                    )
                },
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minY = miny,
                    maxY = maxy,
                ),
                spacing = 18.dp,
                targetVerticalAxisPosition = AxisPosition.Vertical.End
            ),
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
                tickLength = 0.dp,
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 4)
            ),
            bottomAxis = rememberBottomAxis(
                label = null,
                guideline = axisGuidelineComponent(
                    thickness = 0.5.dp,
                    color = Color(0x33FFFFFF)
                ),
                tickLength = 0.dp
            ),
            model = fakeChartEntryModel,
            fadingEdges = rememberFadingEdges(),
            chartScrollSpec = rememberChartScrollSpec(
                isScrollEnabled = false
            )
        )
        Column(
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
                startLabel = firstBox
            )
            StartAxisLabelBox(
                modifier = Modifier.padding(start = (screenWidthDp * 0.03).dp),
                startLabel = secondBox
            )
            StartAxisLabelBox(
                modifier = Modifier.padding(start = (screenWidthDp * 0.07).dp),
                startLabel = thirdBox
            )
            StartAxisLabelBox(
                modifier = Modifier.padding(start = (screenWidthDp * 0.15).dp),
                startLabel = fourthBox
            )
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

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
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