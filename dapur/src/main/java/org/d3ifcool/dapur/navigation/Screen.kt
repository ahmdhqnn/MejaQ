package org.d3ifcool.dapur.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("DapurScreen")
    data object Login : Screen("LoginScreen")
    data object Success : Screen("SuccessScreen")
    data object Riwayat : Screen("RiwayatScreen")
}