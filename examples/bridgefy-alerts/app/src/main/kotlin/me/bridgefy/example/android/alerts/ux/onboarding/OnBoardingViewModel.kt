package me.bridgefy.example.android.alerts.ux.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    val uiState = OnBoardingUiState(onBoardingButtonClicked = {},)

    val scope = viewModelScope
}
