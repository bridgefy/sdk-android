package me.bridgefy.example.android.alerts.ui.compose.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.bridgefy.example.android.alerts.ui.compose.DayNightTextField
import me.bridgefy.example.android.alerts.ui.compose.dialog.DialogDefaults
import me.bridgefy.example.android.alerts.ui.compose.dialog.DialogUiState

@Composable
fun <T> DropDownMenuDialog(
    onDismissRequest: (() -> Unit) = {},
    title: @Composable () -> String? = { null },
    text: @Composable () -> String? = { null },
    initialSelectedOption: T,
    options: List<T>,
    optionToText: @Composable (T) -> String,
    label: @Composable () -> String? = { null },
    confirmButtonText: String = stringResource(android.R.string.ok),
    onConfirmButtonClicked: ((T) -> Unit)? = null,
    dismissButtonText: String = stringResource(android.R.string.cancel),
    onDismissButtonClicked: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionTextFieldValue: T by remember { mutableStateOf(initialSelectedOption) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = backgroundColor,
        ) {
            Column(
                modifier = Modifier.padding(DialogDefaults.DialogPadding),
            ) {
                // Title
                val titleText = title()
                if (titleText != null) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                val textText = text()
                if (textText != null) {
                    Text(
                        text = textText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                // Text Field
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                ) {
                    val labelText = label()
                    DayNightTextField(
                        singleLine = true,
                        readOnly = true,
                        value = optionToText(selectedOptionTextFieldValue),
                        onValueChange = { },
                        label = if (labelText != null) { { Text(labelText) } } else null,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded,
                            )
                        },
                        modifier = Modifier
                            // As of Material3 1.0.0-beta03; The `menuAnchor` modifier must be passed to the text field for correctness.
                            // (https://android-review.googlesource.com/c/platform/frameworks/support/+/2200861)
                            .menuAnchor()
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(optionToText(selectionOption)) },
                                onClick = {
                                    selectedOptionTextFieldValue = selectionOption
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                ) {
                    if (onDismissButtonClicked != null) {
                        TextButton(
                            onClick = {
                                onDismissButtonClicked()
                            },
                        ) {
                            Text(dismissButtonText)
                        }
                    }
                    if (onConfirmButtonClicked != null) {
                        TextButton(
                            onClick = {
                                onConfirmButtonClicked(selectedOptionTextFieldValue)
                            },
                        ) {
                            Text(confirmButtonText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> DropDownMenuDialog(
    dialogUiState: DropDownMenuDialogUiState<T>,
) {
    DropDownMenuDialog(
        title = dialogUiState.title,
        text = dialogUiState.text,
        initialSelectedOption = dialogUiState.initialSelectedOption,
        options = dialogUiState.options,
        optionToText = dialogUiState.optionToText,
        onConfirmButtonClicked = { dialogUiState.onConfirm(it) },
        onDismissButtonClicked = { dialogUiState.onDismissRequest() },
    )
}

data class DropDownMenuDialogUiState<T>(
    val title: @Composable () -> String? = { null },
    val text: @Composable () -> String? = { null },
    val initialSelectedOption: T,
    val options: List<T>,
    val optionToText: @Composable (T) -> String,
    override val onConfirm: (T) -> Unit = {},
    override val onDismiss: () -> Unit = {},
    override val onDismissRequest: () -> Unit = {},
) : DialogUiState<T>
