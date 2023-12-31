package com.kaist.k_dual.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaist.k_dual.R
import com.kaist.k_dual.component.Divider
import com.kaist.k_dual.component.MultipleRowWhiteBox
import com.kaist.k_dual.component.SingleRowWhiteBox
import com.kaist.k_dual.component.Toggle
import com.kaist.k_dual.component.ToggleState
import com.kaist.k_dual.component.WatchFacePreview
import com.kaist.k_dual.model.ManageSetting
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun HomeScreen(navController: NavController, onSendMessageFailed: () -> Unit) {
    val firstUserName = ManageSetting.settings.firstUserSetting.name
    val secondUserName = ManageSetting.settings.secondUserSetting.name
    val units = ManageSetting.settings.glucoseUnits
    val enabledDualMode = ManageSetting.settings.enableDualMode

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WatchFacePreview(
            modifier = Modifier.padding(top = 24.dp),
            isFirstHighLight = false,
            isSecondHighLight = false
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val onClickToggle = {
                ManageSetting.saveSettings(
                    settings = ManageSetting.settings.copy(enableDualMode = !enabledDualMode),
                    context = context,
                    onSendMessageFailed = onSendMessageFailed
                )
            }
            Text(
                text = stringResource(R.string.user_mode),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            SingleRowWhiteBox(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .clickable { onClickToggle() }) {
                Text(
                    text = stringResource(R.string.enabled_dual_mode),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Toggle(
                    state = if (enabledDualMode) ToggleState.Right else ToggleState.Left,
                    onChange = { onClickToggle() },
                )
            }

            if (!enabledDualMode) {
                SingleRowWhiteBox(modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .clickable {
                        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                            navController.navigate("user/1")
                        }
                    }) {
                    Text(
                        text = stringResource(R.string.first_user),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = firstUserName,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else {
                MultipleRowWhiteBox {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                    navController.navigate("user/1")
                                }
                            }
                            .padding(horizontal = 36.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.first_user),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = firstUserName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 36.dp)
                    )
                    Row(
                        modifier = Modifier
                            .clickable {
                                if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                    navController.navigate("user/2")
                                }
                            }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.second_user),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = secondUserName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.setting),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            SingleRowWhiteBox(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .clickable {
                        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                            navController.navigate("unit")
                        }
                    }) {
                Text(
                    text = stringResource(R.string.blood_glucose_units),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = units.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF454545)
                )
            }
        }
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { HomeScreen(navController = navController, onSendMessageFailed = {}) }
    }
}
