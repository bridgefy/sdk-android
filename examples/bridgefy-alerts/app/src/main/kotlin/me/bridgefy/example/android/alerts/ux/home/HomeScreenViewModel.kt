package me.bridgefy.example.android.alerts.ux.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import me.bridgefy.example.android.alerts.model.repository.UserRepository
import me.bridgefy.example.android.alerts.ui.compose.dialog.DialogUiState
import me.bridgefy.example.android.alerts.ui.compose.dialog.InputDialogUiState
import me.bridgefy.example.android.alerts.ui.compose.dialog.dismissDialog
import me.bridgefy.example.android.alerts.ux.image.SendImageRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val userRepository: UserRepository,
) : ViewModel() {

    private var navController: NavController? = null

    private val dialogUiStateFlow = MutableStateFlow<DialogUiState<*>?>(null)

    fun initNavController(mNavController: NavController) {
        navController = mNavController
    }

    val uiState = HomeUiState(
        dialogUiStateFlow = dialogUiStateFlow,
        onReceivedCommand = { },
        onAdminClicked = { onAdminClicked() },
    )

    private fun onAdminClicked() = viewModelScope.launch {
        val oldUser = userRepository.getUserData()
        if (oldUser != null && oldUser.admin == 0) {
            dialogUiStateFlow.value = InputDialogUiState(
                title = { "Enter Admin Password" },
                textFieldLabel = { "" },
                onConfirm = { password ->
                    if (password.isNotBlank() && password == "offline") {
                        viewModelScope.launch(Dispatchers.IO) {
                            oldUser.admin = 1
                            userRepository.updateUserData(oldUser)
                        }
                        dismissDialog(dialogUiStateFlow)
                        navController?.navigate(SendImageRoute.createRoute())
                        Toast.makeText(context, "Now you are admin", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
                    }
                },
                onDismiss = {
                    dismissDialog(dialogUiStateFlow)
                },
            )
        } else {
            navController?.navigate(SendImageRoute.createRoute())
        }
    }
}
