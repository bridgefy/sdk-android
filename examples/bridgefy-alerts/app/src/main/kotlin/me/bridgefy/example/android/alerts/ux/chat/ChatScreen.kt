package me.bridgefy.example.android.alerts.ux.chat

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppBarMenu
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppBarMenuItem
import me.bridgefy.example.android.alerts.util.ext.requireActivity
import me.bridgefy.example.android.alerts.ux.MainAppScaffoldWithNavBar
import me.bridgefy.example.android.alerts.ux.chat.chatcomponets.ChatInput
import me.bridgefy.example.android.alerts.ux.chat.chatcomponets.ReceivedMessageRow
import me.bridgefy.example.android.alerts.ux.chat.chatcomponets.SentMessageRow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import me.bridgefy.example.android.alerts.R

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel(LocalContext.current.requireActivity()),
) {
    val uiState = viewModel.uiState
    val appBarMenuItems = listOf(
        AppBarMenuItem.Icon(
            Icons.Outlined.Clear,
            androidx.compose.ui.R.string.close_drawer,
        ) { uiState.onDeleteChat() },
        AppBarMenuItem.OverflowMenuItem(R.string.remote_option) { navController.popBackStack() },
    )
    MainAppScaffoldWithNavBar(
        title = "Chat",
        navigationIconVisible = false,
        actions = { AppBarMenu(menuItems = appBarMenuItems) },
        onNavigationClick = { navController.popBackStack() },
        hideNavigation = true,
    ) {
        ChatContent(uiState)
    }
}

@Composable
private fun ChatContent(
    uiState: ChatUiState,
) {
    val sdf = remember { SimpleDateFormat("hh:mm", Locale.ROOT) }
    val conversation by uiState.getConversationFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.chatbackground),
                contentScale = ContentScale.FillBounds,
            ),
    ) {
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            // contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
        ) {
            items(conversation) { messages ->
                if (messages.receiverId != null) {
                    SentMessageRow(
                        text = messages.message ?: "empty sent message",
                        messageTime = sdf.format(System.currentTimeMillis()),
                    )
                }
                if (messages.senderId != null) {
                    ReceivedMessageRow(
                        text = messages.message ?: "empty received message",
                        contactName = messages.senderName,
                        messageTime = sdf.format(System.currentTimeMillis()),
                    )
                    LaunchedEffect(key1 = conversation) {
                        coroutineScope.launch {
                            if (conversation.isNotEmpty()) {
                                scrollState.animateScrollToItem(conversation.size - 1)
                            }
                        }
                    }
                }
            }
        }
        val context = LocalContext.current
        ChatInput(
            onMessageChange = { messageContent ->
                sendMessageCommand(messageContent, context)
                coroutineScope.launch {
                    if (conversation.isNotEmpty()) {
                        scrollState.animateScrollToItem(conversation.size - 1)
                    }
                }
            },
        )
    }
}

fun sendMessageCommand(message: String, context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SDK_SEND_MESSAGE
            putExtra("message", message)
        },
    )
}
