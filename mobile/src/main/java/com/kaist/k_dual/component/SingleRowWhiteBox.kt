package com.kaist.k_dual.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun SingleRowWhiteBox(modifier: Modifier = Modifier, content: @Composable() () -> Unit) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .height(64.dp)
            .background(color = Color.White)
            .padding(horizontal = 36.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Preview(showBackground = false, showSystemUi = false)
@Composable
fun SingleRowWhiteBoxPreview() {
    KDualTheme {
        SingleRowWhiteBox() {
            Text("hello")
            Text("bye")
        }
    }
}