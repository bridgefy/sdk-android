package me.bridgefy.example.android.alerts.ui.compose.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.bridgefy.example.android.alerts.ui.compose.util.DateUiUtil
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime

@Composable
fun TimeClickableTextField(label: String, localTimeFlow: StateFlow<LocalTime?>, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val time by localTimeFlow.collectAsStateWithLifecycle()
    val text = DateUiUtil.getLocalTimeText(LocalContext.current, time)
    ClickableTextField(label, text, onClick, modifier)
}
