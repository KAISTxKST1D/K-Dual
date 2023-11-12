package com.example.k_dual.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun Divider(modifier: Modifier = Modifier, color: Color = Color(0xFFE6E6E6)) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = color),
    ) {
    }
}

@Preview(showBackground = false, showSystemUi = false)
@Composable
fun DividerPreview() {
    KDualTheme {
        Divider()
    }
}