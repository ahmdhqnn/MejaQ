package org.d3ifcool.admin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3ifcool.admin.beranda.BerandaAdmin
import org.d3ifcool.admin.event.DetailEventScreen
import org.d3ifcool.admin.event.EventScreen
import org.d3ifcool.admin.event.InputEventScreen
import org.d3ifcool.admin.keuangan.KeuanganScreen
import org.d3ifcool.admin.login.LoginAdminScreen
import org.d3ifcool.admin.menu.DetailMenuScreen
import org.d3ifcool.admin.menu.InputMenuScreen
import org.d3ifcool.admin.menu.MenuScreen
import org.d3ifcool.shared.viewmodel.AuthViewModel
import org.d3ifcool.shared.viewmodel.DashboardViewModel
import org.d3ifcool.shared.viewmodel.EventViewModel
import org.d3ifcool.shared.viewmodel.MenuViewModel
import org.d3ifcool.shared.viewmodel.PesananViewModel

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    // Hanya AuthViewModel yang di-init di awal
    val authViewModel:  AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Login - tidak perlu data dari Firestore
        composable(Screen.Login.route) {
            LoginAdminScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Home/Beranda - init ViewModel di sini setelah login
        composable(Screen.Home.route) {
            // ViewModel di-init hanya saat screen ini dibuka (setelah login)
            val dashboardViewModel: DashboardViewModel = viewModel()
            val menuViewModel: MenuViewModel = viewModel()

            BerandaAdmin(
                navController = navController,
                authViewModel = authViewModel,
                dashboardViewModel = dashboardViewModel,
                menuViewModel = menuViewModel
            )
        }

        // Event List
        composable(Screen.Event.route) {
            val eventViewModel: EventViewModel = viewModel()
            EventScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // Input Event
        composable(Screen. InputEvent.route) {
            val eventViewModel: EventViewModel = viewModel()
            InputEventScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // Detail Event
        composable(
            route = Screen.DetailEvent. route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?. getString("eventId") ?: ""
            val eventViewModel: EventViewModel = viewModel()
            DetailEventScreen(
                navController = navController,
                eventId = eventId,
                eventViewModel = eventViewModel
            )
        }

        // Menu List
        composable(Screen.Menu.route) {
            val menuViewModel: MenuViewModel = viewModel()
            MenuScreen(
                navController = navController,
                menuViewModel = menuViewModel
            )
        }

        // Input Menu
        composable(Screen. InputMenu.route) {
            val menuViewModel: MenuViewModel = viewModel()
            InputMenuScreen(
                navController = navController,
                menuViewModel = menuViewModel
            )
        }

        // Detail Menu
        composable(
            route = Screen.DetailMenu. route,
            arguments = listOf(
                navArgument("menuId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val menuId = backStackEntry. arguments?.getString("menuId") ?: ""
            val menuViewModel:  MenuViewModel = viewModel()
            DetailMenuScreen(
                navController = navController,
                menuId = menuId,
                menuViewModel = menuViewModel
            )
        }

        // Keuangan
        composable(Screen.Keuangan.route) {
            val pesananViewModel: PesananViewModel = viewModel()
            KeuanganScreen(
                navController = navController,
                pesananViewModel = pesananViewModel
            )
        }
    }
}