package com.kaist.k_dual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaist.k_dual.component.OpenWatchAppDialog
import com.kaist.k_dual.screen.AlertScreen
import com.kaist.k_dual.screen.ColorScreen
import com.kaist.k_dual.screen.HomeScreen
import com.kaist.k_dual.screen.UnitScreen
import com.kaist.k_dual.screen.UserScreen
import com.kaist.k_dual.ui.theme.KDualTheme
import com.google.android.gms.wearable.Wearable
import com.kaist.k_dual.model.ManageSetting
import findWearableNode

class MainActivity : ComponentActivity() {
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

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
                    var isDialogOpen by remember { mutableStateOf(false) }
                    var isConnected by remember { mutableStateOf(false) }
                    var userClosed by remember { mutableStateOf(false) }

                    val context = LocalContext.current

                    LaunchedEffect(Unit) {
                        findWearableNode(
                            capabilityClient = capabilityClient,
                            onSuccess = {
                                isConnected = true
                                userClosed = false
                                isDialogOpen = false
                                ManageSetting.initialize(
                                    context = context,
                                )
                            },
                            onFailure = {
                                if (!userClosed) {
                                    isDialogOpen = true
                                }
                                isConnected = false
                            }
                        )
                    }

                    OpenWatchAppDialog(
                        isOpen = isDialogOpen,
                        isConnected = isConnected,
                        onDismiss = {
                            userClosed = true
                            isDialogOpen = false
                        },
                    )

                    val navController = rememberNavController()

                    val onSendMessageFailed = {
                        userClosed = false
                        isDialogOpen = true
                        findWearableNode(
                            capabilityClient = capabilityClient,
                            onSuccess = {
                                isConnected = true
                                userClosed = false
                                isDialogOpen = false
                                ManageSetting.initialize(
                                    context = context,
                                )
                            },
                            onFailure = {
                                if (!userClosed) {
                                    isDialogOpen = true
                                }
                                isConnected = false
                            }
                        )
                    }

                    NavHost(
                        modifier = Modifier,
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                navController = navController,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                        composable("unit") {
                            UnitScreen(
                                navController = navController,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                        composable(
                            "user/{index}",
                            arguments = listOf(
                                navArgument("index") {
                                    type = NavType.IntType
                                }
                            ),
                        ) { backStackEntry ->
                            UserScreen(
                                navController = navController,
                                isFirst = backStackEntry.arguments?.getInt("index") == 1,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                        composable(
                            "user/{index}/color",
                            arguments = listOf(
                                navArgument("index") {
                                    type = NavType.IntType
                                }
                            ),
                        ) { backStackEntry ->
                            ColorScreen(
                                navController = navController,
                                isFirst = backStackEntry.arguments?.getInt("index") == 1,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                        composable(
                            "user/{index}/alert",
                            arguments = listOf(
                                navArgument("index") {
                                    type = NavType.IntType
                                }
                            ),
                        ) { backStackEntry ->
                            AlertScreen(
                                navController = navController,
                                isFirst = backStackEntry.arguments?.getInt("index") == 1,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                    }


                }
            }
        }
    }
}

