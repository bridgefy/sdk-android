package me.bridgefy.example.android.alerts.ux.flash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ux.MainAppScaffoldWithNavBar
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute
import kotlin.time.Duration.Companion.seconds

@Composable
fun SendFlashScreen(
    navController: NavController,
) {
    MainAppScaffoldWithNavBar(
        title = "Amin Panel",
        hideNavigation = false,
        navigationIconVisible = true,
        onNavigationClick = {
            navController.navigate(HomeScreenRoute.createRoute()) {
                popUpTo(SendFlashRoute.createRoute()) {
                    inclusive = true
                }
            }
        },
    ) {
        FlashLightContent()
    }
}

@Composable
private fun FlashLightContent() {
    val context = LocalContext.current
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
        val scope = rememberCoroutineScope()
        var ticks by remember { mutableIntStateOf(0) }
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "* Flash light on controlled devices will be turned on for 15 seconds.",
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = ticks == 0 || ticks > 15,
            onClick = {
                sendFlashCommand(context)
                scope.launch {
                    while (true) {
                        if (ticks <= 15) {
                            delay(1.seconds)
                            ticks++
                        } else {
                            ticks = 0
                            break
                        }
                    }
                }
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
                text = if (ticks == 0 || ticks > 15) "TURN ON FLASHLIGHT" else "Wait ${15 - ticks} seconds",
                color = Color.White,
                style = MaterialTheme.typography.button,
            )
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun FlashCommandView(scope: CoroutineScope, context: Context) {
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
                val hasFlash =
                    context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
                if (hasFlash) {
                    val cameraManager =
                        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    val cameraID = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraID, true)
                    showImage = true
                }
                delay(15000)
                if (hasFlash) {
                    val cameraManager =
                        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    val cameraID = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraID, false)
                    showImage = false
                    showMainScreenCommand(context)
                }
            }
        }

        if (showImage) {
            Image(
                painter = painterResource(id = R.drawable.ic_flashlight),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

fun sendFlashCommand(context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SDK_SEND_FLASH
            putExtra("flash", "")
        },
    )
}

fun showMainScreenCommand(context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SHOW_MAIN_SCREEN
        },
    )
}
