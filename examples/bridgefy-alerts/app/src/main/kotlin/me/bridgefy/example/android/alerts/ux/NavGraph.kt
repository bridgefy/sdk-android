package me.bridgefy.example.android.alerts.ux

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.pager.ExperimentalPagerApi
import me.bridgefy.example.android.alerts.ui.navigation.NavUriLogger
import me.bridgefy.example.android.alerts.ux.chat.ChatRoute
import me.bridgefy.example.android.alerts.ux.chat.ChatScreen
import me.bridgefy.example.android.alerts.ux.color.SendColorRoute
import me.bridgefy.example.android.alerts.ux.color.SendColorScreen
import me.bridgefy.example.android.alerts.ux.flash.SendFlashRoute
import me.bridgefy.example.android.alerts.ux.flash.SendFlashScreen
import me.bridgefy.example.android.alerts.ux.home.HomeScreen
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute
import me.bridgefy.example.android.alerts.ux.image.SendImageRoute
import me.bridgefy.example.android.alerts.ux.image.SendImageScreen
import me.bridgefy.example.android.alerts.ux.login.LoginRoute
import me.bridgefy.example.android.alerts.ux.login.LoginScreen
import me.bridgefy.example.android.alerts.ux.onboarding.OnBoardingRoute
import me.bridgefy.example.android.alerts.ux.onboarding.OnBoardingScreen
import me.bridgefy.example.android.alerts.ux.sound.SendSoundRoute
import me.bridgefy.example.android.alerts.ux.sound.SendSoundScreen
import me.bridgefy.example.android.alerts.ux.splash.SplashRoute
import me.bridgefy.example.android.alerts.ux.splash.SplashScreen
import me.bridgefy.example.android.alerts.ux.text.SendTextRoute
import me.bridgefy.example.android.alerts.ux.text.SendTextScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    // Debug navigation
    navController.addOnDestinationChangedListener(NavUriLogger())

    NavHost(
        navController = navController,
        startDestination = SplashRoute.createRoute(),
    ) {
        OnBoardingRoute.addNavigationRoute(this) { OnBoardingScreen(navController) }
        LoginRoute.addNavigationRoute(this) { LoginScreen(navController) }
        SplashRoute.addNavigationRoute(this) { SplashScreen(navController) }

        HomeScreenRoute.addNavigationRoute(this) { HomeScreen(navController) }
        ChatRoute.addNavigationRoute(this) { ChatScreen(navController) }
        SendImageRoute.addNavigationRoute(this) { SendImageScreen(navController) }
        SendTextRoute.addNavigationRoute(this) { SendTextScreen(navController) }
        SendFlashRoute.addNavigationRoute(this) { SendFlashScreen(navController) }
        SendSoundRoute.addNavigationRoute(this) { SendSoundScreen(navController) }
        SendColorRoute.addNavigationRoute(this) { SendColorScreen(navController) }
    }
}
