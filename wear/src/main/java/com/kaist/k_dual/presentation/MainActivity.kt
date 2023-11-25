package com.kaist.k_dual.presentation

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.kaist.k_dual.presentation.theme.KDualTheme

class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        val userId = intent.getIntExtra("routeGraph", 0)
        setContent {
            UseSetting.initialize(LocalContext.current)
            WearApp(userId)
        }
    }
}

//TODO. 반응형으로 제작

@Composable
fun WearApp(userId: Int) {
    val navController = rememberSwipeDismissableNavController()
    KDualTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "home",
        ) {
            composable("home") {
                Box(modifier = Modifier.fillMaxSize()) {
                    HomePage(navController)
                }
            }
            composable(
                "graph/{userId}",
                arguments = listOf(
                    navArgument("userId") {
                        type = NavType.IntType
                    })
            ) {
                GraphPage(it.arguments?.getInt("userId") == 1)
            }
        }
    }
    if (userId != 0) navController.navigate("graph/${userId}")
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(0)
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultLargePreview() {
    WearApp(0)
}