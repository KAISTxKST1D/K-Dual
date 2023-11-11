package com.example.k_dual.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.k_dual.component.BackButtonTitleRow
import com.example.k_dual.component.Divider
import com.example.k_dual.component.MultipleRowWhiteBox
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun UnitScreen(navController: NavController) {
    val units = arrayOf("mg/dL", "mmol/L")
    var selectedUnitIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        BackButtonTitleRow(navController = navController, title = "Blood Glucose Units")
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Units",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF454545),
                modifier = Modifier.padding(start = 24.dp)
            )
            MultipleRowWhiteBox {
                units.mapIndexed { index, unit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .size(24.dp),
                            selected = selectedUnitIndex == index,
                            onClick = { selectedUnitIndex = index },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                            )
                        )
                        Text(
                            text = unit,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF454545),
                        )
                    }
                    if (index != units.lastIndex) Divider()
                }
            }
        }
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun UnitScreenPreview() {
    val navController = rememberNavController();
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { UnitScreen(navController) }
    }
}