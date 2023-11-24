package com.kaist.k_dual.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import com.kaist.k_dual.R
import com.kaist.k_dual.presentation.theme.KDualTheme
import com.kaist.k_canvas.KColor

@Composable
fun HomePage(
    navController: NavController
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
            // TODO. navigate 화면 전환 animation
            UserItem(
                modifier = Modifier
                    .height(52.dp)
                    .width(172.dp)
                    .clip(RoundedCornerShape(92.129.dp))
                    .clickable {
                        if (navController.currentDestination != NavDestination("graph/${1}")) {
                            navController.navigate("graph/${1}")
                        } },
                name = "Minha",
                color = KColor.YELLOW,
                server = "Libre",
                isAlertOn = true)
            UserItem(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(52.dp)
                    .width(172.dp)
                    .clip(RoundedCornerShape(92.129.dp))
                    .clickable {
                        if (navController.currentDestination != NavDestination("graph/${2}")) {
                            navController.navigate("graph/${2}")
                        } },
                name = "Jaewon",
                color = KColor.BLUE,
                server = "Dexcom",
                isAlertOn = false)
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun HomePagePreview() {
    KDualTheme {
        HomePage(navController = rememberNavController())
    }
}