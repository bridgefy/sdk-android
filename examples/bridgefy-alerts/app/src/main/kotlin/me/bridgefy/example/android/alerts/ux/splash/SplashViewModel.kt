package me.bridgefy.example.android.alerts.ux.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.bridgefy.example.android.alerts.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val mutableStateFlow = MutableStateFlow(true)
    val isLoading = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            mutableStateFlow.value = false
        }
    }

    suspend fun getUserData() = userRepository.getUserData()
}
