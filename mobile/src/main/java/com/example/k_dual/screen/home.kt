package com.example.k_dual.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.k_dual.component.Divider
import com.example.k_dual.component.DoubleRowWhiteBox
import com.example.k_dual.component.SingleRowWhiteBox
import com.example.k_dual.component.Toggle
import com.example.k_dual.component.ToggleState
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun HomeScreen() {
    val firstUserName = "Minha"
    val secondUserName = "Jaewon"
    val language = "English"
    val units = "mg/dL"
    val navController = rememberNavController()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .width(192.dp)
                .height(192.dp)
                .background(color = Color.Black, shape = CircleShape)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val state = remember { mutableStateOf<ToggleState>(ToggleState.Right) }
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
                    state = state.value,
                    onChange = {
                        state.value = it
                    },
                )
            }

            if (state.value == ToggleState.Left) {
                SingleRowWhiteBox {
                    Text(
                        text = "First User",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = firstUserName,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else {
                DoubleRowWhiteBox {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("user/1") },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "First User",
                            style = MaterialTheme.typography.bodyLarge,

                            )
                        Text(
                            text = firstUserName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545)
                        )
                    }
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("user/2") },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Second User",
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
                text = "Language",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            SingleRowWhiteBox {
                Text(
                    text = "Language",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = language,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF454545)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Setting",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            SingleRowWhiteBox {
                Text(
                    text = "Blood Glucose Units",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = units,
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
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { HomeScreen() }
    }
}
