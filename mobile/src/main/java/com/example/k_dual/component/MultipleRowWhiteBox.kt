package com.example.k_dual.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun MultipleRowWhiteBox(modifier: Modifier = Modifier, content: @Composable() () -> Unit) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        content()
    }
}

@Preview(showBackground = false, showSystemUi = false)
@Composable
fun MultipleRowWhiteBoxPreview() {
    KDualTheme {
        MultipleRowWhiteBox {
            Text("hello")
            Divider()
            Text("bye")
            Divider()
            Text("bye")
            Divider()
            Text("bye")
            Divider()
            Text("bye")
        }
    }
}