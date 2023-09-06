package me.bridgefy.example.android.alerts

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.bridgefy.Bridgefy

@HiltAndroidApp
class App : Application() {
    internal val bridgefy by lazy { Bridgefy(this) }
}
