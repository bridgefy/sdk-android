package me.bridgefy.example.android.alerts.ux.color

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
fun SendColorScreen(
    navController: NavController,
) {
    MainAppScaffoldWithNavBar(
        title = "Amin Panel",
        hideNavigation = false,
        navigationIconVisible = true,
        onNavigationClick = {
            navController.navigate(HomeScreenRoute.createRoute()) {
                popUpTo(SendColorRoute.createRoute()) {
                    inclusive = true
                }
            }
        },
    ) {
        ColorContent()
    }
}

@Composable
fun ColorCommandView(color: String) {
    val colorArray = color.split("-").toTypedArray()
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
        ColorBox(
            colorBackground = Color(
                red = colorArray[0].toInt(),
                green = colorArray[1].toInt(),
                blue = colorArray[2].toInt(),
            ),
            boxSize = 1000,
        )
    }
}

@Composable
private fun ColorBox(colorBackground: Color, boxSize: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
    ) {
        Box(
            modifier = Modifier
                .size(boxSize.dp)
                .clip(shape = RectangleShape)
                .background(colorBackground),
        ) {
            Image(
                painter = painterResource(id = R.drawable.bridgefy_white_complete_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 30.dp, end = 34.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun ColorContent() {
    var redValue by remember { mutableIntStateOf(0) }
    var greenValue by remember { mutableIntStateOf(0) }
    var blueValue by remember { mutableIntStateOf(0) }
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
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Select a color to show on remote devices.",
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )
        Spacer(modifier = Modifier.size(20.dp))
        ColorBox(
            colorBackground = Color(
                red = redValue,
                green = greenValue,
                blue = blueValue,
            ),
            boxSize = 100,
        )
        Spacer(modifier = Modifier.size(20.dp))
        Slider(
            value = redValue.toFloat(),
            onValueChange = { newValue -> redValue = newValue.toInt() },
            onValueChangeFinished = {
            },
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
            ),
        )
        Spacer(modifier = Modifier.size(20.dp))
        Slider(
            value = greenValue.toFloat(),
            onValueChange = { newValue -> greenValue = newValue.toInt() },
            onValueChangeFinished = {
            },
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Green,
                activeTrackColor = Color.Green,
            ),
        )
        Spacer(modifier = Modifier.size(20.dp))
        Slider(
            value = blueValue.toFloat(),
            onValueChange = { newValue -> blueValue = newValue.toInt() },
            onValueChangeFinished = {
            },
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Blue,
                activeTrackColor = Color.Blue,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                sendImageCommand("$redValue-$greenValue-$blueValue", context)
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
                text = "SEND COLOR",
                color = Color.White,
                style = MaterialTheme.typography.button,
            )
        }
    }
}

fun sendImageCommand(colors: String, context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SDK_SEND_COLOR
            putExtra("color", colors)
        },
    )
}
