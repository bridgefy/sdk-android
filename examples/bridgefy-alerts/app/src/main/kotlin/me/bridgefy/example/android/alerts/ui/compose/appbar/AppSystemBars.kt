package me.bridgefy.example.android.alerts.ui.compose.appbar

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import me.bridgefy.example.android.alerts.R
import timber.log.Timber

/**
 * Handle the colors of the OS system bars (statusbar and navbar) (for M3, Light/Dark themes, Dynamic themes)
 *
 * Notes:
 * 1. WindowCompat.setDecorFitsSystemWindows(window, false) should be place in MainActivity.onCreate()
 * 2. This function should be called from the top of AppTheme() { }
 */
@Composable
fun HandleSystemBarColors(darkTheme: Boolean) {
    val view = LocalView.current
    val activity = view.context.getActivity() ?: return

    if (Build.VERSION.SDK_INT in 23..26) {
        activity.window.statusBarColor = if (darkTheme) {
            activity.applicationContext.resources.getColor(
                R.color.bridgefy_light_bar_color,
            )
        } else {
            activity.applicationContext.resources.getColor(R.color.bridgefy_dark_bar_color)
        }
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !darkTheme
        activity.window.navigationBarColor = if (darkTheme) activity.applicationContext.resources.getColor(R.color.bridgefy_light_bar_color) else activity.applicationContext.resources.getColor(R.color.bridgefy_dark_bar_color)
    } else {
        activity.window.statusBarColor = if (darkTheme) activity.applicationContext.resources.getColor(R.color.bridgefy_light_bar_color) else activity.applicationContext.resources.getColor(R.color.bridgefy_dark_bar_color)
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !darkTheme

        activity.window.navigationBarColor = if (darkTheme) activity.applicationContext.resources.getColor(R.color.bridgefy_light_bar_color) else activity.applicationContext.resources.getColor(R.color.bridgefy_dark_bar_color)
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightNavigationBars = !darkTheme
    }
}

private fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> {
        Timber.e("No Activity Found")
        null
    }
}
