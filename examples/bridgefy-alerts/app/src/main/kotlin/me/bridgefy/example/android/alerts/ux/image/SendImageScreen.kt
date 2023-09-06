package me.bridgefy.example.android.alerts.ux.image

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ux.MainAppScaffoldWithNavBar
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute

@Composable
fun SendImageScreen(
    navController: NavController,
) {
    MainAppScaffoldWithNavBar(
        title = "Amin Panel",
        hideNavigation = false,
        navigationIconVisible = true,
        onNavigationClick = {
            navController.navigate(HomeScreenRoute.createRoute()) {
                popUpTo(SendImageRoute.createRoute()) {
                    inclusive = true
                }
            }
        },
    ) {
        ImageScreenContent()
    }
}

@Composable
fun ImageCommandView(image: String) {
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
            painter = painterResource(id = if (image == "0") R.drawable.img_map else R.drawable.img_tips),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun ImageScreenContent() {
    var mapSelected by remember { mutableStateOf(false) }
    var tipsSelected by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val mapButtonColor = if (mapSelected) Color.Red else Color.White
    val tipsButtonColor = if (tipsSelected) Color.Red else Color.White

    val mapTextColor = if (mapSelected) Color.White else Color.Red
    val tipsTextColor = if (tipsSelected) Color.White else Color.Red

    val imageSource = if (mapSelected) R.drawable.img_map else R.drawable.img_tips

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
        Text(
            text = "Tap a button to send an image",
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )

        Row() {
            OutlinedButton(
                border = BorderStroke(1.dp, Color.Red),
                onClick = {
                    mapSelected = !mapSelected
                    tipsSelected = false
                    sendImageCommand("0", context)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = mapButtonColor),
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 30.dp, bottom = 34.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = "MAP",
                    color = mapTextColor,
                    style = MaterialTheme.typography.button,
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            OutlinedButton(
                onClick = {
                    tipsSelected = !tipsSelected
                    mapSelected = false
                    sendImageCommand("1", context)
                },
                border = BorderStroke(1.dp, Color.Red),
                colors = ButtonDefaults.buttonColors(backgroundColor = tipsButtonColor),
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 30.dp, bottom = 34.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = "TIPS",
                    color = tipsTextColor,
                    style = MaterialTheme.typography.button,
                )
            }
        }

        Image(
            painter = painterResource(id = if (mapSelected || tipsSelected) imageSource else R.drawable.bridgefy_complete_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

fun sendImageCommand(image: String, context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SDK_SEND_IMAGE
            putExtra("image", image)
        },
    )
}
