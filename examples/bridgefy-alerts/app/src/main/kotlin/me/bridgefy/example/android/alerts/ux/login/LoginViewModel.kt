package me.bridgefy.example.android.alerts.ux.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.bridgefy.example.android.alerts.model.db.main.user.UserEntity
import me.bridgefy.example.android.alerts.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val uiState = LoginUiState(
        onStartClicked = { userName ->
            viewModelScope.launch(Dispatchers.IO) { onStartClicked(userName) }
        },
    )

    private suspend fun onStartClicked(userName: String) { userRepository.saveUserData(UserEntity(UUID.randomUUID().toString(), userName, 0)) }
}
