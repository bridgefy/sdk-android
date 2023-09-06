package me.bridgefy.example.android.alerts.ui.compose.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import me.bridgefy.example.android.alerts.ui.compose.PreviewDefault

@Composable
fun TextWithTitle(
    text: String?,
    label: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    if (text.isNullOrBlank()) {
        return
    }
    Column {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp),
            )
        }
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .padding(top = if (label != null) 4.dp else 8.dp, bottom = 8.dp),
        )
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextWithTitle(text = "John", label = "First Name")
            TextWithTitle(text = "Adams", label = "Last Name")
            TextWithTitle(text = "123 Main Street")
        }
    }
}
