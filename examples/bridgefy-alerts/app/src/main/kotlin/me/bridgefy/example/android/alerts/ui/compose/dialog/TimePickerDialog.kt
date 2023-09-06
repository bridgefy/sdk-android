package me.bridgefy.example.android.alerts.ui.compose.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalTime

@Composable
fun TimePickerDialog(
    title: (@Composable () -> Unit)? = null,
    onDismissRequest: (() -> Unit) = {},
    initialHour: Int = 0,
    initialMinute: Int = 0,
    is24Hour: Boolean = false,
    confirmButtonText: String = stringResource(android.R.string.ok),
    onConfirmButtonClicked: ((LocalTime) -> Unit)? = null,
    dismissButtonText: String = stringResource(android.R.string.cancel),
    onDismissButtonClicked: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(), // DialogProperties(usePlatformDefaultWidth = false),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    val timePickerState = rememberTimePickerState(initialHour, initialMinute, is24Hour)

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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Title
                title?.invoke()

                // Time
                TimePicker(
                    state = timePickerState,
                )

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
                                onConfirmButtonClicked(LocalTime.of(timePickerState.hour, timePickerState.minute))
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
fun TimePickerDialog(
    dialogUiState: TimePickerDialogUiState,
) {
    val initialTime: LocalTime? = dialogUiState.localTime

    TimePickerDialog(
        title = dialogUiState.title,
        initialHour = initialTime?.hour ?: 0,
        initialMinute = initialTime?.minute ?: 0,
        is24Hour = dialogUiState.is24Hour,
        onConfirmButtonClicked = { localTime ->
            dialogUiState.onConfirm(localTime)
        },
        onDismissButtonClicked = dialogUiState.onDismissRequest,
        onDismissRequest = dialogUiState.onDismissRequest,
        confirmButtonText = dialogUiState.confirmButtonText(),
        dismissButtonText = dialogUiState.dismissButtonText(),
    )
}

data class TimePickerDialogUiState(
    val title: (@Composable () -> Unit)? = null,
    val localTime: LocalTime? = null,
    val is24Hour: Boolean = false,
    override val onConfirm: (LocalTime?) -> Unit = {},
    override val onDismiss: () -> Unit = {},
    override val onDismissRequest: () -> Unit = {},
    val confirmButtonText: @Composable () -> String = { stringResource(android.R.string.ok) },
    val dismissButtonText: @Composable () -> String = { stringResource(android.R.string.cancel) },
) : DialogUiState<LocalTime>
