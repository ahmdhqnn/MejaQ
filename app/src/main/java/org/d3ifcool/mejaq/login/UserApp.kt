package org.d3ifcool.mejaq.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.mejaq.navigation.SetupNavGraph
@Composable
fun UserApp() {
    val viewModel: AppViewModel = viewModel()
    val userFlow by viewModel.userFlow.collectAsState()

    if (userFlow == null) {
        // 🔐 BELUM LOGIN → TAMPILKAN LOGIN
        LoginScreen(
            navController = rememberNavController()
        )
    } else {
        // ✅ SUDAH LOGIN → MASUK APP
        SetupNavGraph(user = userFlow!!)
    }
}
