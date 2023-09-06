package me.bridgefy.example.android.alerts.ui.compose.form

data class TextFieldData(
    val text: String,
    val supportingText: String? = null,
    val isError: Boolean = false,
)
