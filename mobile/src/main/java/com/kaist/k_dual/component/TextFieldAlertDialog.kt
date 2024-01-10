package com.kaist.k_dual.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaist.k_dual.R
import com.kaist.k_dual.ui.theme.Background
import com.kaist.k_dual.ui.theme.RedUISolid
import com.kaist.k_dual.ui.theme.KDualTheme
import kotlin.math.min

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
    var textFieldValue by remember { mutableStateOf(TextFieldValue(outlinedInputParameters.initialValue)) }

    val customColorScheme = lightColorScheme(
        surface = Background
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
                    textFieldValue = textFieldValue.copy(text = "")
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
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        textFieldValue =
                                            textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
                                    }
                                },
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                textFieldValue = if (outlinedInputParameters.maxLength != null) {
                                    val trimmedText = newValue.text.substring(
                                        0,
                                        min(
                                            newValue.text.length,
                                            outlinedInputParameters.maxLength
                                        )
                                    )
                                    if (newValue.composition != null) {
                                        TextFieldValue(
                                            text = trimmedText,
                                            selection = TextRange(newValue.selection.start),
                                            composition = newValue.composition
                                        )
                                    } else {
                                        TextFieldValue(
                                            text = trimmedText,
                                            selection = TextRange(newValue.text.length)
                                        )
                                    }
                                } else {
                                    newValue
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
                                        text = "${textFieldValue.text.length}/${outlinedInputParameters.maxLength}",
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
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = RedUISolid,
                                focusedLabelColor = RedUISolid,
                                focusedPlaceholderColor = Color(0xFFA79C9E),
                            ),
                            keyboardOptions = outlinedInputParameters.keyboardOptions,
                            isError = textFieldValue.text.isNotEmpty() && !outlinedInputParameters.validation(
                                textFieldValue.text
                            ),
                            maxLines = 1,
                            singleLine = true,
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm(textFieldValue.text)
                        },
                        enabled = (outlinedInputParameters.allowEmpty || textFieldValue.text.isNotEmpty())
                                && outlinedInputParameters.validation(textFieldValue.text),
                        colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)
                    ) {
                        Text(
                            stringResource(R.string.done),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        textFieldValue = textFieldValue.copy(text = "")
                        onDismiss()
                    }, colors = ButtonDefaults.textButtonColors(contentColor = RedUISolid)) {
                        Text(
                            stringResource(R.string.cancel),
                            style = MaterialTheme.typography.labelLarge
                        )
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
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val validation: (String) -> Boolean = { _ -> true },
    val allowEmpty: Boolean = false,
    val initialValue: String = "",
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

