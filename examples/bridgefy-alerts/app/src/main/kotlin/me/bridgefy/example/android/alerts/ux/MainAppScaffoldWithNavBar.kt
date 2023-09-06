package me.bridgefy.example.android.alerts.ux

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppNavBarData
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppNavBarType
import me.bridgefy.example.android.alerts.ui.compose.appbar.AppScaffoldAndNavigation
import me.bridgefy.example.android.alerts.ui.compose.appnavbar.AppBottomNavigationItem
import me.bridgefy.example.android.alerts.ui.compose.appnavbar.AppNavigationDrawerItem
import me.bridgefy.example.android.alerts.ui.compose.appnavbar.AppNavigationDrawerLabel
import me.bridgefy.example.android.alerts.ui.compose.appnavbar.AppNavigationRailItem
import me.bridgefy.example.android.alerts.ui.compose.util.rememberWindowSize
import me.bridgefy.example.android.alerts.util.ext.requireActivity
import me.bridgefy.example.android.alerts.ux.main.MainViewModel
import me.bridgefy.example.android.alerts.ux.main.NavBarItem

@Composable
internal fun MainAppScaffoldWithNavBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIconVisible: Boolean = true,
    navigationIcon: ImageVector = Icons.Filled.ArrowBack,
    onNavigationClick: (() -> Unit)? = null,
    hideNavigation: Boolean = false,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    content: @Composable (PaddingValues) -> Unit,
) {
    val activity = LocalContext.current.requireActivity()
    val windowSize = activity.rememberWindowSize()
    val viewModel: MainViewModel = hiltViewModel(activity)
    val selectedBarItem by viewModel.selectedNavBarFlow.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // TopAppBar
    val topAppBar: @Composable (() -> Unit) = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.bridgefy_color),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
            title = { Text(title) },
            navigationIcon = if (!navigationIconVisible) {
                {}
            } else {
                {
                    IconButton(onClick = { onNavigationClick?.invoke() }) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = stringResource(id = R.string.back),
                            modifier = Modifier,
                        )
                    }
                }
            },
            actions = {
                if (actions != null) {
                    actions()
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }

    // Navigation (support Bottom / Rail / Drawer)
    val navBarData = AppNavBarData(
        appNavBarType = AppNavBarType.byWindowSize(windowSize),
        navBar = {
            AppNavigationBar(
                selectedItem = selectedBarItem,
                onNavItemClicked = { viewModel.onNavBarItemSelected(it) },
            )
        },
        navRail = {
            AppNavigationRail(
                selectedItem = selectedBarItem,
                onNavItemClicked = { viewModel.onNavBarItemSelected(it) },
            )
        },
        navDrawer = { appScaffold ->
            AppNavigationDrawer(
                selectedItem = selectedBarItem,
                onNavItemClicked = { viewModel.onNavBarItemSelected(it) },
                appScaffoldContent = appScaffold,
            )
        },
    )

    // Scaffold
    AppScaffoldAndNavigation(
        topAppBar = topAppBar,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        hideNavigation = hideNavigation,
        navBarData = navBarData,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = MaterialTheme.colorScheme.surface,
        content = content,
    )
}

@Composable
private fun AppNavigationBar(
    selectedItem: NavBarItem?,
    onNavItemClicked: (NavBarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NavigationBar(
            containerColor = colorResource(id = R.color.bridgefy_color),
            contentColor = Color.White,
        ) {
            NavBarItem.values().forEach { item ->
                AppBottomNavigationItem(item, ImageVector.vectorResource(id = item.unselectedImage), ImageVector.vectorResource(id = item.selectedImage), selectedItem, item.textResId) { onNavItemClicked(it) }
            }
        }
    }
}

@Composable
private fun AppNavigationRail(
    selectedItem: NavBarItem?,
    onNavItemClicked: (NavBarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        NavigationRail {
            NavBarItem.values().forEach { item ->
                AppNavigationRailItem(item, ImageVector.vectorResource(id = item.unselectedImage), ImageVector.vectorResource(id = item.selectedImage), selectedItem, item.textResId) { onNavItemClicked(it) }
            }
        }
    }
}

@Composable
private fun AppNavigationDrawer(
    selectedItem: NavBarItem?,
    onNavItemClicked: (NavBarItem) -> Unit,
    appScaffoldContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet {
                AppNavigationDrawerLabel(stringResource(R.string.app_name))

                NavBarItem.values().forEach { item ->
                    AppNavigationDrawerItem(item, ImageVector.vectorResource(id = item.unselectedImage), ImageVector.vectorResource(id = item.selectedImage), selectedItem, item.textResId) { onNavItemClicked(it) }
                }
            }
        },
        modifier = modifier,
    ) {
        appScaffoldContent()
    }
}
