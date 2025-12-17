package org.d3ifcool.mejaq.ui.keranjang

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import org.d3ifcool.mejaq.R
import org.d3ifcool.shared.model.CartItem

class MainViewModel : ViewModel() {

    val cartItems = mutableStateListOf(
        CartItem(1, R.drawable.mie_goreng, "Indomie Goreng", 10000, 1),
        CartItem(2, R.drawable.mie_rebus, "Indomie Rebus", 10000, 2),
        CartItem(3, R.drawable.es_teh, "Es Teh Manis", 5000, 1)
    )

    fun subtotal(): Int {
        return cartItems.sumOf { it.price * it.quantity }
    }

    fun serviceFee(): Int {
        return 2000
    }

    fun totalPayment(): Int {
        return subtotal() + serviceFee()
    }
}
