package me.bridgefy.example.android.alerts.model.datastore.migration

import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.preferences.core.Preferences
import me.bridgefy.example.android.alerts.model.datastore.DevicePreferenceDataSource
import timber.log.Timber

object SharedPreferenceMigration {
    fun migrateSharedPreferences(sharedPrefs: SharedPreferencesView, currentData: Preferences): Preferences {
        Timber.d("Migrating LEGACY shared preferences...")
        val mutablePreferences = currentData.toMutablePreferences()

        sharedPrefs.getAll().forEach { (key, value) ->
            when {
                key == "displayThemeType" && value is String -> {
                    mutablePreferences[DevicePreferenceDataSource.Keys.THEME] = value
                }
                key == "lastInstalledVersionCode" && value is Int -> {
                    mutablePreferences[DevicePreferenceDataSource.Keys.LAST_INSTALLED_VERSION_CODE] = value
                }
                key == "workSchedulerVersion" && value is Int -> {
                    mutablePreferences[DevicePreferenceDataSource.Keys.WORK_SCHEDULER_VERSION] = value
                }
                key == "appInstanceId" && value is String -> {
                    mutablePreferences[DevicePreferenceDataSource.Keys.APP_INSTANCE_ID] = value
                }
            }
        }

        Timber.d("Finished migrating LEGACY shared preferences.")
        return mutablePreferences.toPreferences()
    }
}
