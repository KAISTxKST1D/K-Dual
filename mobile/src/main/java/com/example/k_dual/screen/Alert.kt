package com.example.k_dual.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.k_dual.R
import com.example.k_dual.component.Divider
import com.example.k_dual.component.MultipleRowWhiteBox
import com.example.k_dual.component.SingleRowWhiteBox
import com.example.k_dual.component.Toggle
import com.example.k_dual.component.ToggleState
import com.example.k_dual.component.WatchFacePreview
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun AlertScreen(navController: NavController, isFirst: Boolean) {
    val isVibrationEnabled = remember { mutableStateOf<ToggleState>(ToggleState.Left) }
    val isVisualEnabled = remember { mutableStateOf<ToggleState>(ToggleState.Left) }
    val lowValue by remember { mutableIntStateOf(70) }
    val highValue by remember { mutableIntStateOf(110) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                contentDescription = "Back arrow icon",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Text(
                text = if (isFirst) "First User" else "Second User",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF454545)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Connected Devices",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                SingleRowWhiteBox {
                    Text(
                        text = "Enabled dual mode",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Toggle(
                        state = isVibrationEnabled.value,
                        onChange = {
                            isVibrationEnabled.value = it
                        },
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Visual Alert Setting",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                SingleRowWhiteBox {
                    Text(
                        text = "Enabled",
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
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .clickable { navController.navigate("user/1") },
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
                    }
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .clickable { navController.navigate("user/2") },
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
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun AlertScreenPreview() {
    val navController = rememberNavController();
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { AlertScreen(navController, isFirst = true) }
    }
}
