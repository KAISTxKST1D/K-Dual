package com.example.k_dual.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun DetailGraphPage() {
    val chartEntryModel = entryModelOf(4f, 12f, 8f, 16f)
    Chart(
        chart = lineChart(
            currentChartStyle.lineChart.lines.map {
                defaultLines -> defaultLines.copy(pointConnector = DefaultPointConnector(cubicStrength = 0f))
            }
        ),
        model = chartEntryModel,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(label = null)
    )
}

@Preview()
@Composable
fun DetailGraphPagePreview() {
    DetailGraphPage()
}