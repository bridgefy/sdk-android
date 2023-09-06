package me.bridgefy.example.android.alerts.ux.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import me.bridgefy.example.android.alerts.ui.navigation.HandleNavBarNavigation
import me.bridgefy.example.android.alerts.util.ext.requireActivity
import me.bridgefy.example.android.alerts.ux.NavGraph

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(LocalContext.current.requireActivity()),
) {
    val navController = rememberNavController()

    NavGraph(navController)

    HandleNavBarNavigation(viewModel, navController)
}
