package me.bridgefy.example.android.alerts.ux.chat.chatcomponets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageTimeText(
    modifier: Modifier = Modifier,
    messageTime: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                modifier = Modifier
                    .padding(top = 1.dp, bottom = 1.dp),
                text = messageTime,
                fontSize = 12.sp,
            )
        }

        Icon(
            modifier = Modifier
                .size(16.dp, 12.dp)
                .padding(start = 4.dp),
            imageVector = Icons.Default.Done,
            tint = Color(0xff424242),
            contentDescription = "messageStatus",
        )
    }
}
