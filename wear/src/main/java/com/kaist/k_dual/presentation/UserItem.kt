package com.kaist.k_dual.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kaist.k_dual.R
import com.kaist.k_dual.presentation.theme.Colors
import com.kaist.k_dual.presentation.theme.KDualTheme
import com.kaist.k_canvas.KColor

@Composable
fun UserItem(modifier: Modifier, name: String, color: KColor, server: String, isAlertOn: Boolean) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(92.129.dp))
            .background(Color(0xFF202124))
            .padding(14.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .height(28.dp)
                .width(26.dp),
            painter = painterResource(id = R.drawable.waterdrop_outline),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Colors.icon(color))
        )
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.title1,
                color = Colors.name(color)
            )
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = "$server • Alert ${if (isAlertOn) "ON" else "OFF"}",
                style = MaterialTheme.typography.title2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    KDualTheme {
        UserItem(modifier = Modifier, "Minha", KColor.YELLOW, "Libre", true)
    }
}