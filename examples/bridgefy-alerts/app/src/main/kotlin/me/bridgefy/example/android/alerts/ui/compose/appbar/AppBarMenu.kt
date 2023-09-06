package me.bridgefy.example.android.alerts.ui.compose.appbar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.bridgefy.example.android.alerts.ui.compose.menu.OverflowMenuItem
import me.bridgefy.example.android.alerts.ui.compose.menu.OverflowMenuItemsContent
import me.bridgefy.example.android.alerts.R

@Composable
fun AppBarMenu(menuItems: List<AppBarMenuItem>) {
    val itemsOnBar = menuItems.filter { !it.isOverFlowItem() }
    val overflowMenuItems = menuItems.filter { it.isOverFlowItem() }

    // show items on bar first (in the order received)
    itemsOnBar.forEach {
        when (it) {
            is AppBarMenuItem.Icon -> AppBarIcon(it)
            is AppBarMenuItem.Text -> AppBarText(it)
            is AppBarMenuItem.TextButton -> AppBarTextButton(it)
            else -> error("Unexpected item (filter failed)")
        }
    }

    // Show overflow items
    AppBarOverflowMenu(overflowMenuItems)
}

@Composable
fun AppBarIcon(imageVector: ImageVector, contentDescription: String, action: () -> Unit) {
    PlainTooltipBox(
        tooltip = { Text(contentDescription) },
    ) {
        IconButton(
            onClick = action,
            modifier = Modifier.tooltipAnchor(),
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
            )
        }
    }
}

@Composable
fun AppBarIcon(menuItem: AppBarMenuItem.Icon) {
    PlainTooltipBox(
        tooltip = { Text(menuItem.text()) },
    ) {
        IconButton(
            onClick = { menuItem.action() },
            modifier = Modifier.tooltipAnchor(),
        ) {
            Icon(
                imageVector = menuItem.imageVector,
                contentDescription = menuItem.text(),
            )
        }
    }
}

@Composable
fun AppBarText(text: String, colors: ButtonColors? = null, action: () -> Unit) {
    TextButton(
        onClick = action,
        colors = colors ?: ButtonDefaults.textButtonColors(),
    ) {
        Text(text)
    }
}

@Composable
fun AppBarText(menuItem: AppBarMenuItem.Text) {
    AppBarText(text = menuItem.text(), colors = menuItem.colors, action = menuItem.action)
}

@Composable
fun AppBarTextButton(text: String, colors: ButtonColors? = null, action: () -> Unit) {
    Row {
        Button(
            onClick = action,
            colors = colors ?: ButtonDefaults.buttonColors(),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 8.dp,
                end = 12.dp,
                bottom = 8.dp,
            ),
        ) {
            Text(text)
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
fun AppBarTextButton(menuItem: AppBarMenuItem.TextButton) {
    AppBarTextButton(text = menuItem.text(), colors = menuItem.colors, action = menuItem.action)
}

@Composable
fun AppBarOverflowMenu(menuItems: List<AppBarMenuItem>) {
    if (menuItems.isEmpty()) {
        return
    }

    val expanded = remember { mutableStateOf(false) }

    IconButton(onClick = {
        expanded.value = true
    }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more_options),
        )
    }
    DropdownMenu(
        expanded = expanded.value,
        offset = DpOffset((-40).dp, (-40).dp),
        onDismissRequest = { expanded.value = false },
    ) {
        // Convert AppBarMenuItem to OverflowMenuItem
        val overflowMenuItems: List<OverflowMenuItem> = menuItems
            .filter { it.isOverFlowItem() }
            .map {
                when (it) {
                    is OverflowMenuItem.MenuItem -> it
                    is OverflowMenuItem.Divider -> it
                    else -> error("Unsupported OverflowMenuItem type")
                }
            }

        OverflowMenuItemsContent(overflowMenuItems, expanded)
    }
}

sealed interface AppBarMenuItem {
    class Icon(val imageVector: ImageVector, val text: @Composable () -> String, val action: () -> Unit) :
        AppBarMenuItem {
        constructor(imageVector: ImageVector, @StringRes textId: Int, action: () -> Unit) : this(imageVector = imageVector, text = { stringResource(textId) }, action = action)
    }

    /**
     * If setting colors, consider using ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
     */
    class Text(val text: @Composable () -> String, val colors: ButtonColors? = null, val action: () -> Unit) :
        AppBarMenuItem {
        constructor(@StringRes textId: Int, colors: ButtonColors? = null, action: () -> Unit) : this(text = { stringResource(textId) }, colors = colors, action = action)
    }

    /**
     * If setting colors, consider using ButtonDefaults.buttonColors(contentColor = LocalContentColor.current)
     */
    class TextButton(val text: @Composable () -> String, val colors: ButtonColors? = null, val action: () -> Unit) :
        AppBarMenuItem {
        constructor(@StringRes textId: Int, colors: ButtonColors? = null, action: () -> Unit) : this(text = { stringResource(textId) }, colors = colors, action = action)
    }

    class OverflowMenuItem(
        override val text: @Composable () -> String,
        override val leadingIcon: ImageVector? = null,
        override val trailingIcon: ImageVector? = null,
        override val action: () -> Unit,
    ) : AppBarMenuItem, me.bridgefy.example.android.alerts.ui.compose.menu.OverflowMenuItem.MenuItem(text, leadingIcon, trailingIcon, action) {
        constructor(@StringRes textId: Int, leadingIcon: ImageVector? = null, trailingIcon: ImageVector? = null, action: () -> Unit) : this(
            text = { stringResource(textId) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            action = action,
        )
    }

    object OverflowDivider : AppBarMenuItem, me.bridgefy.example.android.alerts.ui.compose.menu.OverflowMenuItem.Divider()

    fun isOverFlowItem(): Boolean = this is OverflowMenuItem || this is OverflowDivider
}
