package com.kaist.k_dual.presentation

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import com.kaist.k_canvas.KCanvas
import com.kaist.k_dual.R
import com.kaist.k_dual.presentation.theme.KDualTheme
import com.kaist.k_canvas.KColor

@Composable
fun HomePage(
    navController: NavController
) {
    val context = LocalContext.current
    val robotoMedium = Typeface.createFromAsset(context.assets, "Roboto-Medium.ttf")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (UseSetting.settings == null) {
            Canvas(modifier = Modifier) {
                drawIntoCanvas {
                    val canvas = it.nativeCanvas
                    KCanvas.drawBackgroundBox(canvas, null, false, null)
                    KCanvas.drawSetupInfo(canvas, context, robotoMedium)
                }
            }
        } else {
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
                val settings = UseSetting.settings
                settings?.let {
                    UserItem(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(92.129.dp))
                            .clickable {
                                if (navController.currentDestination != NavDestination("graph/${1}")) {
                                    navController.navigate("graph/${1}")
                                }
                            },
                        name = settings.firstUserSetting.name,
                        color = settings.firstUserSetting.color,
                        server = settings.firstUserSetting.deviceType.label,
                        isAlertOn = settings.firstUserSetting.colorBlinkEnabled || settings.firstUserSetting.vibrationEnabled
                    )
                    UserItem(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 10.dp, end = 10.dp)
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(92.129.dp))
                            .clickable {
                                if (navController.currentDestination != NavDestination("graph/${2}")) {
                                    navController.navigate("graph/${2}")
                                }
                            },
                        name = settings.secondUserSetting.name,
                        color = settings.secondUserSetting.color,
                        server = settings.secondUserSetting.deviceType.label,
                        isAlertOn = settings.secondUserSetting.colorBlinkEnabled || settings.secondUserSetting.vibrationEnabled
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun HomePagePreview() {
    KDualTheme {
        HomePage(navController = rememberNavController())
    }
}