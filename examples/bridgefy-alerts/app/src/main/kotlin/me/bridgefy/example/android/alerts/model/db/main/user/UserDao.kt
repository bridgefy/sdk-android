package me.bridgefy.example.android.alerts.model.db.main.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Update
    suspend fun updateUser(userEntity: UserEntity): Int

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM User LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Query("DELETE FROM User")
    suspend fun deleteUser()
}
