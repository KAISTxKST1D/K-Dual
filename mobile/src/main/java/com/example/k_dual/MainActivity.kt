package com.example.k_dual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.k_dual.screen.HomeScreen
import com.example.k_dual.screen.UserScreen
import com.example.k_dual.ui.theme.KDualTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KDualTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeContentPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        modifier = Modifier,
                        navController = rememberNavController(),
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen()
                        }
                        composable(
                            "user/{index}",
                            arguments = listOf(
                                navArgument("index") {                                    // Make argument type safe
                                    type = NavType.IntType
                                }
                            ),
                        ) {entry ->
                            val index = entry.arguments?.getInt("index")
                            UserScreen(index == 1)
                        }
                    }
                }
            }
        }
    }
}

