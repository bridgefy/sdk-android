package me.bridgefy.example.android.alerts.ux.text

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.service.BridgefyService
import me.bridgefy.example.android.alerts.ux.MainAppScaffoldWithNavBar
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute

@Composable
fun SendTextScreen(
    navController: NavController,
) {
    MainAppScaffoldWithNavBar(
        title = "Amin Panel",
        hideNavigation = false,
        navigationIconVisible = true,
        onNavigationClick = {
            navController.navigate(HomeScreenRoute.createRoute()) {
                popUpTo(SendTextRoute.createRoute()) {
                    inclusive = true
                }
            }
        },
    ) {
        TextScreenContent()
    }
}

@Composable
fun TextCommandView(text: String) {
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
            maxLines = 15,
            text = text,
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )
    }
}

@Composable
private fun TextScreenContent() {
    val showDialog = remember { mutableStateOf(false) }
    val newText = remember { mutableStateOf("") }

    if (showDialog.value) {
        OpenDialog(
            showDialog = showDialog.value,
            onDismiss = { showDialog.value = false },
            onConfirm = { text ->
                newText.value = text
                showDialog.value = false
            },
        )
    }

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
            text = "Set a text to show on remote devices.",
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )
        Spacer(modifier = Modifier.size(40.dp))
        Text(
            maxLines = 15,
            text = newText.value,
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                showDialog.value = true
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
                text = "SEND NEW MESSAGE",
                color = Color.White,
                style = MaterialTheme.typography.button,
            )
        }
    }
}

@Composable
private fun OpenDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val openDialog = remember { mutableStateOf(true) }
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Enter a message to send")
            },
            text = {
                Column() {
                    val maxChar = 400
                    TextField(
                        singleLine = true,
                        value = text,
                        onValueChange = { if (it.length <= maxChar) text = it },
                    )
                    Text("${maxChar - text.length} characters left")
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedButton(
                        onClick = { onDismiss.invoke() },
                        border = BorderStroke(1.dp, colorResource(id = R.color.bridgefy_color)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Black,
                            style = MaterialTheme.typography.button,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.bridgefy_color)),
                        onClick = {
                            onConfirm.invoke(
                                text,
                            )
                            sendTextCommand(text, context)
                        },
                    ) {
                        Text(
                            text = "Send",
                            color = Color.White,
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
            },
        )
    }
}

fun sendTextCommand(text: String, context: Context) {
    context.startService(
        Intent(context, BridgefyService::class.java).apply {
            action = BridgefyService.SDK_SEND_TEXT
            putExtra("text", text)
        },
    )
}
