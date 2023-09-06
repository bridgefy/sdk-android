package me.bridgefy.example.android.alerts.ux.sound

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ux.MainAppScaffoldWithNavBar
import me.bridgefy.example.android.alerts.ux.flash.showMainScreenCommand
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute
import kotlin.time.Duration.Companion.seconds

@Composable
fun SendSoundScreen(
    navController: NavController,
) {
    MainAppScaffoldWithNavBar(
        title = "Amin Panel",
        hideNavigation = false,
        navigationIconVisible = true,
        onNavigationClick = {
            navController.navigate(HomeScreenRoute.createRoute()) {
                popUpTo(SendSoundRoute.createRoute()) {
                    inclusive = true
                }
            }
        },
    ) {
        SoundContent()
    }
}

@SuppressLint("NewApi")
@Composable
fun SoundCommandView(scope: CoroutineScope, context: Context) {
    var showImage by remember { mutableStateOf(false) }
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
        LaunchedEffect(key1 = Unit) {
            scope.launch {
                showImage = true
                val alarmSound = MediaPlayer.create(context, R.raw.sound)
                alarmSound.start()
                delay(4000)
                alarmSound.stop()
                showImage = false
                showMainScreenCommand(context)
            }
        }

        if (showImage) {
            Image(
                painter = painterResource(id = R.drawable.img_music),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun SoundContent() {
    val context = LocalContext.current
    var ticks by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
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
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Press button to play an alert on remote devices.",
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = ticks == 0 || ticks > 5,
            onClick = {
                scope.launch {
                    while (true) {
                        if (ticks <= 5) {
                            delay(1.seconds)
                            ticks++
                        } else {
                            ticks = 0
                            break
                        }
                    }
                }
                sendSoundCommand(context)
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 34.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp),
                text = "PLAY A SOUND",
                color = Color.White,
                style = MaterialTheme.typography.button,
            )
        }
    }
}

fun sendSoundCommand(context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SDK_SEND_SOUND
            putExtra("sound", "")
        },
    )
}
