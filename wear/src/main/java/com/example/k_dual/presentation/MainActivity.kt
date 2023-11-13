package com.example.k_dual.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.k_dual.presentation.theme.KDualTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

//TODO. 반응형으로 제작

@Composable
fun WearApp() {
    val navController = rememberNavController()
    KDualTheme {
        NavHost(
            navController = navController,
            startDestination = "home",
            ) {
            composable("home") {
                HomePage(
                    navigateToGraphPage = { userId: Number -> navController.navigate("graph/${userId}") }
                )
            }
            composable("graph/{userId}") {
                GraphPage()
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultLargePreview() {
    WearApp()
}