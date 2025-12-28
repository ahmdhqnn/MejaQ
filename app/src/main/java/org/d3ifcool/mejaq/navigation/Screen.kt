package org.d3ifcool.mejaq.navigation

sealed class Screen(val route: String) {

    object Home : Screen("berandaUser")

    object QrCodeScanner : Screen("qrScanner")

    object Order : Screen("order/{tableNumber}") {
        fun withTableNumber(tableNumber: String) =
            "order/$tableNumber"
    }

    object Cart : Screen("cart")

    object Riwayat : Screen("riwayat")

    object Success : Screen("success")

    object DetailEvent : Screen("detail_event/{eventId}") {
        fun createRoute(eventId: String) =
            "detail_event/$eventId"
    }

    object DetailMenu : Screen("menu/detail/{menuId}") {
        fun createRoute(menuId: String) =
            "menu/detail/$menuId"
    }
}
