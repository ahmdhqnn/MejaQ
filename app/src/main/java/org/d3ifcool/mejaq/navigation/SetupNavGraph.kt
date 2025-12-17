package org.d3ifcool.mejaq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.mejaq.login.LoginScreen

import org.d3ifcool.mejaq.ui.beranda.BerandaUserScreen
import org.d3ifcool.mejaq.ui.beranda.DetailEventScreen
import org.d3ifcool.mejaq.ui.keranjang.CartScreen
import org.d3ifcool.mejaq.ui.pemesanan.DetailMenuScreen
import org.d3ifcool.mejaq.ui.pemesanan.KEY_TABLE_NUMBER
import org.d3ifcool.mejaq.ui.pemesanan.OrderScreen
import org.d3ifcool.mejaq.ui.pemesanan.QrCodeScannerScreen
import org.d3ifcool.mejaq.ui.keranjang.SuccessScreen
import org.d3ifcool.mejaq.ui.riwayat.RiwayatScreen

@Composable
fun SetupNavGraph(
    user: FirebaseUser,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            BerandaUserScreen(
                user = user,
                navController = navController
            )
        }

        composable(
            route = Screen.DetailEvent.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType }
            )
        ) {
            val eventId = it.arguments?.getInt("eventId") ?: 0
            DetailEventScreen(navController, eventId)
        }

        composable(Screen.QrCodeScanner.route) {
            QrCodeScannerScreen(navController)
        }

        composable(Screen.Cart.route) {
            CartScreen(navController)
        }

        composable(Screen.Success.route) {
            SuccessScreen(navController)
        }

        composable(Screen.Riwayat.route) {
            RiwayatScreen(navController)
        }
    }
}


