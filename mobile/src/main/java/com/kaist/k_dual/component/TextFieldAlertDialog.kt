package com.kaist.k_dual.component

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaist.k_dual.ui.theme.RedUISolid
import com.kaist.k_dual.ui.theme.KDualTheme
import java.lang.Integer.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldAlertDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    description: String,
    outlinedInputParameters: OutlinedInputParameters
) {
    var textValue by remember { mutableStateOf("") }

    val customColorScheme = lightColorScheme(
        surface = Color(0xFFFCECEC)
    )

    val focusRequester = remember { FocusRequester() }

    if (isOpen) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        MaterialTheme(colorScheme = customColorScheme) {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = {
                    onDismiss()
                },
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        OutlinedTextField(
                            modifier = outlinedInputParameters.modifier
                                .padding(vertical = 16.dp)
                                .focusRequester(focusRequester),
                            value = textValue,
                            onValueChange = {
                                textValue = if (outlinedInputParameters.maxLength != null) {
                                    it.substring(
                                        0,
                                        min(it.length, outlinedInputParameters.maxLength)
                                    )
                                } else {
                                    it
                                }
                            },
                            trailingIcon = {
                                if (outlinedInputParameters.suffix != null) {
                                    Text(
                                        text = outlinedInputParameters.suffix,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )
                                }
                                if (outlinedInputParameters.maxLength != null) {
                                    Text(
                                        text = "${textValue.length}/${outlinedInputParameters.maxLength}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(end = 16.dp),
                                        color = RedUISolid,
                                    )
                                }
                            },
                            label = { Text(title) },
                            placeholder = {
                                Text(
                                    outlinedInputParameters.placeholder,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = RedUISolid,
                                placeholderColor = Color(0xFFA79C9E),
                                containerColor = Color.Transparent,
                                focusedLabelColor = RedUISolid
                            ),
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm(textValue)
                        },
                        enabled = textValue.isNotEmpty(),
                        colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)
                    ) {
                        Text("Done", style = MaterialTheme.typography.labelLarge)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        onDismiss()
                    }, colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }
                },
            )
        }
    }
}

data class OutlinedInputParameters(
    val modifier: Modifier = Modifier,
    val label: String,
    val placeholder: String,
    val suffix: String? = null,
    val maxLength: Int? = null,
)

@Preview
@Composable
fun TextFieldAlertDialogPreview() {
    KDualTheme {
        TextFieldAlertDialog(
            isOpen = true,
            onConfirm = { /* Preview doesn't handle interactions */ },
            onDismiss = { /* Preview doesn't handle interactions */ },
            title = "Alert Title",
            description = "This is an alert description.",
            outlinedInputParameters = OutlinedInputParameters(
                label = "Label",
                placeholder = "Placeholder",
                maxLength = 10,
            )
        )
    }
}

