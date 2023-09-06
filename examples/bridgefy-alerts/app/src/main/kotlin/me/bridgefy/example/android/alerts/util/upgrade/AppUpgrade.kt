package me.bridgefy.example.android.alerts.util.upgrade

import me.bridgefy.example.android.alerts.inject.IoDispatcher
import me.bridgefy.example.android.alerts.model.repository.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import me.bridgefy.example.android.alerts.BuildConfig

class AppUpgrade
@Inject constructor(
    private val settingsRepository: SettingsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    fun upgradeApp() = runBlocking(ioDispatcher) {
        val lastInstalledVersionCode = settingsRepository.getLastInstalledVersionCode()
        Timber.i("Checking for app upgrade from [%d]", lastInstalledVersionCode)

        if (lastInstalledVersionCode == 0) {
            Timber.i("Skipping app upgrade on fresh install")
            settingsRepository.setLastInstalledVersionCodeAsync(BuildConfig.VERSION_CODE)
            return@runBlocking
        }

//        if (lastInstalledVersionCode < VERSION_CODE_HERE) {
//            migrateXXX()
//        }

        // set the current version
        settingsRepository.setLastInstalledVersionCodeAsync(BuildConfig.VERSION_CODE)
    }
}
