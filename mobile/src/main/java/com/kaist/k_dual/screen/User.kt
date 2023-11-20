package com.kaist.k_dual.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kaist.k_dual.component.BackButtonTitleRow
import com.kaist.k_dual.component.Divider
import com.kaist.k_dual.component.MultipleRowWhiteBox
import com.kaist.k_dual.component.OutlinedInputParameters
import com.kaist.k_dual.component.SingleRowWhiteBox
import com.kaist.k_dual.component.TextFieldAlertDialog
import com.kaist.k_dual.component.TwoTextFieldsAlertDialog
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun UserScreen(
    navController: NavController,
    isFirst: Boolean,
    onSendMessageFailed: () -> Unit = {}
) {
    val name = "-"
    val nightscoutUrl = "-"
    val color = "Yellow"
    val alert = "On / On / 70-180"
    val deviceTypes = arrayOf("Nightscout", "Dexcom")
    var selectedDeviceTypeIndex by remember { mutableIntStateOf(0) }

    var isNameDialogOpen by remember { mutableStateOf(false) }
    var isNightscoutURLDialogOpen by remember { mutableStateOf(false) }
    var isDexcomURLDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        BackButtonTitleRow(
            navController = navController,
            title = if (isFirst) "First User" else "Second User"
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "General Setting",
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
                        text = "Name",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF454545),
                    )
                    TextFieldAlertDialog(
                        isOpen = isNameDialogOpen,
                        onConfirm = { isNameDialogOpen = false },
                        onDismiss = { isNameDialogOpen = false },
                        title = "Name",
                        description = "Enter the name of the first user.\n" +
                                "(Minimum 1 character, maximum 10 characters limit.)",
                        outlinedInputParameters = OutlinedInputParameters(
                            placeholder = "Enter Name",
                            label = "Name",
                            maxLength = 10,
                        )
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 36.dp))
                Row(
                    modifier = Modifier
                        .clickable { navController.navigate("user/${if (isFirst) 1 else 2}/color") }
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = color,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF454545),
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 36.dp))
                Row(
                    modifier = Modifier
                        .clickable { navController.navigate("user/${if (isFirst) 1 else 2}/alert") }
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Alert",
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
                text = "Device Type",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(start = 24.dp)
            )
            MultipleRowWhiteBox {
                deviceTypes.mapIndexed { index, deviceType ->
                    Row(
                        modifier = Modifier
                            .clickable { selectedDeviceTypeIndex = index }
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 36.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .size(24.dp),
                            selected = selectedDeviceTypeIndex == index,
                            onClick = { selectedDeviceTypeIndex = index },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                            )
                        )
                        Text(
                            text = deviceType,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    if (index != deviceTypes.lastIndex) Divider(
                        modifier = Modifier.padding(
                            horizontal = 36.dp
                        )
                    )
                }
            }
            if (selectedDeviceTypeIndex == 0) {
                SingleRowWhiteBox(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable { isNightscoutURLDialogOpen = true }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Nightscout URL",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = nightscoutUrl,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545),
                        )
                        TextFieldAlertDialog(
                            isOpen = isNightscoutURLDialogOpen,
                            onConfirm = { isNightscoutURLDialogOpen = false },
                            onDismiss = { isNightscoutURLDialogOpen = false },
                            title = "Nightscout URL",
                            description = "Enter the website link to retrieve the first user's blood glucose data.",
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = "Enter or paste URL",
                                label = "URL",
                            )
                        )
                    }
                }
            } else if (selectedDeviceTypeIndex == 1) {
                SingleRowWhiteBox(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable { isDexcomURLDialogOpen = true }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Dexcom Address",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = nightscoutUrl,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545),
                        )
                        TwoTextFieldsAlertDialog(
                            isOpen = isDexcomURLDialogOpen,
                            onConfirm = { _, _ -> isDexcomURLDialogOpen = false },
                            onDismiss = { isDexcomURLDialogOpen = false },
                            title = "Dexcom Address",
                            description = "Enter the Dexcom ID and Password to retrieve the first user's blood glucose data.",
                            outlinedInputParameters1 = OutlinedInputParameters(
                                placeholder = "ex. user123",
                                label = "ID",
                            ),
                            outlinedInputParameters2 = OutlinedInputParameters(
                                placeholder = "ex. 12345678",
                                label = "Password",
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