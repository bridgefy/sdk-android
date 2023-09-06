package me.bridgefy.example.android.alerts.model.db.main.messages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messageEntity: MessageEntity): Long

    @Update
    suspend fun update(messageEntity: MessageEntity)

    @Query("DELETE FROM Messages WHERE chatRoomId = :chatRoomId")
    suspend fun deleteConversation(chatRoomId: String)

    @Query("DELETE FROM MESSAGES")
    fun deleteAllMessages(): Int

    @Delete
    suspend fun delete(messageEntity: MessageEntity)

    @Query("SELECT * FROM Messages WHERE chatRoomId = :chatRoom")
    fun getConversation(chatRoom: String): Flow<List<MessageEntity>>
}
