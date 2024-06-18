package me.bridgefy.example.android.alerts.ux.onboarding

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.ux.login.LoginRoute
import me.bridgefy.example.android.alerts.R
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Preview
fun OnBoardingScreen(
    navController: NavController? = null,
    viewModel: OnBoardingViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState
    uiState.onBoardingButtonClicked

    val permissionsList: ArrayList<String> = arrayListOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.CAMERA,
    )

    val permissionState = rememberMultiplePermissionsState(permissions = permissionsList)
    val items = OnBoardingItems.getData()
    val pageState = rememberPagerState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(236, 236, 236, 255)),
    ) {
        // BridgefyPermissions(multiplePermissionState = permissionState)
        Spacer(Modifier.height(40.dp))
        TopSection(size = items.size, index = pageState.currentPage)
        Spacer(Modifier.height(20.dp))
        val target =
            if (pageState.currentPage < items.size - 1) pageState.currentPage + 1 else 0
        if (target == 2) {
            permissionState.launchMultiplePermissionRequest()
        }
        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()
                .background(Color(236, 236, 236, 255)),
        ) { page ->
            OnBoardingItem(items = items[page])
        }
        Spacer(Modifier.height(40.dp))
        BottomSection(
            count = items.size,
            index = pageState.currentPage,
            buttonEvent = {
                if (pageState.currentPage + 1 <= items.size - 1) {
                    viewModel.scope.launch {
                        if (target == 2) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                        pageState.scrollToPage(page = target)
                    }
                } else {
                    navController?.navigate(LoginRoute.createRoute()) {
                        popUpTo(OnBoardingRoute.createRoute()) {
                            inclusive = true
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun TopSection(size: Int, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(236, 236, 236, 255))
            .padding(12.dp),
    ) {
        Indicators(size, index)
    }
}

@Composable
fun BoxScope.Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.align(Alignment.Center),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
private fun BottomSection(
    count: Int,
    index: Int,
    buttonEvent: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(236, 236, 236, 255)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = buttonEvent,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF95151)),
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 34.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = if (index + 1 <= count - 1) {
                        stringResource(id = R.string.on_boarding_button_next)
                    } else {
                        stringResource(
                            id = R.string.on_boarding_button_start,
                        )
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.button,
                )
            }
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
    )

    Box(
        modifier = Modifier
            .height(5.dp)
            .width(width.value)
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .background(
                color = if (isSelected) {
                    colorResource(id = R.color.bridgefy_color)
                } else {
                    colorResource(id = R.color.unselected_color)
                },
            ),
    )
}

@Composable
fun OnBoardingItem(items: OnBoardingItems) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        items.imageResource?.let { res ->
            Image(
                painter = painterResource(id = res),
                modifier = Modifier.size(300.dp),
                contentDescription = "OnBoarding",
            )
        }

        items.titleResource?.let { res ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = res),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            )
        }

        items.descriptionResource?.let { res ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                color = Color(98, 98, 98),
                text = stringResource(id = res),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            )
        }

        items.warningResource?.let { res ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                color = Color(98, 98, 98),
                text = stringResource(id = res),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            )
        }
    }
}
