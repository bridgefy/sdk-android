package me.bridgefy.example.android.alerts.model.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import me.bridgefy.example.android.alerts.util.datastore.PreferenceMigrations
import me.bridgefy.example.android.alerts.util.ext.enumValueOfOrDefault
import me.bridgefy.example.android.alerts.model.data.DisplayThemeType
import me.bridgefy.example.android.alerts.model.datastore.migration.DevicePreferenceMigration1To3
import me.bridgefy.example.android.alerts.model.datastore.migration.DevicePreferenceMigration2
import me.bridgefy.example.android.alerts.model.datastore.migration.DevicePreferenceMigration3
import me.bridgefy.example.android.alerts.model.datastore.migration.SharedPreferenceMigration
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DevicePreferenceDataSource
@Inject constructor(
    private val application: Application,
) {
    private val version = 3

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "device",
        produceMigrations = { applicationContext ->
            listOf(
                PreferenceMigrations.sharedPreferenceMigration(applicationContext, toVersion = 1, migrate = { sharedPrefs, currentData ->
                    SharedPreferenceMigration.migrateSharedPreferences(sharedPrefs, currentData)
                }),
                PreferenceMigrations(version, listOf(DevicePreferenceMigration2, DevicePreferenceMigration1To3, DevicePreferenceMigration3)),
            )
        },
        corruptionHandler = ReplaceFileCorruptionHandler {
            Timber.e(it, "DevicePreferenceDataSource Corrupted... recreating...")
            emptyPreferences()
        },
    )

    val themePref: DatastorePrefItem<DisplayThemeType> =
        DatastorePrefItem.createEnum(application.dataStore, Keys.THEME) {
            enumValueOfOrDefault(
                it,
                PrefsDefaults.SYSTEM_THEME_TYPE,
            )
        }
    val dynamicThemePref: DatastorePrefItem<Boolean> =
        DatastorePrefItem.create(application.dataStore, Keys.DYNAMIC_THEME, false)
    val lastInstalledVersionCodePref: DatastorePrefItem<Int> =
        DatastorePrefItem.create(application.dataStore, Keys.LAST_INSTALLED_VERSION_CODE, 0)
    val rangePref: DatastorePrefItem<Int> =
        DatastorePrefItem.create(application.dataStore, Keys.RANGE, 1)
    val workSchedulerVersionPref: DatastorePrefItem<Int> =
        DatastorePrefItem.create(application.dataStore, Keys.WORK_SCHEDULER_VERSION, 0)
    val developerModePref: DatastorePrefItem<Boolean> =
        DatastorePrefItem.create(application.dataStore, Keys.DEV_MODE, false)
    val appInstanceIdPref: DatastorePrefItem<String> = DatastorePrefItem.create(
        application.dataStore,
        Keys.APP_INSTANCE_ID,
        UUID.randomUUID().toString(),
    )

    val appInfoPref: DatastorePrefItem<AppInfo> = DatastorePrefItem.createCustom(
        dataStore = application.dataStore,
        read = { preferences ->
            AppInfo(
                preferences[Keys.DEV_MODE] ?: false,
                preferences[Keys.APP_INSTANCE_ID] ?: UUID.randomUUID().toString(),
                preferences[Keys.WORK_SCHEDULER_VERSION] ?: 0,
                preferences[Keys.LAST_INSTALLED_VERSION_CODE] ?: 0,
            )
        },
        write = { preferences, appInfo ->
            preferences[Keys.DEV_MODE] = appInfo.devMode
            preferences[Keys.APP_INSTANCE_ID] = appInfo.appInstanceId
            preferences[Keys.WORK_SCHEDULER_VERSION] = appInfo.workerVersion
            preferences[Keys.LAST_INSTALLED_VERSION_CODE] = appInfo.lastInstallVersionCode
        },
    )

    suspend fun clearAll() {
        application.dataStore.edit { it.clear() }
    }

    object Keys {
        val THEME = stringPreferencesKey("theme")
        val DYNAMIC_THEME = booleanPreferencesKey("dynamicTheme")
        val LAST_INSTALLED_VERSION_CODE = intPreferencesKey("lastInstalledVersionCode")
        val RANGE = intPreferencesKey("range")
        val WORK_SCHEDULER_VERSION = intPreferencesKey("workSchedulerVersion")
        val DEV_MODE = booleanPreferencesKey("devMode")
        val APP_INSTANCE_ID = stringPreferencesKey("appInstanceId")
    }
}

data class AppInfo(val devMode: Boolean, val appInstanceId: String, val workerVersion: Int, val lastInstallVersionCode: Int)
