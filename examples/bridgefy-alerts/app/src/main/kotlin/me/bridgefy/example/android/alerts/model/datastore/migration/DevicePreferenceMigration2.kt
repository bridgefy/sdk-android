package me.bridgefy.example.android.alerts.model.datastore.migration

import androidx.datastore.preferences.core.Preferences
import me.bridgefy.example.android.alerts.util.datastore.PreferenceMigration

object DevicePreferenceMigration2 : PreferenceMigration(1, 2) {
    override suspend fun migrate(currentData: Preferences): Preferences {
        val mutablePreferences = currentData.toMutablePreferences()

        // do preference migrations from version 1 to 2 here

        return mutablePreferences.toPreferences()
    }
}
