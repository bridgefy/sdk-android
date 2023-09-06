package me.bridgefy.example.android.alerts.model.datastore

import android.os.Build
import me.bridgefy.example.android.alerts.model.data.DisplayThemeType

object PrefsDefaults {
    val SYSTEM_THEME_TYPE = getSystemThemeTypeDefault()

    @Deprecated("This is only required for systems that don't support a system dark mode", ReplaceWith("DisplayThemeType.SYSTEM_DEFAULT"))
    private fun getSystemThemeTypeDefault(): DisplayThemeType {
        return if (Build.VERSION.SDK_INT > 28) {
            // support Android Q System Theme
            DisplayThemeType.SYSTEM_DEFAULT
        } else {
            DisplayThemeType.LIGHT
        }
    }
}
