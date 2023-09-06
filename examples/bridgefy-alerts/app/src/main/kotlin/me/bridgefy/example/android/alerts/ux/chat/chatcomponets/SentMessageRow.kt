package me.bridgefy.example.android.alerts.ux.chat.chatcomponets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.bridgefy.example.android.alerts.ui.chatutils.ChatFlexBoxLayout
import me.bridgefy.example.android.alerts.ui.chatutils.ChatRowData
import me.bridgefy.example.android.alerts.ui.chatutils.SubcomposeColumn
import me.bridgefy.example.android.alerts.ui.chatutils.measureText

@Composable
fun SentMessageRow(
    text: String,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 60.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),
    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xffE7FFDB))
                .clickable { },

            content = {
                if (quotedMessage != null || quotedImage != null) {
                    QuotedMessage(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .height(IntrinsicSize.Min)
                            .background(Color(0xffDEF6D3), shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                            },
                        quotedMessage = quotedMessage,
                        quotedImage = quotedImage,
                    )
                }

                ChatFlexBoxLayout(
                    modifier = Modifier.padding(
                        start = 2.dp,
                        top = 2.dp,
                        end = 8.dp,
                        bottom = 2.dp,
                    ),
                    text = text,
                    messageStat = {
                        MessageTimeText(
                            modifier = Modifier.wrapContentSize(),
                            messageTime = messageTime,
                        )
                    },
                )
            },
        )
    }
}

@Composable
fun SentMessageRowAlt1(
    text: String,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
) {
    val chatRowData = remember { ChatRowData() }
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 60.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),

    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xffE7FFDB))
                .clickable { },

            content = {
                if (quotedMessage != null || quotedImage != null) {
                    QuotedMessage(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .height(IntrinsicSize.Min)
                            .background(Color(0xffDEF6D3), shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                            },
                        quotedMessage = quotedMessage,
                        quotedImage = quotedImage,
                    )
                }

                ChatFlexBoxLayout(
                    modifier = Modifier.padding(
                        start = 2.dp,
                        top = 2.dp,
                        end = 8.dp,
                        bottom = 2.dp,
                    ),
                    message = {
                        Text(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            text = text,
                            fontSize = 16.sp,
                            onTextLayout = {
                                measureText(chatRowData, it)
                            },
                        )
                    },
                    messageStat = {
                        MessageTimeText(
                            modifier = Modifier.wrapContentSize(),
                            messageTime = messageTime,
                        )
                    },
                    chatRowData = chatRowData,
                )
            },
        )
    }
}

@Composable
fun SentMessageRowAlt2(
    text: String,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 60.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),
    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xffE7FFDB))
                .clickable { },

            mainContent = {
                if (quotedMessage != null || quotedImage != null) {
                    QuotedMessage(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .height(IntrinsicSize.Min)
                            .background(Color(0xffDEF6D3)
                                , shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {},
                        quotedMessage = quotedMessage,
                        quotedImage = quotedImage,
                    )
                }
            },
            dependentContent = {
                ChatFlexBoxLayout(
                    modifier = Modifier.padding(
                        start = 2.dp,
                        top = 2.dp,
                        end = 8.dp,
                        bottom = 2.dp,
                    ),
                    text = text,
                    messageStat = {
                        MessageTimeText(
                            modifier = Modifier.wrapContentSize(),
                            messageTime = messageTime,
                        )
                    },
                )
            },
        )
    }
}

@Composable
fun SentMessageRowAlt3(
    text: String,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 60.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),
    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xffE7FFDB))
                .clickable { },

            mainContent = {
                if (quotedMessage != null || quotedImage != null) {
                    QuotedMessageWithConstraintLayout(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .height(IntrinsicSize.Min)
                            .background(Color(0xffDEF6D3), shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                            },
                        quotedMessage = quotedMessage,
                        quotedImage = quotedImage,
                    )
                }
            },
            dependentContent = {
                ChatFlexBoxLayout(
                    modifier = Modifier.padding(
                        start = 2.dp,
                        top = 2.dp,
                        end = 8.dp,
                        bottom = 2.dp,
                    ),
                    text = text,
                    messageStat = {
                        MessageTimeText(
                            modifier = Modifier.wrapContentSize(),
                            messageTime = messageTime,
                        )
                    },
                )
            },
        )
    }
}
