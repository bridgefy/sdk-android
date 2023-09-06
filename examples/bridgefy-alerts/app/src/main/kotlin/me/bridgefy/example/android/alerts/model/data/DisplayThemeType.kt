package me.bridgefy.example.android.alerts.model.data

import android.content.Context
import me.bridgefy.example.android.alerts.R

enum class DisplayThemeType(private val textResId: Int) {
    SYSTEM_DEFAULT(R.string.system_default),
    LIGHT(R.string.light),
    DARK(R.string.dark),
    ;

    fun getString(context: Context): String {
        return context.getText(this.textResId).toString()
    }
}
