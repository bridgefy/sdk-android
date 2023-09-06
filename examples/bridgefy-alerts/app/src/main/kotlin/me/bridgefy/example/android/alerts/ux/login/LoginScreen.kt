package me.bridgefy.example.android.alerts.ux.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.ux.home.HomeScreenRoute

@Composable
fun LoginScreen(
    navController: NavController? = null,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            ConstraintLayout {
                val (image, userForm) = createRefs()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(280.dp)
                        .constrainAs(image) {
                            top.linkTo(userForm.top)
                            bottom.linkTo(userForm.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                ) {
                    HeaderView()
                }
                Card(
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp,
                    ),
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp)
                        .constrainAs(userForm) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                    ) {
                        val bridgefyAlertsText = "Bridgefy Alerts System"
                        val bridgefyText = "Bridgefy"
                        val welcomeAnnotatedString = buildAnnotatedString {
                            append(bridgefyAlertsText)
                            addStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                ),
                                start = 0,
                                end = bridgefyAlertsText.length,
                            )
                            addStyle(
                                style = SpanStyle(
                                    color = Color.Red,
                                ),
                                start = 0,
                                end = bridgefyText.length,
                            )
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 20.dp),
                            text = welcomeAnnotatedString,
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                        )

                        Text(
                            text = "Select a username:",
                            style = MaterialTheme.typography.subtitle1.copy(color = Color.Black),
                            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                        )

                        val userNameState = remember { UserNameFieldState() }

                        CustomStyleTextField(
                            usernameState = userNameState,
                            placeHolder = "Username",
                            icon = Icons.Default.Person,
                            keyBoardType = KeyboardType.Text,
                            visualTransformation = VisualTransformation.None,
                        )

                        Button(
                            onClick = {
                                if (userNameState.text.isNotEmpty()) {
                                    uiState.onStartClicked(userNameState.text)
                                    navController?.navigate(HomeScreenRoute.createRoute()) {
                                        popUpTo(LoginRoute.createRoute()) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.bridgefy_color),
                            ),
                            modifier = Modifier
                                .padding(top = 30.dp, bottom = 34.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                                text = "START",
                                color = Color.White,
                                style = MaterialTheme.typography.button,
                            )
                        }
                    }
                }
            }
        }
    }
}
class UserNameFieldState() { var text: String by mutableStateOf("") }

@Composable
fun CustomStyleTextField(
    placeHolder: String,
    usernameState: UserNameFieldState = remember { UserNameFieldState() },
    icon: ImageVector,
    keyBoardType: KeyboardType,
    visualTransformation: VisualTransformation,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        value = usernameState.text,
        onValueChange = {
            usernameState.text = it
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
        placeholder = { Text(text = placeHolder) },
        leadingIcon = {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .size(18.dp),
                        imageVector = icon,
                        tint = Color.Black,
                        contentDescription = "user_name",
                    )
                    Canvas(
                        modifier = Modifier.height(24.dp),
                    ) {
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 2.0f,
                        )
                    }
                },
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = Color.White,
            trailingIconColor = Color.White,
        ),
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        visualTransformation = visualTransformation,
    )
}

@Composable
private fun HeaderView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            start = 40.dp,
            end = 40.dp,
            bottom = 40.dp,
        ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.bridgefy_complete_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
