package org.d3ifcool.admin.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("loginAdmin")
    data object Home : Screen("berandaAdmin")

    data object Event : Screen("eventScreen")
    data object InputEvent : Screen("inputEvent")
    // Ubah dari Int ke String untuk Firestore document ID
    data object DetailEvent :  Screen("event/detail/{eventId}") {
        fun createRoute(eventId: String) = "event/detail/$eventId"
    }

    data object Menu : Screen("menuScreen")
    data object InputMenu : Screen("inputMenu")
    // Ubah dari Int ke String untuk Firestore document ID
    data object DetailMenu : Screen("menu/detail/{menuId}") {
        fun createRoute(menuId:  String) = "menu/detail/$menuId"
    }

    data object Keuangan : Screen("keuanganScreen")
}