package com.example.k_dual.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.k_dual.R
import com.example.k_dual.component.Divider
import com.example.k_dual.component.MultipleRowWhiteBox
import com.example.k_dual.component.OutlinedInputParameters
import com.example.k_dual.component.TextFieldAlertDialog
import com.example.k_dual.ui.theme.KDualTheme

@Composable
fun UserScreen(navController: NavController, isFirst: Boolean) {
    val name = "-"
    val url = "-"
    val color = "Yellow"
    val alert = "On / On / 70-180"

    var isNameDialogOpen by remember { mutableStateOf(false) }
    var isURLDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
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
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .clickable { isNameDialogOpen = true },
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
                            placeholder = "Enter Name", suffix = null
                        )
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .clickable { isURLDialogOpen = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "URL",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = url,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF454545),
                    )
                    TextFieldAlertDialog(
                        isOpen = isURLDialogOpen,
                        onConfirm = { isURLDialogOpen = false },
                        onDismiss = { isURLDialogOpen = false },
                        title = "URL",
                        description = "Enter the website link to retrieve the first user's blood glucose data.",
                        outlinedInputParameters = OutlinedInputParameters(
                            placeholder = "Enter or paste URL", suffix = null
                        )
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
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
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
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

    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun UserScreenPreview() {
    val navController = rememberNavController();
    KDualTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) { UserScreen(navController, isFirst = true) }
    }
}