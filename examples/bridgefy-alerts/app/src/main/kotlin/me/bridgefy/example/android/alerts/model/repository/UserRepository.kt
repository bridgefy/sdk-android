package me.bridgefy.example.android.alerts.model.repository

import kotlinx.coroutines.CoroutineScope
import me.bridgefy.example.android.alerts.inject.ApplicationScope
import me.bridgefy.example.android.alerts.model.db.main.MainDatabaseWrapper
import me.bridgefy.example.android.alerts.model.db.main.user.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val mainDatabaseWrapper: MainDatabaseWrapper,
    @ApplicationScope private val appScope: CoroutineScope,
) {

    private fun alertsDatabase() = mainDatabaseWrapper.getDatabase()
    private fun userDao() = alertsDatabase().userDao()
    suspend fun getUserData(): UserEntity? = userDao().getUser()
    suspend fun updateUserData(user: UserEntity) = userDao().updateUser(user)
    suspend fun deleteUserData() = userDao().deleteUser()
    fun getUserDataFlow() = userDao().getUserFlow()
    suspend fun saveUserData(user: UserEntity) = userDao().insertUser(user)
}
