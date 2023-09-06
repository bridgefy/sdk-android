package me.bridgefy.example.android.alerts.ux.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute
import me.bridgefy.example.android.alerts.ux.onboarding.OnBoardingRoute
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController? = null,
    viewModel: SplashViewModel = hiltViewModel(),
) = Box(
    Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
) {
    LaunchedEffect(key1 = true) {
        viewModel.viewModelScope.launch {
            val userData = viewModel.getUserData()
            if (userData != null) {
                navController?.navigate(HomeScreenRoute.createRoute()) {
                    popUpTo(SplashRoute.createRoute()) {
                        inclusive = true
                    }
                }
            } else {
                navController?.navigate(OnBoardingRoute.createRoute()) {
                    popUpTo(SplashRoute.createRoute()) {
                        inclusive = true
                    }
                }
            }
        }
    }
}
