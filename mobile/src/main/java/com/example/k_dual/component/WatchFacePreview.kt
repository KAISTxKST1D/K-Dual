package com.example.k_dual.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.k_dual.ui.theme.KDualTheme



@Composable
fun WatchFacePreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(192.dp)
            .height(192.dp)
            .background(color = Color.Black, shape = CircleShape)
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun WatchFacePreviewPreview() {
    KDualTheme {
        WatchFacePreview()
    }
}