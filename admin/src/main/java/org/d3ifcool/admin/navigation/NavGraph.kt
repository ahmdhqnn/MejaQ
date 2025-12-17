package org.d3ifcool.admin.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
        composable(Screen.Login.route) {
                LoginAdminScreen(navController)
            }
        composable(Screen.Home.route) {
            BerandaAdmin(navController)
        }

        composable(Screen.Event.route) {
            EventScreen(navController)
        }

        composable(Screen.InputEvent.route) {
            InputEventScreen(navController)
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

        // MENU
        composable(Screen.Menu.route) {
            MenuScreen(navController)
        }
        composable(Screen.InputMenu.route) {
            InputMenuScreen(navController)
        }

        composable(
            route = Screen.DetailMenu.route,
            arguments = listOf(
                navArgument("menuId") { type = NavType.IntType }
            )
        ) {
            val menuId = it.arguments?.getInt("menuId") ?: 0
            DetailMenuScreen(navController, menuId)
        }

        composable(Screen.Keuangan.route) {
            KeuanganScreen(navController)
        }
    }
}
