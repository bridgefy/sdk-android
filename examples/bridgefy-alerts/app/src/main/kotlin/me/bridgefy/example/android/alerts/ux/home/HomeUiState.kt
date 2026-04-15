package me.bridgefy.example.android.alerts.ux.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.bridgefy.example.android.alerts.ui.compose.dialog.DialogUiState

data class HomeUiState(
    val dialogUiStateFlow: StateFlow<DialogUiState<*>?> = MutableStateFlow(null),
    val onReceivedCommand: () -> Unit = {},
    val onAdminClicked: () -> Unit = {},
)
