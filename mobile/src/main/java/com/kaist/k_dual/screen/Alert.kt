package com.kaist.k_dual.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaist.k_canvas.UserSetting
import com.kaist.k_dual.R
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

    fun saveUserSettings(updatedUserSetting: UserSetting) {
        val updatedSettings = if (isFirst) {
            ManageSetting.settings.copy(firstUserSetting = updatedUserSetting)
        } else {
            ManageSetting.settings.copy(secondUserSetting = updatedUserSetting)
        }
        ManageSetting.saveSettings(
            settings = updatedSettings,
            context = context,
            onSendMessageFailed = onSendMessageFailed
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButtonTitleRow(
            navController = navController,
            title = (if (isFirst) stringResource(R.string.first_user) else stringResource(R.string.second_user)) + stringResource(
                R.string.alert2
            )
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
                    text = stringResource(R.string.alert_setting),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                MultipleRowWhiteBox {
                    val onClickVibration = {
                        saveUserSettings(userSetting.copy(vibrationEnabled = !userSetting.vibrationEnabled))
                    }
                    val onClickColorBlink = {
                        saveUserSettings(userSetting.copy(colorBlinkEnabled = !userSetting.colorBlinkEnabled))
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
                            text = stringResource(R.string.vibration),
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
                            text = stringResource(R.string.color_blink),
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
                    text = stringResource(R.string.value_setting),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                MultipleRowWhiteBox {
                    val onChangeLowValue = { lowValue: Int ->
                        saveUserSettings(userSetting.copy(lowValue = lowValue))
                    }
                    val onChangeHighValue = { highValue: Int ->
                        saveUserSettings(userSetting.copy(highValue = highValue))
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
                            text = stringResource(R.string.low_value),
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
                                try {
                                    onChangeLowValue(it.toInt())
                                } catch (_: NumberFormatException) {
                                }
                                isLowValueDialogOpen = false
                            },
                            onDismiss = { isLowValueDialogOpen = false },
                            title = stringResource(R.string.low_value),
                            description = stringResource(R.string.enter_the_low_value),
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = stringResource(R.string.enter_the_glucose_value),
                                suffix = ManageSetting.settings.glucoseUnits.label,
                                label = stringResource(R.string.low_value),
                                initialValue = userSetting.lowValue.toString(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                validation = {
                                    try {
                                        it.toInt()
                                        true
                                    } catch (_: NumberFormatException) {
                                        false
                                    }
                                }
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
                            text = stringResource(R.string.high_value),
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
                                try {
                                    onChangeHighValue(it.toInt())
                                } catch (_: NumberFormatException) {
                                }

                                isHighValueDialogOpen = false
                            },
                            onDismiss = { isHighValueDialogOpen = false },
                            title = "High Value",
                            description = "Enter the high value of blood glucose to receive vibration alert.",
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = stringResource(R.string.enter_the_glucose_value),
                                suffix = ManageSetting.settings.glucoseUnits.label,
                                label = stringResource(R.string.high_value),
                                initialValue = userSetting.highValue.toString(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                validation = {
                                    try {
                                        it.toInt()
                                        true
                                    } catch (_: NumberFormatException) {
                                        false
                                    }
                                }
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
