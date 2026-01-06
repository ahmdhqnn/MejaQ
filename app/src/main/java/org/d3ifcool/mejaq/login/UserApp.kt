package org.d3ifcool.mejaq.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.d3ifcool.mejaq.navigation.SetupNavGraph
import org.d3ifcool.shared.viewmodel.AuthViewModel

@Composable
fun UserApp() {
    val authViewModel: AuthViewModel = viewModel()
    val uiState by authViewModel.uiState.collectAsState()

    when {
        uiState.isLoggedIn && uiState.currentUser != null -> {
            SetupNavGraph(
                user = uiState.currentUser!!,
                authViewModel = authViewModel
            )
        }
        else -> {
            LoginScreen(
                onLoginSuccess = { firebaseUser ->
                    authViewModel.onAuthSuccess(firebaseUser)
                }
            )
        }
    }
}


