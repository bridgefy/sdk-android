package me.bridgefy.example.android.alerts.ux.chat.chatcomponets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.bridgefy.example.android.alerts.ui.chatutils.ChatFlexBoxLayout
import me.bridgefy.example.android.alerts.ui.chatutils.SubcomposeColumn

var isRecipientRegistered = true
var recipientOriginalName = "Some user"

@Composable
fun ReceivedMessageRow(
    text: String,
    contactName: String?,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp, end = 60.dp, top = 2.dp, bottom = 2.dp),

    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable { },
            content = {
                if (contactName != null) {
                    RecipientName(
                        name = contactName,
                        isName = isRecipientRegistered,
                        altName = recipientOriginalName,
                    )
                }

                if (quotedMessage != null || quotedImage != null) {
                    QuotedMessage(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .height(IntrinsicSize.Min)
                            .background(Color(0xff757575), shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                            },
                        quotedMessage = quotedMessage,
                        quotedImage = quotedImage,
                    )
                }

                ChatFlexBoxLayout(
                    modifier = Modifier
                        .padding(start = 2.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
                    text = text,
                    messageStat = {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                modifier = Modifier.padding(top = 1.dp, bottom = 1.dp, end = 4.dp),
                                text = messageTime,
                                fontSize = 12.sp,
                            )
                        }
                    },
                )
            },
        )
    }
}

@Composable
fun ReceivedMessageRowAlt1(
    text: String,
    contactName: String,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp, end = 60.dp, top = 2.dp, bottom = 2.dp),

    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable { },
            content = {
                RecipientName(
                    name = contactName,
                    isName = isRecipientRegistered,
                    altName = recipientOriginalName,
                )

                if (quotedMessage != null || quotedImage != null) {
                    QuotedMessageWithConstraintLayout(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .height(IntrinsicSize.Min)
                            .background(Color(0xff757575), shape = RoundedCornerShape(8.dp))
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                            },
                        quotedMessage = quotedMessage,
                        quotedImage = quotedImage,
                    )
                }

                ChatFlexBoxLayout(
                    modifier = Modifier
                        .padding(start = 2.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
                    text = text,
                    messageStat = {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                modifier = Modifier.padding(top = 1.dp, bottom = 1.dp, end = 4.dp),
                                text = messageTime,
                                fontSize = 12.sp,
                            )
                        }
                    },
                )
            },
        )
    }
}

@Composable
fun ReceivedMessageRowAlt2(
    text: String,
    quotedMessage: String? = null,
    contactId: String,
    quotedImage: Int? = null,
    messageTime: String,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp, end = 60.dp, top = 2.dp, bottom = 2.dp),

    ) {
        SubcomposeColumn(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable { },
            mainContent = {
                Column {
                    RecipientName(
                        name = contactId.substring(0, 8),
                        isName = isRecipientRegistered,
                        altName = recipientOriginalName,
                    )

                    if (quotedMessage != null || quotedImage != null) {
                        QuotedMessage(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                                .height(IntrinsicSize.Min)
                                .background(Color(0xff757575), shape = RoundedCornerShape(8.dp))
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable {},
                            quotedMessage = quotedMessage,
                            quotedImage = quotedImage,
                        )
                    }
                }
            },
            dependentContent = {
                ChatFlexBoxLayout(
                    modifier = Modifier
                        .padding(start = 2.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
                    text = text,
                    messageStat = {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                modifier = Modifier.padding(top = 1.dp, bottom = 1.dp, end = 4.dp),
                                text = messageTime,
                                fontSize = 12.sp,
                            )
                        }
                    },
                )
            },
        )
    }
}
