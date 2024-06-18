package me.bridgefy.example.android.alerts.ui.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun DateRangePickerDialog(
    dialogUiState: DateRangePickerDialogUiState,
) {
    val initialStartMs: Long? = dialogUiState.startLocalDate?.atStartOfDay()?.atOffset(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    val initialEndMs: Long? = dialogUiState.endLocalDate?.atStartOfDay()?.atOffset(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartMs,
        initialSelectedEndDateMillis = initialEndMs,
        initialDisplayMode = dialogUiState.initialDisplayMode,
    )
    val onDismissButtonClicked = dialogUiState.onDismiss

    DatePickerDialog(
        onDismissRequest = dialogUiState.onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val startMs = dateRangePickerState.selectedStartDateMillis
                    val endMs = dateRangePickerState.selectedEndDateMillis
                    if (startMs != null && endMs != null) {
                        val dateRange = PickerDateRange(
                            startDate = Instant.ofEpochMilli(startMs).atZone(ZoneId.of("UTC")).toLocalDate(),
                            endDate = Instant.ofEpochMilli(endMs).atZone(ZoneId.of("UTC")).toLocalDate(),
                        )

                        dialogUiState.onConfirm(dateRange)
                    }
                },
            ) {
                Text(dialogUiState.confirmButtonText())
            }
        },
        dismissButton = if (onDismissButtonClicked != null) { {
            TextButton(
                onClick = onDismissButtonClicked,
            ) {
                Text(dialogUiState.dismissButtonText())
            }
        }
        } else {
            null
        },
    ) {
        val dateFormatter = remember { DatePickerDefaults.dateFormatter() }
        DateRangePicker(
            dateFormatter = dateFormatter,
            title = if (dialogUiState.title != null) {
                dialogUiState.title
            } else {
                {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        DateRangePickerDefaults.DateRangePickerTitle(
                            displayMode = dateRangePickerState.displayMode,
                            modifier = Modifier.padding(PaddingValues(start = 64.dp, end = 12.dp)),
                        )
                    }
                }
            },
            headline = {
                DateRangePickerDefaults.DateRangePickerHeadline(
                    selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis,
                    selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis,
                    displayMode = dateRangePickerState.displayMode,
                    dateFormatter,
                    modifier =
                    Modifier.padding(
                        PaddingValues(
                            start = 64.dp,
                            end = 12.dp,
                            bottom = 12.dp,
                        ),
                    ),
                )
            },
            state = dateRangePickerState,
            showModeToggle = dialogUiState.showModeToggle,
            modifier = Modifier.heightIn(0.dp, 400.dp), // leave room for buttons
        )
    }
}

/**
 * DatePickerDialogUiState
 * @property title the title to be displayed in the date range picker
 * @property startLocalDate selected start date in range
 * @property endLocalDate selected end date in range
 * @property dateValidator a lambda that takes a date timestamp and return true if the date is a valid one for selection. Invalid dates will appear disabled in the UI.
 * @property showModeToggle show UI to switch between calendar view or text field view
 * @property initialDisplayMode initial range display mode
 * @property onConfirm confirm button press with the selected DateRange
 * @property onDismiss dismiss button press
 * @property onDismissRequest outside the dialog press
 * @property confirmButtonText text for confirm button. default to "OK"
 * @property dismissButtonText text for dismiss button. default to "Cancel"
 */
data class DateRangePickerDialogUiState(
    val title: (@Composable () -> Unit)? = null,
    val startLocalDate: LocalDate? = null,
    val endLocalDate: LocalDate? = null,
    val dateValidator: (Long) -> Boolean = { true },
    val showModeToggle: Boolean = true,
    val initialDisplayMode: DisplayMode = DisplayMode.Picker,
    override val onConfirm: (PickerDateRange?) -> Unit = {},
    override val onDismiss: (() -> Unit)? = null,
    override val onDismissRequest: () -> Unit = {},
    val confirmButtonText: @Composable () -> String = { stringResource(android.R.string.ok) },
    val dismissButtonText: @Composable () -> String = { stringResource(android.R.string.cancel) },
) : DialogUiState<PickerDateRange>

data class PickerDateRange(val startDate: LocalDate?, val endDate: LocalDate?)
