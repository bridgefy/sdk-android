package me.bridgefy.example.android.alerts.model.db.main.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.dbtools.android.room.ext.dropAllViews

class MainMigration3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // BOTH views and tables are changed

        // drop views
        db.dropAllViews()
    }
}
