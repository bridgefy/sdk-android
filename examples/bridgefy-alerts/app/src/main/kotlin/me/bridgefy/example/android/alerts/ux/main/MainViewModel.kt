package me.bridgefy.example.android.alerts.ux.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.bridgefy.example.android.alerts.model.data.DisplayThemeType
import me.bridgefy.example.android.alerts.model.repository.SettingsRepository
import me.bridgefy.example.android.alerts.model.repository.UserRepository
import me.bridgefy.example.android.alerts.ui.navigation.DefaultNavBarConfig
import me.bridgefy.example.android.alerts.ui.navigation.ViewModelNavBar
import me.bridgefy.example.android.alerts.ui.navigation.ViewModelNavBarImpl
import me.bridgefy.example.android.alerts.util.ext.stateInDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import me.bridgefy.example.android.alerts.ux.main.NavBarItem

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    settingsRepository: SettingsRepository,
) : ViewModel(),
    ViewModelNavBar<NavBarItem> by ViewModelNavBarImpl(
        NavBarItem.IMAGE,
        DefaultNavBarConfig(
            NavBarItem.getNavBarItemRouteMap(),
        ),
    ) {
    val uiState = MainUiState(
        selectedAppThemeFlow = combine(
            settingsRepository.themeFlow.stateInDefault(viewModelScope, null),
            settingsRepository.dynamicThemeFlow.stateInDefault(viewModelScope, null),
        ) { displayThemeType, dynamicTheme ->
            SelectedAppTheme(displayThemeType ?: DisplayThemeType.SYSTEM_DEFAULT, dynamicTheme ?: false)
        }.stateInDefault(viewModelScope, null),
    )
    fun hasSession() = userRepository.getUserDataFlow()
}

data class SelectedAppTheme(val displayThemeType: DisplayThemeType, val dynamicTheme: Boolean)
