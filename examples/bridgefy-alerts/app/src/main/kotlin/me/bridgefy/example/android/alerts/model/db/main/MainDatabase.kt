package me.bridgefy.example.android.alerts.model.db.main

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.bridgefy.example.android.alerts.model.db.converter.DateTimeTextConverter
import me.bridgefy.example.android.alerts.model.db.main.messages.MessageEntity
import me.bridgefy.example.android.alerts.model.db.main.messages.MessagesDao
import me.bridgefy.example.android.alerts.model.db.main.user.UserDao
import me.bridgefy.example.android.alerts.model.db.main.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
        MessageEntity::class,
    ],
    version = 1,
)
@TypeConverters(DateTimeTextConverter::class)
abstract class MainDatabase : RoomDatabase() {

    abstract fun chatDao(): MessagesDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "main.db"
    }
}
