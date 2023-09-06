package me.bridgefy.example.android.alerts.ui.notification // ktlint-disable package-name

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import me.bridgefy.example.android.alerts.R

enum class NotificationChannels constructor(
    val channelId: String,
    @StringRes val textResId: Int,
    val importance: Int,
) {
    GENERAL("general_channel", R.string.notification_channel_general, NotificationManagerCompat.IMPORTANCE_DEFAULT),
    ;

    companion object {
        fun registerAllChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= 26) {
                val notificationManager = context.getSystemService<NotificationManager>()

                values().forEach {
                    val channel = NotificationChannel(it.channelId, context.getString(it.textResId), it.importance)
                    notificationManager?.createNotificationChannel(channel)
                }
            }
        }
    }
}
