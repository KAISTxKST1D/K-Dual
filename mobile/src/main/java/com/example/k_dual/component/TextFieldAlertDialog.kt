package com.example.k_dual.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.k_dual.ui.theme.KDualTheme
import com.example.k_dual.ui.theme.RedUISolid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldAlertDialog(
    title: String, description: String, outlinedInputParameters: OutlinedInputParameters
) {
    var openDialog by remember { mutableStateOf(true) }
    var textValue by remember { mutableStateOf("") }

    val customColorScheme = lightColorScheme(
        surface = Color(0xFFFCECEC)
    )

    if (openDialog) {
        // Apply the custom theme to the AlertDialog
        MaterialTheme(colorScheme = customColorScheme) {
            AlertDialog(onDismissRequest = {
                openDialog = false
            }, title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }, text = {
                Column {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    OutlinedTextField(
                        modifier = Modifier.padding(vertical = 16.dp),
                        value = textValue,
                        onValueChange = {
                            textValue = it
                        },
                        trailingIcon = {
                            Text(outlinedInputParameters.suffix)
                        },
                        label = { Text(title) },
                        placeholder = { Text(outlinedInputParameters.placeholder) },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = RedUISolid,
                            placeholderColor = Color(0xFFA79C9E),
                            containerColor = Color.Transparent,
                            focusedLabelColor = RedUISolid
                        ),
                    )
                }
            }, confirmButton = {
                TextButton(
                    onClick = {
                        outlinedInputParameters.onConfirm(textValue)
                        openDialog = false
                    },
                    enabled = textValue.isNotEmpty(),
                    colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)
                ) {
                    Text("Done")
                }
            }, dismissButton = {
                TextButton(onClick = {
                    openDialog = false
                }, colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)) {
                    Text("Cancel")
                }
            })
        }
    }
}

data class OutlinedInputParameters(
    val label: String,
    val placeholder: String,
    val suffix: String,
    val onConfirm: (String) -> Unit,
)

@Preview
@Composable
fun TextFieldAlertDialogPreview() {
    KDualTheme {
        TextFieldAlertDialog(
            title = "Alert Title",
            description = "This is an alert description.",
            outlinedInputParameters = OutlinedInputParameters(
                label = "Input Label",
                placeholder = "Placeholder",
                onConfirm = { /* Preview doesn't handle interactions */ },
                suffix = "Suffix"
            )
        )
    }
}

