package org.d3ifcool.dapur.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.dapur.beranda.DapurMainScreen
import org.d3ifcool.dapur.beranda.RiwayatScreen
import org.d3ifcool.dapur.beranda.SuccessScreen
import org.d3ifcool.dapur.login.LoginScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination =Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Home.route) {
            DapurMainScreen(navController)
        }

        composable(Screen.Success.route) {
            SuccessScreen(navController)
        }
        composable(Screen.Riwayat.route) {
            RiwayatScreen(navController)
        }
    }

}