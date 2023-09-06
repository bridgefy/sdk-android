package me.bridgefy.example.android.alerts.model.db.main.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MainMigration2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // ONLY views are changed
    }
}
