package me.bridgefy.example.android.alerts.model.datastore.migration

import androidx.datastore.preferences.core.Preferences
import me.bridgefy.example.android.alerts.util.datastore.PreferenceMigration

object DevicePreferenceMigration3 : PreferenceMigration(2, 3) {
    override suspend fun migrate(currentData: Preferences): Preferences {
        val mutablePreferences = currentData.toMutablePreferences()

        // do preference migrations from version 2 to 3 here

        return mutablePreferences.toPreferences()
    }
}
