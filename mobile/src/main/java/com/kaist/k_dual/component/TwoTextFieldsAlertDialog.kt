package com.kaist.k_dual.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaist.k_dual.ui.theme.RedUISolid
import com.kaist.k_dual.ui.theme.KDualTheme
import kotlin.math.min

@Composable
fun TwoTextFieldsAlertDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    description: String,
    outlinedInputParameters1: OutlinedInputParameters,
    outlinedInputParameters2: OutlinedInputParameters
) {
    var textFieldValue1 by remember { mutableStateOf(TextFieldValue(outlinedInputParameters1.initialValue)) }
    var textFieldValue2 by remember { mutableStateOf(TextFieldValue(outlinedInputParameters2.initialValue)) }

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
                    textFieldValue1 = TextFieldValue("")
                    textFieldValue2 = TextFieldValue("")
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
                            modifier = outlinedInputParameters1.modifier
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        textFieldValue1 =
                                            textFieldValue1.copy(selection = TextRange(textFieldValue1.text.length))
                                    }
                                },
                            value = textFieldValue1,
                            onValueChange = { newValue ->
                                textFieldValue1 = if (outlinedInputParameters1.maxLength != null) {
                                    TextFieldValue(
                                        text = newValue.text.substring(
                                            0,
                                            min(
                                                newValue.text.length,
                                                outlinedInputParameters1.maxLength
                                            )
                                        ),
                                        selection = TextRange(newValue.text.length)
                                    )
                                } else {
                                    newValue
                                }
                            },
                            trailingIcon = {
                                outlinedInputParameters1.suffix?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )
                                }
                            },
                            label = { Text(outlinedInputParameters1.label) },
                            placeholder = {
                                Text(
                                    outlinedInputParameters1.placeholder,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = RedUISolid,
                                focusedLabelColor = RedUISolid,
                                focusedPlaceholderColor = Color(0xFFA79C9E),
                            ),
                            keyboardOptions = outlinedInputParameters1.keyboardOptions,
                            isError = textFieldValue1.text.isNotEmpty() && !outlinedInputParameters1.validation(
                                textFieldValue1.text
                            ),
                            maxLines = 1,
                            singleLine = true,
                        )
                        OutlinedTextField(
                            modifier = outlinedInputParameters2.modifier
                                .padding(top = 6.dp)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        textFieldValue2 =
                                            textFieldValue2.copy(selection = TextRange(textFieldValue2.text.length))
                                    }
                                },
                            value = textFieldValue2,
                            onValueChange = { newValue ->
                                textFieldValue2 = if (outlinedInputParameters2.maxLength != null) {
                                    TextFieldValue(
                                        text = newValue.text.substring(
                                            0,
                                            min(
                                                newValue.text.length,
                                                outlinedInputParameters2.maxLength
                                            )
                                        ),
                                        selection = TextRange(newValue.text.length)
                                    )
                                } else {
                                    newValue
                                }
                            },
                            trailingIcon = {
                                outlinedInputParameters2.suffix?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )
                                }
                            },
                            label = { Text(outlinedInputParameters2.label) },
                            placeholder = {
                                Text(
                                    outlinedInputParameters2.placeholder,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = RedUISolid,
                                focusedLabelColor = RedUISolid,
                                focusedPlaceholderColor = Color(0xFFA79C9E),
                            ),
                            keyboardOptions = outlinedInputParameters2.keyboardOptions,
                            isError = textFieldValue2.text.isNotEmpty() && !outlinedInputParameters2.validation(
                                textFieldValue2.text
                            ),
                            maxLines = 1,
                            singleLine = true,
                        )

                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm(textFieldValue1.text, textFieldValue2.text)
                        },
                        enabled = textFieldValue1.text.isNotEmpty() && textFieldValue2.text.isNotEmpty(),
                        colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)
                    ) {
                        Text("Done", style = MaterialTheme.typography.labelLarge)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        textFieldValue1 = TextFieldValue("")
                        textFieldValue2 = TextFieldValue("")
                        onDismiss()
                    }, colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun TwoTextFieldsAlertDialogPreview() {
    KDualTheme {
        TwoTextFieldsAlertDialog(
            isOpen = true,
            onConfirm = { _, _ -> {} },
            onDismiss = { /* Preview doesn't handle interactions */ },
            title = "Alert Title",
            description = "This is an alert description.",
            outlinedInputParameters1 = OutlinedInputParameters(
                label = "Label1",
                placeholder = "Placeholder1",
            ),
            outlinedInputParameters2 = OutlinedInputParameters(
                label = "Label2",
                placeholder = "Placeholder2",
                suffix = "Suffix2"
            )
        )
    }
}

