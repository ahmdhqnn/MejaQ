package org.d3ifcool.mejaq.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.mejaq.ui.beranda.BerandaUserScreen
import org.d3ifcool.mejaq.ui.beranda.DetailEventScreen
import org.d3ifcool.mejaq.ui.keranjang.CartScreen
import org.d3ifcool.mejaq.ui.keranjang.SuccessScreen
import org.d3ifcool.mejaq.ui.pemesanan.*
import org.d3ifcool.mejaq.ui.riwayat.RiwayatScreen
import org.d3ifcool.shared.viewmodel.PesananViewModel

@Composable
fun SetupNavGraph(
    user: FirebaseUser,
    navController: NavHostController = rememberNavController()
) {
    // 🔥 SHARED VIEWMODEL
    val pesananViewModel: PesananViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            BerandaUserScreen(user, navController)
        }

        composable(Screen.QrCodeScanner.route) {
            QrCodeScannerScreen(navController)
        }

        composable(
            Screen.Order.route,
            arguments = listOf(navArgument("tableNumber") {
                type = NavType.StringType
            })
        ) {
            OrderScreen(
                navController = navController,
                tableNumber = it.arguments?.getString("tableNumber"),
                pesananViewModel = pesananViewModel
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                tableNumber = null,
                viewModel = pesananViewModel
            )
        }

        // ✅🔥 INI YANG HILANG → RIWAYAT
        composable(Screen.Riwayat.route) {
            RiwayatScreen(navController)
        }

        // ✅🔥 INI YANG HILANG → DETAIL EVENT
        composable(
            route = Screen.DetailEvent.route,
            arguments = listOf(navArgument("eventId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            DetailEventScreen(
                navController = navController,
                eventId = backStackEntry.arguments?.getString("eventId")
            )
        }

        composable(Screen.Success.route) {
            SuccessScreen(navController)
        }
    }
}


