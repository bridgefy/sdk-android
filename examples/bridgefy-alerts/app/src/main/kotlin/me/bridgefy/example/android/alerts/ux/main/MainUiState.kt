package me.bridgefy.example.android.alerts.ux.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MainUiState(
    val selectedAppThemeFlow: StateFlow<SelectedAppTheme?> = MutableStateFlow(null),
)
