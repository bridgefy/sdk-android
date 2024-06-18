package me.bridgefy.example.android.alerts.ux.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.bridgefy.example.android.alerts.model.data.DisplayThemeType
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ui.notification.NotificationChannels
import me.bridgefy.example.android.alerts.ui.theme.AppTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var bridgefyService: BridgefyService? = null
    private var mBound = false

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder: BridgefyService.LocalBinder = service as BridgefyService.LocalBinder
            bridgefyService = binder.getService()
            mBound = true
            bringServiceToForeground()
        }

        override fun onServiceDisconnected(p0: ComponentName?) { mBound = false }
    }

    override fun onPause() {
        super.onPause()
        if (mBound) {
            unbindService(connection)
            mBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    private fun stopService() {
        startService(
            Intent(this, BridgefyService::class.java).apply {
                action = BridgefyService.STOP_SERVICE
            },
        )
    }

    private fun bindWithService() {
        viewModel.viewModelScope.launch {
            viewModel.hasSession().collect { user ->
                if (user != null) {
                    val intent = Intent(this@MainActivity, BridgefyService::class.java)
                    bindService(intent, connection, BIND_IMPORTANT)
                }
            }
        }
    }

    private fun bringServiceToForeground() {
        bridgefyService?.let {
            if (!it.isForegroundService) {
                val intent = Intent(this, BridgefyService::class.java)
                intent.action = BridgefyService.FOREGROUND_SERVICE
                ContextCompat.startForegroundService(this, intent)
                it.doForegroundThings()
            } else {
                Timber.log(Log.WARN, "Service is already in foreground")
            }
        } ?: Timber.log(Log.WARN, "Service is null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which allows us to handle insets, including IME animations (OS statusbar and nav bar colors is handled by app)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        NotificationChannels.registerAllChannels(this)

        installSplashScreen()

        setContent {
            val uiState = viewModel.uiState
            val theme by uiState.selectedAppThemeFlow.collectAsStateWithLifecycle()

            val darkTheme = when (theme?.displayThemeType) {
                DisplayThemeType.SYSTEM_DEFAULT -> isSystemInDarkTheme()
                DisplayThemeType.LIGHT -> false
                DisplayThemeType.DARK -> true
                null -> isSystemInDarkTheme()
            }

            val dynamicTheme = when (theme?.dynamicTheme) {
                true -> true
                else -> false
            }

            AppTheme(darkTheme, dynamicTheme) {
                MainScreen()
            }
        }

        bindWithService()
    }
}
