package me.bridgefy.example.android.alerts.ui.chatutils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.TextLayoutResult
import kotlin.random.Random

fun measureText(chatRowData: ChatRowData, textLayoutResult: TextLayoutResult) {
    chatRowData.text = textLayoutResult.layoutInput.text.text
    chatRowData.lineCount = textLayoutResult.lineCount
    chatRowData.lastLineWidth =
        textLayoutResult.getLineRight(chatRowData.lineCount - 1)
    chatRowData.textWidth = textLayoutResult.size.width
}

internal fun calculateChatWidthAndHeight(
    chatRowData: ChatRowData,
    message: Placeable,
    status: Placeable?,
) {
    if (status != null) {
        val lineCount = chatRowData.lineCount
        val lastLineWidth = chatRowData.lastLineWidth
        val parentWidth = chatRowData.parentWidth

        val padding = (message.measuredWidth - chatRowData.textWidth) / 2

        if (lineCount > 1 && lastLineWidth + status.measuredWidth >= chatRowData.textWidth + padding) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight + status.measuredHeight
            chatRowData.measuredType = 0
        } else if (lineCount > 1 && lastLineWidth + status.measuredWidth < chatRowData.textWidth + padding) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight
            chatRowData.measuredType = 1
        } else if (lineCount == 1 && message.width + status.measuredWidth >= parentWidth) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight + status.measuredHeight
            chatRowData.measuredType = 2
        } else {
            chatRowData.rowWidth = message.measuredWidth + status.measuredWidth
            chatRowData.rowHeight = message.measuredHeight
            chatRowData.measuredType = 3
        }
    } else {
        chatRowData.rowWidth = message.width
        chatRowData.rowHeight = message.height
    }
}

fun getRandomColor() = Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256),
    alpha = 255,
)
