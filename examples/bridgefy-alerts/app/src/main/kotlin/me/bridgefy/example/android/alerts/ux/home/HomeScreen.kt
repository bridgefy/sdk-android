package me.bridgefy.example.android.alerts.ux.home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppBarMenu
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppBarMenuItem
import me.bridgefy.example.android.alerts.ui.compose.dialog.HandleDialogUiState
import me.bridgefy.example.android.alerts.ux.MainAppScaffoldWithNavBar
import me.bridgefy.example.android.alerts.ux.chat.ChatRoute
import me.bridgefy.example.android.alerts.ux.color.ColorCommandView
import me.bridgefy.example.android.alerts.ux.flash.FlashCommandView
import me.bridgefy.example.android.alerts.ux.image.ImageCommandView
import me.bridgefy.example.android.alerts.ux.sound.SoundCommandView
import me.bridgefy.example.android.alerts.ux.text.TextCommandView

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val mContext = LocalContext.current
    viewModel.initNavController(navController)
    val uiState = viewModel.uiState
    var commandReceived by remember { mutableStateOf("") }
    var commandValue by remember { mutableStateOf("") }
    val appBarMenuItems = listOf(
        AppBarMenuItem.Icon(
            ImageVector.vectorResource(id = R.drawable.admin),
            androidx.compose.ui.R.string.close_drawer,
        ) { uiState.onAdminClicked() },
        AppBarMenuItem.OverflowMenuItem(R.string.chat_option) {
            navController.navigate(ChatRoute.createRoute())
        },
    )
    val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            commandReceived = ""
            commandValue = ""
            intent.getStringExtra("color")?.let {
                commandReceived = "color"
                commandValue = it
            }
            intent.getStringExtra("image")?.let {
                commandReceived = "image"
                commandValue = it
            }
            intent.getStringExtra("sound")?.let {
                commandReceived = "sound"
            }
            intent.getStringExtra("text")?.let {
                commandReceived = "text"
                commandValue = it
            }
            intent.getStringExtra("flash")?.let {
                commandReceived = "flash"
            }
            intent.getStringExtra("show_main")?.let {
                commandReceived = "main"
            }
        }
    }

    LocalBroadcastManager.getInstance(LocalContext.current).registerReceiver(
        broadcastReceiver,
        IntentFilter("command_intent"),
    )
    SendInitBridgefyCommand()
    MainAppScaffoldWithNavBar(
        title = "Bridgefy Remote Control",
        navigationIconVisible = false,
        hideNavigation = true,
        actions = { AppBarMenu(appBarMenuItems) },
        onNavigationClick = { navController.popBackStack() },
    ) {
        if (commandReceived == "image" && commandValue.isNotEmpty()) {
            ImageCommandView(image = commandValue)
        } else if (commandReceived == "color" && commandValue.isNotEmpty()) {
            ColorCommandView(color = commandValue)
        } else if (commandReceived == "text" && commandValue.isNotEmpty()) {
            TextCommandView(text = commandValue)
        } else if (commandReceived == "sound" && commandValue.isEmpty()) {
            SoundCommandView(scope = viewModel.viewModelScope, context = mContext)
        } else if (commandReceived == "show_main" && commandValue.isEmpty()) {
            MainScreen()
        } else if (commandReceived == "flash" && commandValue.isEmpty()) {
            FlashCommandView(viewModel.viewModelScope, mContext)
        }
    }
    HandleDialogUiState(uiState.dialogUiStateFlow)
}

@Composable
fun MainScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 40.dp,
                end = 40.dp,
                bottom = 40.dp,
            ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.bridgefy_complete_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun SendInitBridgefyCommand() {
    val context = LocalContext.current
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.INIT_SDK
        },
    )
}
