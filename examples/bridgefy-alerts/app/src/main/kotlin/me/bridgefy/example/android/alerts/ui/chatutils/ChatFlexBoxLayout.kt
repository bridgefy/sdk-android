package me.bridgefy.example.android.alerts.ui.chatutils

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatFlexBoxLayout(
    modifier: Modifier = Modifier,
    messageModifier: Modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
    text: String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 16.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    messageStat: @Composable () -> Unit = {},
    onMeasure: ((ChatRowData) -> Unit)? = null,
) {
    val chatRowData = remember { ChatRowData() }
    val content = @Composable {
        Message(
            modifier = messageModifier,
            text = text,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                chatRowData.text = text
                chatRowData.lineCount = textLayoutResult.lineCount
                chatRowData.lastLineWidth =
                    textLayoutResult.getLineRight(chatRowData.lineCount - 1)
                chatRowData.textWidth = textLayoutResult.size.width
            },
        )

        messageStat()
    }

    ChatLayout(modifier, chatRowData, content, onMeasure)
}

@Composable
fun ChatFlexBoxLayout(
    modifier: Modifier,
    messageModifier: Modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
    text: AnnotatedString,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 16.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    messageStat: @Composable () -> Unit = {},
    onMeasure: ((ChatRowData) -> Unit)? = null,
) {
    val chatRowData = remember { ChatRowData() }

    val content = @Composable {
        Message(
            modifier = messageModifier,
            text = text,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                chatRowData.text = text.text
                chatRowData.lineCount = textLayoutResult.lineCount
                chatRowData.lastLineWidth =
                    textLayoutResult.getLineRight(chatRowData.lineCount - 1)
                chatRowData.textWidth = textLayoutResult.size.width
            },
        )
        messageStat()
    }
    ChatLayout(modifier, chatRowData, content, onMeasure)
}

@Composable
fun ChatFlexBoxLayout(
    modifier: Modifier,
    message: @Composable () -> Unit,
    messageStat: @Composable () -> Unit = {},
    chatRowData: ChatRowData,
    onMeasure: ((ChatRowData) -> Unit)? = null,
) {
    val content = @Composable {
        message()
        messageStat()
    }
    ChatLayout(modifier, chatRowData, content, onMeasure)
}

@Composable
internal fun ChatLayout(
    modifier: Modifier = Modifier,
    chatRowData: ChatRowData,
    content: @Composable () -> Unit,
    onMeasure: ((ChatRowData) -> Unit)? = null,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables: List<Measurable>, constraints: Constraints ->

        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0))
        }

        require(placeables.size in 1..2)

        val message = placeables.first()
        val status = if (placeables.size > 1) {
            placeables.last()
        } else {
            null
        }

        chatRowData.parentWidth = constraints.maxWidth
        calculateChatWidthAndHeight(chatRowData, message, status)
        chatRowData.parentWidth =
            chatRowData.rowWidth.coerceAtLeast(minimumValue = constraints.minWidth)

        onMeasure?.invoke(chatRowData)

        layout(width = chatRowData.parentWidth, height = chatRowData.rowHeight) {
            message.placeRelative(0, 0)
            status?.placeRelative(
                chatRowData.parentWidth - status.width,
                chatRowData.rowHeight - status.height,
            )
        }
    }
}
