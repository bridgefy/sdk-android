package me.bridgefy.example.android.alerts.model.db.main.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("User")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val userName: String?,
    var admin: Int,
)
