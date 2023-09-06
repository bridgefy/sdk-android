package me.bridgefy.example.android.alerts.ui.compose.appnavbar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import me.bridgefy.example.android.alerts.R

@Composable
fun <T : Enum<T>> RowScope.AppBottomNavigationItem(
    navBarItem: T,
    unselectedImageVector: ImageVector,
    selectedImageVector: ImageVector,
    selectedBarItem: T?,
    @StringRes textResId: Int? = null,
    text: String? = null,
    onNavItemClicked: (T) -> Unit,
) {
    val selected = selectedBarItem == navBarItem

    NavigationBarItem(
        icon = { if (selected) Icon(selectedImageVector, contentDescription = null, tint = colorResource(id = R.color.bridgefy_color)) else Icon(unselectedImageVector, contentDescription = null, tint = Color.White) },
        label = {
            when {
                text != null -> Text(text, maxLines = 1, color = Color.White)
                textResId != null -> Text(stringResource(textResId), maxLines = 1, color = Color.White)
            }
        },
        selected = selected,
        onClick = {
            onNavItemClicked(navBarItem)
        },
    )
}
