package org.d3ifcool.mejaq.navigation

import org.d3ifcool.mejaq.ui.pemesanan.KEY_TABLE_NUMBER

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home: Screen("berandaUser")
    data object Success: Screen("SuccesScreen")
    data object DetailEvent : Screen("event/detail/{eventId}") {
        fun createRoute(eventId: Int) = "event/detail/$eventId"
    }
    data object QrCodeScanner: Screen("qrCodeScannerScreen")
    data object Order: Screen("orderScreen/{$KEY_TABLE_NUMBER}") {
        fun withTableNumber(tableNumber: String) = "orderScreen/$tableNumber"
    }
    data object DetailMenu : Screen("menu/detail/{menuId}") {
        fun createRoute(menuId: Int) = "menu/detail/$menuId"
    }
    data object Cart : Screen("cart")
    data object Riwayat : Screen("riwayat")


}