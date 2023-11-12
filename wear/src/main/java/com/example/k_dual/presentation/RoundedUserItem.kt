package com.example.k_dual.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.k_dual.R
import com.example.k_dual.presentation.theme.KDualTheme

@Composable
fun RoundedUserItem(modifier: Modifier, name: String, server: String, isAlertOn: Boolean) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF202124), RoundedCornerShape(92.129.dp))
            .padding(14.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = modifier
                .padding(end = 12.dp)
                .height(28.dp)
                .width(26.dp),
            painter = painterResource(id = R.drawable.waterdrop_outline),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.Black))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.title1
            )
            Text(
                text = "$server • Alert ${if (isAlertOn) "ON" else "OFF"}",
                style = MaterialTheme.typography.title2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedUserItemPreview() {
    KDualTheme {
        RoundedUserItem(modifier = Modifier, "Minha", "Libre", true)
    }
}