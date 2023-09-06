package me.bridgefy.example.android.alerts.ui.compose.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.bridgefy.example.android.alerts.ui.compose.PreviewDefault
import me.bridgefy.example.android.alerts.ui.theme.AppTheme

@Composable
fun <T> MultiSelectDialog(
    allItems: List<MultiSelectDataItem<T>>,
    selectedItems: List<T>,
    title: @Composable () -> String? = { null },
    confirmButtonText: @Composable () -> String? = { stringResource(android.R.string.ok) },
    onConfirmButtonClicked: ((List<T>) -> Unit)? = null,
    dismissButtonText: @Composable () -> String? = { stringResource(android.R.string.cancel) },
    onDismissButtonClicked: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit) = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    properties: DialogProperties = DialogProperties(),
) {
    val savedSelectedItems = remember { selectedItems.toMutableStateList() }

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

                // Items
                val height = LocalConfiguration.current.screenHeightDp * .50 // don't take the full screen
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .heightIn(0.dp, height.dp),
                ) {
                    items(allItems) {
                        val selected = savedSelectedItems.contains(it.item)

                        MultiSelectDialogItem(it, selected) { selectedItem ->
                            if (savedSelectedItems.contains(selectedItem)) {
                                savedSelectedItems.remove(selectedItem)
                            } else {
                                savedSelectedItems.add(selectedItem)
                            }
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
                    val dismissButtonTextString = dismissButtonText()
                    if (onDismissButtonClicked != null && dismissButtonTextString != null) {
                        TextButton(
                            onClick = {
                                onDismissButtonClicked()
                            },
                        ) {
                            Text(dismissButtonTextString)
                        }
                    }
                    val confirmButtonTextString = confirmButtonText()
                    if (onConfirmButtonClicked != null && confirmButtonTextString != null) {
                        TextButton(
                            onClick = {
                                onConfirmButtonClicked(savedSelectedItems)
                            },
                        ) {
                            Text(confirmButtonTextString)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> MultiSelectDialogItem(
    multiSelectDataItem: MultiSelectDataItem<T>,
    selected: Boolean,
    onItemSelected: (T) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(45.dp)
            .selectable(
                selected = selected,
                onClick = { onItemSelected(multiSelectDataItem.item) },
                role = Role.Checkbox,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = multiSelectDataItem.text(),
            style = MaterialTheme.typography.bodyMedium.merge(),
            modifier = Modifier.padding(start = 16.dp),
        )
        Checkbox(
            checked = selected,
            onCheckedChange = null, // null recommended for accessibility with screen readers
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
fun <T> MultiSelectDialog(
    dialogUiState: MultiSelectDialogUiState<T>,
) {
    MultiSelectDialog(
        allItems = dialogUiState.allItems,
        selectedItems = dialogUiState.selectedItems,
        title = dialogUiState.title,
        onConfirmButtonClicked = { dialogUiState.onConfirm?.invoke(it) },
        onDismissRequest = { dialogUiState.onDismissRequest() },
        onDismissButtonClicked = if (dialogUiState.onDismiss != null) {
            { dialogUiState.onDismiss.invoke() }
        } else {
            null
        },
        confirmButtonText = dialogUiState.confirmButtonText,
        dismissButtonText = dialogUiState.dismissButtonText,
    )
}

data class MultiSelectDialogUiState<T>(
    val allItems: List<MultiSelectDataItem<T>>,
    val selectedItems: List<T>,
    val title: @Composable () -> String? = { null },
    val confirmButtonText: @Composable () -> String? = { stringResource(android.R.string.ok) },
    val dismissButtonText: @Composable () -> String? = { stringResource(android.R.string.cancel) },
    override val onConfirm: ((List<T>) -> Unit)? = null,
    override val onDismiss: (() -> Unit)? = null,
    override val onDismissRequest: () -> Unit = {},
) : DialogUiState<List<T>>

data class MultiSelectDataItem<T>(val item: T, val text: @Composable () -> String)

@PreviewDefault
@Composable
private fun PreviewMultiSelectDialog() {
    val items = listOf(
        MultiSelectDataItem("id1") { "A" },
        MultiSelectDataItem("id2") { "B" },
        MultiSelectDataItem("id3") { "C" },
    )

    val selectedItems = listOf("id2", "id3")

    AppTheme {
        MultiSelectDialog(
            title = { "Title" },
            allItems = items,
            selectedItems = selectedItems,
            onConfirmButtonClicked = { },
            onDismissButtonClicked = { },
        )
    }
}
