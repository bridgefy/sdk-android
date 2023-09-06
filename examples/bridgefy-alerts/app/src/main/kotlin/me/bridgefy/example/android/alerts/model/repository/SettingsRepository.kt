package me.bridgefy.example.android.alerts.model.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.bridgefy.example.android.alerts.inject.ApplicationScope
import me.bridgefy.example.android.alerts.model.data.DisplayThemeType
import me.bridgefy.example.android.alerts.model.datastore.DevicePreferenceDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository
@Inject constructor(
    private val devicePreferenceDataSource: DevicePreferenceDataSource,
    @ApplicationScope private val appScope: CoroutineScope,
) {
    val themeFlow: Flow<DisplayThemeType> get() = devicePreferenceDataSource.themePref.flow
    val dynamicThemeFlow: Flow<Boolean> get() = devicePreferenceDataSource.dynamicThemePref.flow
    suspend fun getLastInstalledVersionCode(): Int = devicePreferenceDataSource.lastInstalledVersionCodePref.flow.first()
    fun setLastInstalledVersionCodeAsync(version: Int) = appScope.launch { devicePreferenceDataSource.lastInstalledVersionCodePref.setValue(version) }
}
