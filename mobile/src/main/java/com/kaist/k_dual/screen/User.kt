package com.kaist.k_dual.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaist.k_dual.component.BackButtonTitleRow
import com.kaist.k_dual.component.Divider
import com.kaist.k_dual.component.MultipleRowWhiteBox
import com.kaist.k_dual.component.OutlinedInputParameters
import com.kaist.k_dual.component.SingleRowWhiteBox
import com.kaist.k_dual.component.TextFieldAlertDialog
import com.kaist.k_dual.component.TwoTextFieldsAlertDialog
import com.kaist.k_canvas.DeviceType
import com.kaist.k_canvas.UserSetting
import com.kaist.k_dual.R
import com.kaist.k_dual.model.ManageSetting
import com.kaist.k_dual.ui.theme.KDualTheme
import com.kaist.k_dual.urlPattern

@Composable
fun UserScreen(
    navController: NavController,
    isFirst: Boolean,
    onSendMessageFailed: () -> Unit = {}
) {
    val context = LocalContext.current
    val userSetting =
        if (isFirst) ManageSetting.settings.firstUserSetting else ManageSetting.settings.secondUserSetting
    val alert =
        "${if (userSetting.vibrationEnabled) stringResource(id = R.string.on) else stringResource(id = R.string.off)} / ${
            if (userSetting.colorBlinkEnabled) stringResource(
                id = R.string.on
            ) else stringResource(id = R.string.off)
        } / ${userSetting.lowValue}-${userSetting.highValue}"

    var isNameDialogOpen by remember { mutableStateOf(false) }
    var isNightscoutURLDialogOpen by remember { mutableStateOf(false) }
    var isDexcomURLDialogOpen by remember { mutableStateOf(false) }

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
    ) {
        BackButtonTitleRow(
            navController = navController,
            title = if (isFirst) stringResource(R.string.first_user) else stringResource(R.string.second_user)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.general_setting),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(start = 24.dp)
            )
            MultipleRowWhiteBox {
                Row(
                    modifier = Modifier
                        .clickable { isNameDialogOpen = true }
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.name),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = userSetting.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF454545),
                    )
                    TextFieldAlertDialog(
                        isOpen = isNameDialogOpen,
                        onConfirm = {
                            saveUserSettings(userSetting.copy(name = it))
                            isNameDialogOpen = false
                        },
                        onDismiss = { isNameDialogOpen = false },
                        title = stringResource(R.string.name),
                        description = if (isFirst) {
                            stringResource(R.string.enter_the_name)
                        } else {
                            stringResource(R.string.enter_the_name2)
                        },
                        outlinedInputParameters = OutlinedInputParameters(
                            placeholder = stringResource(R.string.enter_name),
                            label = stringResource(R.string.name),
                            maxLength = 8,
                            allowEmpty = true,
                        )
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 36.dp))
                Row(
                    modifier = Modifier
                        .clickable {
                            if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate("user/${if (isFirst) 1 else 2}/color")
                            }
                        }
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.color),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = userSetting.color.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF454545),
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 36.dp))
                Row(
                    modifier = Modifier
                        .clickable {
                            if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate("user/${if (isFirst) 1 else 2}/alert")
                            }
                        }
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.alert),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = alert,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF454545),
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.device_type),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(start = 24.dp)
            )
            MultipleRowWhiteBox {
                val onClick = { deviceType: DeviceType ->
                    saveUserSettings(userSetting.copy(deviceType = deviceType))
                }
                DeviceType.values().mapIndexed { index, deviceType ->
                    Row(
                        modifier = Modifier
                            .clickable { onClick(deviceType) }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .size(24.dp),
                            selected = userSetting.deviceType == deviceType,
                            onClick = { onClick(deviceType) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                            )
                        )
                        Text(
                            text = deviceType.label,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    if (index != DeviceType.values().lastIndex) Divider(
                        modifier = Modifier.padding(
                            horizontal = 36.dp
                        )
                    )
                }
            }
            if (userSetting.deviceType == DeviceType.Nightscout) {
                SingleRowWhiteBox(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable { isNightscoutURLDialogOpen = true }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(R.string.nightscout_address),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = userSetting.nightscoutUrl.ifEmpty { "-" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        TextFieldAlertDialog(
                            isOpen = isNightscoutURLDialogOpen,
                            onConfirm = {
                                saveUserSettings(userSetting.copy(nightscoutUrl = it))
                                isNightscoutURLDialogOpen = false
                            },
                            onDismiss = { isNightscoutURLDialogOpen = false },
                            title = stringResource(R.string.nightscout_address),
                            description = if (isFirst) {
                                stringResource(R.string.enter_the_website_link)
                            } else {
                                stringResource(R.string.enter_the_website_link2)
                            },
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = stringResource(R.string.enter_or_paste_url),
                                label = stringResource(R.string.url),
                                validation = { urlPattern.matches(it) }
                            )
                        )
                    }
                }
            } else if (userSetting.deviceType == DeviceType.Dexcom) {
                SingleRowWhiteBox(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable { isDexcomURLDialogOpen = true }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(R.string.dexcom_account),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = userSetting.dexcomId.ifEmpty { "-" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        TwoTextFieldsAlertDialog(
                            isOpen = isDexcomURLDialogOpen,
                            onConfirm = { id, password ->
                                saveUserSettings(
                                    userSetting.copy(
                                        dexcomId = id,
                                        dexcomPassword = password
                                    )
                                )
                                isDexcomURLDialogOpen = false
                            },
                            onDismiss = { isDexcomURLDialogOpen = false },
                            title = stringResource(R.string.dexcom_account),
                            description = if (isFirst) {
                                stringResource(R.string.enter_the_dexcom_id)
                            } else {
                                stringResource(R.string.enter_the_dexcom_id2)
                            },
                            outlinedInputParameters1 = OutlinedInputParameters(
                                placeholder = stringResource(R.string.ex_user1),
                                label = stringResource(R.string.id),
                            ),
                            outlinedInputParameters2 = OutlinedInputParameters(
                                placeholder = stringResource(R.string.ex_user2),
                                label = stringResource(R.string.password),
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
fun UserScreenPreview() {
    val navController = rememberNavController()
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { UserScreen(navController, isFirst = true) }
    }
}