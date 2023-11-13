package com.example.k_dual.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import com.example.k_dual.R
import com.example.k_dual.presentation.components.UserItem
import com.example.k_dual.presentation.theme.CustomColor
import com.example.k_dual.presentation.theme.KDualTheme

@Composable
fun HomePage(
    navigateToGraphPage: (userId: Number) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "icon",
            modifier = Modifier
                .padding(top = 7.dp)
                .size(24.dp)
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserItem(
                modifier = Modifier
                    .height(52.dp)
                    .width(172.dp)
                    .clickable { navigateToGraphPage(1) },
                name = "Minha",
                color = CustomColor.YELLOW,
                server = "Libre",
                isAlertOn = true)
            UserItem(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(52.dp)
                    .width(172.dp)
                    .clickable { navigateToGraphPage(2) },
                name = "Jaewon",
                color = CustomColor.BLUE,
                server = "Dexcom",
                isAlertOn = false)
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun HomePagePreview() {
    fun test(num: Number) {
        println("hi")
    }
    KDualTheme {
        HomePage(navigateToGraphPage = { num -> test(num) } )
    }
}