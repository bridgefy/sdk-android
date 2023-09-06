package me.bridgefy.example.android.alerts.ui.compose.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.bridgefy.example.android.alerts.ui.compose.DayNightTextField
import me.bridgefy.example.android.alerts.ui.compose.PreviewDefault
import me.bridgefy.example.android.alerts.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun InputDialog(
    onDismissRequest: (() -> Unit),
    title: String? = null,
    textFieldLabel: String? = null,
    initialTextFieldText: String? = null,
    confirmButtonText: String? = stringResource(android.R.string.ok),
    onConfirmButtonClicked: ((String) -> Unit)? = null,
    dismissButtonText: String? = stringResource(android.R.string.cancel),
    onDismissButtonClicked: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLength: Int = -1,
    maxLength: Int = -1,
    properties: DialogProperties = DialogProperties(),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textButtonColor: Color = MaterialTheme.colorScheme.primary, // This is specifically for handling theming in this app. May not want in Commons.
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(initialTextFieldText.orEmpty(), TextRange(initialTextFieldText?.length ?: 0))) }

    val focusRequester = remember { FocusRequester() }

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
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                val keyboardController = LocalSoftwareKeyboardController.current

                // Text Field
                DayNightTextField(
                    value = textFieldValue,
                    onValueChange = { newTextFieldValue ->
                        val newText = if (maxLength > 0) {
                            newTextFieldValue.text.take(maxLength)
                        } else {
                            newTextFieldValue.text
                        }

                        textFieldValue = newTextFieldValue.copy(newText)
                    },
                    label = if (textFieldLabel != null) {
                        { Text(textFieldLabel) }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .focusRequester(focusRequester),
                    singleLine = singleLine,
                    keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                )

                LaunchedEffect(Unit) {
                    delay(200) // Need to have a small delay or the keyboard does not appear on the focus: https://issuetracker.google.com/issues/204502668
                    focusRequester.requestFocus()
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                ) {
                    if (onDismissButtonClicked != null && dismissButtonText != null) {
                        TextButton(
                            onClick = {
                                onDismissButtonClicked()
                            },
                        ) {
                            Text(dismissButtonText, color = textButtonColor)
                        }
                    }
                    if (onConfirmButtonClicked != null && confirmButtonText != null) {
                        val isEnabled = minLength == -1 || textFieldValue.text.length >= minLength
                        TextButton(
                            enabled = isEnabled,
                            onClick = {
                                onConfirmButtonClicked(textFieldValue.text)
                            },
                        ) {
                            Text(confirmButtonText, color = if (isEnabled) textButtonColor else textButtonColor.copy(alpha = 0.38f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputDialog(
    dialogUiState: InputDialogUiState,
) {
    InputDialog(
        onDismissRequest = dialogUiState.onDismissRequest,
        title = dialogUiState.title(),
        textFieldLabel = dialogUiState.textFieldLabel(),
        initialTextFieldText = dialogUiState.initialTextFieldText(),
        confirmButtonText = dialogUiState.confirmButtonText(),
        onConfirmButtonClicked = dialogUiState.onConfirm,
        dismissButtonText = dialogUiState.dismissButtonText(),
        onDismissButtonClicked = dialogUiState.onDismiss,
        keyboardOptions = dialogUiState.keyboardOptions ?: KeyboardOptions.Default,
        singleLine = dialogUiState.singleLine,
        minLength = dialogUiState.minLength,
        maxLength = dialogUiState.maxLength,
        properties = DialogProperties(),
        backgroundColor = MaterialTheme.colorScheme.surface,
        textButtonColor = MaterialTheme.colorScheme.primary, // This is specifically for handling theming in this app. May not want in Commons.
    )
}

data class InputDialogUiState(
    val title: @Composable () -> String? = { null },
    val textFieldLabel: @Composable () -> String? = { null },
    val initialTextFieldText: @Composable () -> String? = { null },
    val confirmButtonText: @Composable () -> String? = { stringResource(android.R.string.ok) },
    val dismissButtonText: @Composable () -> String? = { stringResource(android.R.string.cancel) },
    val keyboardOptions: KeyboardOptions? = null,
    val singleLine: Boolean = true,
    val minLength: Int = -1,
    val maxLength: Int = -1,
    override val onConfirm: (String) -> Unit = {},
    override val onDismiss: () -> Unit = {},
    override val onDismissRequest: () -> Unit = {},
) : DialogUiState<String>

@Suppress("LongMethod")
@Composable
fun TwoInputDialog(
    onDismissRequest: (() -> Unit) = {},
    title: String? = null,
    textFieldLabelFirst: String? = null,
    initialTextFieldTextFirst: String? = null,
    textFieldLabelSecond: String? = null,
    initialTextFieldTextSecond: String? = null,
    confirmButtonText: String? = stringResource(android.R.string.ok),
    onConfirmButtonClicked: ((Pair<String, String>) -> Unit)? = null,
    dismissButtonText: String? = stringResource(android.R.string.cancel),
    onDismissButtonClicked: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLengthFirst: Int = -1,
    maxLengthFirst: Int = -1,
    minLengthSecond: Int = -1,
    maxLengthSecond: Int = -1,
    properties: DialogProperties = DialogProperties(),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textButtonColor: Color = MaterialTheme.colorScheme.primary, // This is specifically for handling theming in this app. May not want in Commons.
) {
    var textFieldValueFirst by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(initialTextFieldTextFirst.orEmpty(), TextRange(initialTextFieldTextFirst?.length ?: 0)))
    }
    var textFieldValueSecond by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(initialTextFieldTextSecond.orEmpty(), TextRange(initialTextFieldTextSecond?.length ?: 0)))
    }

    val (item1, item2) = remember { FocusRequester.createRefs() }

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
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                val keyboardController = LocalSoftwareKeyboardController.current

                // First Text Field
                DayNightTextField(
                    value = textFieldValueFirst,
                    onValueChange = { newTextFieldValue ->
                        val newText = if (maxLengthFirst > 0) {
                            newTextFieldValue.text.take(maxLengthFirst)
                        } else {
                            newTextFieldValue.text
                        }

                        textFieldValueFirst = newTextFieldValue.copy(newText)
                    },
                    label = if (textFieldLabelFirst != null) {
                        { Text(textFieldLabelFirst) }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .focusRequester(item1)
                        .focusProperties {
                            next = item2
                            down = item2
                            previous = item2
                            up = item2
                        },
                    singleLine = singleLine,
                    keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Next),
                )

                // Second Text Field
                DayNightTextField(
                    value = textFieldValueSecond,
                    onValueChange = { newTextFieldValue ->
                        val newText = if (maxLengthSecond > 0) {
                            newTextFieldValue.text.take(maxLengthSecond)
                        } else {
                            newTextFieldValue.text
                        }

                        textFieldValueSecond = newTextFieldValue.copy(newText)
                    },
                    label = if (textFieldLabelSecond != null) {
                        { Text(textFieldLabelSecond) }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .focusRequester(item2)
                        .focusProperties {
                            next = item1
                            down = item1
                            previous = item1
                            up = item1
                        },
                    singleLine = singleLine,
                    keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                )

                LaunchedEffect(Unit) {
                    delay(200) // Need to have a small delay or the keyboard does not appear on the focus: https://issuetracker.google.com/issues/204502668
                    item1.requestFocus()
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                ) {
                    if (onDismissButtonClicked != null && dismissButtonText != null) {
                        TextButton(
                            onClick = {
                                onDismissButtonClicked()
                            },
                        ) {
                            Text(dismissButtonText, color = textButtonColor)
                        }
                    }
                    if (onConfirmButtonClicked != null && confirmButtonText != null) {
                        val isEnabled = (minLengthFirst == -1 || textFieldValueFirst.text.length >= minLengthFirst) && (minLengthSecond == -1 || textFieldValueSecond.text.length >= minLengthSecond)
                        TextButton(
                            enabled = isEnabled,
                            onClick = {
                                onConfirmButtonClicked(Pair(textFieldValueFirst.text, textFieldValueSecond.text))
                            },
                        ) {
                            Text(confirmButtonText, color = if (isEnabled) textButtonColor else textButtonColor.copy(alpha = 0.38f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TwoInputDialog(
    dialogUiState: TwoInputDialogUiState,
) {
    TwoInputDialog(
        onDismissRequest = { dialogUiState.onDismissRequest() },
        title = dialogUiState.title(),
        textFieldLabelFirst = dialogUiState.textFieldLabelFirst(),
        initialTextFieldTextFirst = dialogUiState.initialTextFieldTextFirst(),
        textFieldLabelSecond = dialogUiState.textFieldLabelSecond(),
        initialTextFieldTextSecond = dialogUiState.initialTextFieldTextSecond(),
        confirmButtonText = dialogUiState.confirmButtonText(),
        onConfirmButtonClicked = { dialogUiState.onConfirm(it) },
        dismissButtonText = dialogUiState.dismissButtonText(),
        onDismissButtonClicked = { dialogUiState.onDismiss() },
        keyboardOptions = dialogUiState.keyboardOptions ?: KeyboardOptions.Default,
        singleLine = dialogUiState.singleLine,
        minLengthFirst = dialogUiState.minLengthSecond,
        maxLengthFirst = dialogUiState.maxLengthSecond,
        properties = DialogProperties(),
        backgroundColor = MaterialTheme.colorScheme.surface,
        textButtonColor = MaterialTheme.colorScheme.primary, // This is specifically for handling theming in this app. May not want in Commons.
    )
}

data class TwoInputDialogUiState(
    val title: @Composable () -> String? = { null },
    val textFieldLabelFirst: @Composable () -> String? = { null },
    val initialTextFieldTextFirst: @Composable () -> String? = { null },
    val textFieldLabelSecond: @Composable () -> String? = { null },
    val initialTextFieldTextSecond: @Composable () -> String? = { null },
    val confirmButtonText: @Composable () -> String? = { stringResource(android.R.string.ok) },
    val dismissButtonText: @Composable () -> String? = { stringResource(android.R.string.cancel) },
    val keyboardOptions: KeyboardOptions? = null,
    val singleLine: Boolean = true,
    val minLengthFirst: Int = -1,
    val maxLengthFirst: Int = -1,
    val minLengthSecond: Int = -1,
    val maxLengthSecond: Int = -1,
    override val onConfirm: (Pair<String, String>) -> Unit = {},
    override val onDismiss: () -> Unit = {},
    override val onDismissRequest: () -> Unit = {},
) : DialogUiState<Pair<String, String>>

@PreviewDefault
@Composable
private fun PreviewInputDialog() {
    AppTheme {
        InputDialog(
            onDismissRequest = {},
            title = "Title",
            initialTextFieldText = "Default Value",
            onConfirmButtonClicked = { },
            onDismissButtonClicked = { },
            minLength = 1,
            maxLength = 20,
        )
    }
}

@PreviewDefault
@Composable
private fun PreviewTwoInputDialog() {
    AppTheme {
        TwoInputDialog(
            onDismissRequest = {},
            title = "Title",
            initialTextFieldTextFirst = "First",
            initialTextFieldTextSecond = "Second",
            onConfirmButtonClicked = { },
            onDismissButtonClicked = { },
        )
    }
}
