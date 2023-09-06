package me.bridgefy.example.android.alerts.ux.chat.chatcomponets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import me.bridgefy.example.android.alerts.ui.chatutils.getRandomColor

@Composable
fun QuotedMessage(
    modifier: Modifier = Modifier,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
) {
    val color = remember { getRandomColor() }

    QuoteImageRow(
        modifier = modifier,
        content = {
            Row {
                Surface(
                    color = color,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp),
                ) {
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .wrapContentHeight(),
                ) {
                    Text(
                        "You",
                        color = color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        letterSpacing = 1.sp,
                        overflow = TextOverflow.Ellipsis,
                    )

                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (quotedImage != null) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            Text(
                                text = quotedMessage ?: "Photo",
                                fontSize = 12.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        },
        image = {
            if (quotedImage != null) {
                Image(
                    painter = painterResource(id = quotedImage),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .layoutId("image")
                        .size(60.dp)
                        .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
                )
            }
        },
    )
}

@Composable
private fun QuoteImageRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    image: @Composable (() -> Unit)? = null,
) {
    val finalContent = @Composable {
        if (image != null) {
            content()
            image.invoke()
        } else {
            content()
        }
    }

    Layout(modifier = modifier, content = finalContent) { measurables, constraints ->

        var imageIndex = -1

        val placeables = measurables.mapIndexed { index, measurable ->

            if (measurable.layoutId == "image") {
                imageIndex = index
            }

            measurable.measure(constraints = constraints.copy(minWidth = 0))
//            measurable.measure(constraints)
        }

        val size =
            placeables.fold(IntSize.Zero) { current: IntSize, placeable: Placeable ->

                IntSize(
                    width = current.width + placeable.width,
                    height = maxOf(current.height, placeable.height),
                )
            }

        val width = size.width.coerceAtLeast(constraints.minWidth)

        layout(width, size.height) {
            var x = 0

            placeables.forEachIndexed { index: Int, placeable: Placeable ->
                if (index != imageIndex) {
                    placeable.placeRelative(x, 0)
                    x += placeable.width
                } else {
                    placeable.placeRelative(width - placeable.width, 0)
                }
            }
        }
    }
}

@Composable
fun QuotedMessageWithConstraintLayout(
    modifier: Modifier = Modifier,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
) {
    val color = remember { getRandomColor() }

    val constraintSet = decoupledConstraints()

    ConstraintLayout(
        modifier = modifier,
        constraintSet = constraintSet,
    ) {
        Row(
            modifier = Modifier.layoutId("description"),
        ) {
            Surface(
                color = color,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp),
            ) {
            }

            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    "You",
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    letterSpacing = 1.sp,
                    overflow = TextOverflow.Ellipsis,
                )

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                    ) {
                        if (quotedImage != null) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Text(
                            color = Color(0xff757575),
                            text = quotedMessage ?: "Photo",
                            fontSize = 12.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        if (quotedImage != null) {
            Image(
                painter = painterResource(id = quotedImage),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .layoutId("image")
                    .size(60.dp)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
            )
        }
    }
}

private fun decoupledConstraints(): ConstraintSet {
    return ConstraintSet {
        val description = createRefFor("description")
        val image = createRefFor("image")

        constrain(description) {
            start.linkTo(parent.start)
            end.linkTo(image.start)
        }
        constrain(image) {
            end.linkTo(parent.end)
        }
    }
}
