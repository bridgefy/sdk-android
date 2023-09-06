package me.bridgefy.example.android.alerts.ui.compose.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import me.bridgefy.example.android.alerts.ui.compose.DayNightTextField
import me.bridgefy.example.android.alerts.ui.compose.util.formKeyEventHandler

@Composable
fun ClickableTextField(
    label: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false,
) {
    val source = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    DayNightTextField(
        value = text,
        onValueChange = { },
        readOnly = true,
        label = { Text(label) },
        interactionSource = source,
        supportingText = supportingText?.let { { Text(it) } },
        isError = isError,
        modifier = modifier
            .onPreviewKeyEvent { formKeyEventHandler(it, focusManager, onEnter = onClick) },
    )

    if (source.collectIsPressedAsState().value) {
        onClick()
    }
}
