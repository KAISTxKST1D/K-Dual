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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.kaist.k_dual.component.Toggle
import com.kaist.k_dual.component.ToggleState
import com.kaist.k_dual.ui.theme.KDualTheme

@Composable
fun AlertScreen(navController: NavController, isFirst: Boolean) {
    val isVibrationEnabled = remember { mutableStateOf<ToggleState>(ToggleState.Left) }
    val isVisualEnabled = remember { mutableStateOf<ToggleState>(ToggleState.Left) }
    val lowValue by remember { mutableIntStateOf(70) }
    val highValue by remember { mutableIntStateOf(110) }
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
                    Row(
                        modifier = Modifier
                            .clickable { isVibrationEnabled.value = !isVibrationEnabled.value }
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
                            state = isVibrationEnabled.value,
                            onChange = {
                                isVibrationEnabled.value = it
                            },
                        )
                    }
                    Divider(modifier = Modifier.padding(horizontal = 36.dp))
                    Row(
                        modifier = Modifier
                            .clickable { isVisualEnabled.value = !isVisualEnabled.value }
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
                            state = isVisualEnabled.value,
                            onChange = {
                                isVisualEnabled.value = it
                            },
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
                            text = lowValue.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                        TextFieldAlertDialog(
                            isOpen = isLowValueDialogOpen,
                            onConfirm = { isLowValueDialogOpen = false },
                            onDismiss = { isLowValueDialogOpen = false },
                            title = "Low Value",
                            description = "Enter the low value of blood glucose to receive vibration alert.",
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = "Enter the glucose value",
                                suffix = "mg/dL",
                                label = "Low Value"
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
                            text = highValue.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                        TextFieldAlertDialog(
                            isOpen = isHighValueDialogOpen,
                            onConfirm = { isHighValueDialogOpen = false },
                            onDismiss = { isHighValueDialogOpen = false },
                            title = "High Value",
                            description = "Enter the high value of blood glucose to receive vibration alert.",
                            outlinedInputParameters = OutlinedInputParameters(
                                placeholder = "Enter the glucose value",
                                suffix = "mg/dL",
                                label = "High Value",
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
        ) { AlertScreen(navController, isFirst = true) }
    }
}
