package com.kaist.k_dual.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaist.k_dual.component.BackButtonTitleRow
import com.kaist.k_dual.component.Divider
import com.kaist.k_dual.component.MultipleRowWhiteBox
import com.kaist.k_dual.component.OutlinedInputParameters
import com.kaist.k_dual.component.TextFieldAlertDialog
import com.kaist.k_dual.component.Toggle
import com.kaist.k_dual.component.ToggleState
import com.kaist.k_dual.model.ManageSetting
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun AlertScreen(navController: NavController, isFirst: Boolean, onSendMessageFailed: () -> Unit) {
    val context = LocalContext.current
    val userSetting =
        if (isFirst) ManageSetting.settings.firstUserSetting else ManageSetting.settings.secondUserSetting

    var isLowValueDialogOpen by remember { mutableStateOf(false) }
    var isHighValueDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButtonTitleRow(
            navController = navController,
            title = (if (isFirst) "First User" else "Second User") + " - Alert"
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Alert Setting",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                MultipleRowWhiteBox {
                    val onClickVibration = {
                        if (isFirst) {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    firstUserSetting = userSetting.copy(vibrationEnabled = !userSetting.vibrationEnabled)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        } else {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    secondUserSetting = userSetting.copy(vibrationEnabled = !userSetting.vibrationEnabled)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                    }
                    val onClickColorBlink = {
                        if (isFirst) {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    firstUserSetting = userSetting.copy(colorBlinkEnabled = !userSetting.colorBlinkEnabled)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        } else {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    secondUserSetting = userSetting.copy(colorBlinkEnabled = !userSetting.colorBlinkEnabled)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clickable { onClickVibration() }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Vibration",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Toggle(
                            state = if (userSetting.vibrationEnabled) ToggleState.Right else ToggleState.Left,
                            onChange = { onClickVibration() },
                        )
                    }
                    Divider(modifier = Modifier.padding(horizontal = 36.dp))
                    Row(
                        modifier = Modifier
                            .clickable { onClickColorBlink() }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Color Blink",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Toggle(
                            state = if (userSetting.colorBlinkEnabled) ToggleState.Right else ToggleState.Left,
                            onChange = { onClickColorBlink() },
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Value Setting",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                MultipleRowWhiteBox {
                    val onChangeLowValue = { lowValue: Int ->
                        if (isFirst) {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    firstUserSetting = userSetting.copy(lowValue = lowValue)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        } else {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    secondUserSetting = userSetting.copy(lowValue = lowValue)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                    }
                    val onChangeHighValue = { highValue: Int ->
                        if (isFirst) {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    firstUserSetting = userSetting.copy(highValue = highValue)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        } else {
                            ManageSetting.saveSettings(
                                settings = ManageSetting.settings.copy(
                                    secondUserSetting = userSetting.copy(highValue = highValue)
                                ),
                                context = context,
                                onSendMessageFailed = onSendMessageFailed
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clickable { isLowValueDialogOpen = true }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Low Value",
                            style = MaterialTheme.typography.bodyLarge,

                            )
                        Text(
                            text = userSetting.lowValue.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                        TextFieldAlertDialog(
                            isOpen = isLowValueDialogOpen,
                            onConfirm = {
                                onChangeLowValue(it.toInt())
                                isLowValueDialogOpen = false
                            },
                            onDismiss = { isLowValueDialogOpen = false },
                            title = "Low Value",
                            description = "Enter the low value of blood glucose to receive vibration alert.",
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = "Enter the glucose value",
                                suffix = ManageSetting.settings.glucoseUnits.label,
                                label = "Low Value"
                                // TODO. Allow only int value
                            )
                        )
                    }
                    Divider(modifier = Modifier.padding(horizontal = 36.dp))
                    Row(
                        modifier = Modifier
                            .clickable { isHighValueDialogOpen = true }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "High Value",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = userSetting.highValue.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                        TextFieldAlertDialog(
                            isOpen = isHighValueDialogOpen,
                            onConfirm = {
                                onChangeHighValue(it.toInt())
                                isHighValueDialogOpen = false
                            },
                            onDismiss = { isHighValueDialogOpen = false },
                            title = "High Value",
                            description = "Enter the high value of blood glucose to receive vibration alert.",
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = "Enter the glucose value",
                                suffix = ManageSetting.settings.glucoseUnits.label,
                                label = "High Value",
                                // TODO. Allow only int value
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun AlertScreenPreview() {
    val navController = rememberNavController()
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { AlertScreen(navController = navController, isFirst = true, onSendMessageFailed = {}) }
    }
}
