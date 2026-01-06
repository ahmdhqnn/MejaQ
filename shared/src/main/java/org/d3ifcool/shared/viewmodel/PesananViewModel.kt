package org.d3ifcool.shared.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.ItemMenu
import org.d3ifcool.shared.model.Menu
import org.d3ifcool.shared.model.Pesanan
import org.d3ifcool.shared.repository.FirestoreRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PesananUiState(
    val pendingPesanan: List<Pesanan> = emptyList(),
    val completedPesanan: List<Pesanan> = emptyList(),
    val userPesanan: List<Pesanan> = emptyList(),
    val selectedPesanan: Pesanan? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null

)

@Suppress("DEPRECATION")
class PesananViewModel : ViewModel() {

    private val firestoreRepository = FirestoreRepository()


    private val _uiState = MutableStateFlow(PesananUiState())
    val uiState: StateFlow<PesananUiState> = _uiState.asStateFlow()

    private val _cartItems = mutableStateListOf<ItemMenu>()
    val cartItems: List<ItemMenu> get() = _cartItems

    private val _selectedMeja = MutableStateFlow("1")
    val selectedMeja: StateFlow<String> = _selectedMeja.asStateFlow()

    fun setMeja(meja: String) {
        _selectedMeja.value = meja.filter { it.isDigit() }.ifEmpty { "1" }
    }


    fun addToCart(menu: Menu, qty: Int, note: String) {
        _cartItems.add(
            ItemMenu(
                menuId = menu.id,
                nama = menu.name,
                imageUrl = menu.imageUrl,
                catatan = note,
                jumlah = qty,
                harga = menu.price,
                subtotal = menu.price * qty
            )
        )
    }


    fun clearCart() {
        _cartItems.clear()
    }



    fun loadCompletedPesanan() {
        viewModelScope.launch {
            firestoreRepository.getCompletedPesananFlow().collect { pesananList ->
                _uiState.value = _uiState.value.copy(completedPesanan = pesananList)
            }
        }
    }

    fun loadUserPesanan(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            firestoreRepository.getPesananByUserFlow(userId).collect { pesananList ->
                _uiState.value = _uiState.value.copy(
                    userPesanan = pesananList,
                    isLoading = false
                )
            }
        }
    }


    fun createPesanan(
        userId: String,
        namaPelanggan: String,
        items: List<ItemMenu>,
        pajak: Int = 2000
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            if (items.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Keranjang masih kosong"
                )
                return@launch
            }

            try {
                val dateFormat =
                    SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                val timeFormat =
                    SimpleDateFormat("HH:mm", Locale("id", "ID"))
                val now = Date()

                val subtotal = items.sumOf { it.subtotal }
                val total = subtotal + pajak

                val pesanan = Pesanan(
                    userId = userId,
                    namaPelanggan = namaPelanggan,
                    meja = selectedMeja.value,
                    tanggal = dateFormat.format(now),
                    waktu = timeFormat.format(now),
                    daftarMenu = items,
                    totalHarga = total,
                    status = "Pending"
                )

                firestoreRepository.addPesanan(pesanan)
                clearCart()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Pesanan berhasil dibuat"
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }


    fun updateCartItem(index: Int, qty: Int, note: String) {
        if (index in _cartItems.indices) {
            val item = _cartItems[index]
            _cartItems[index] = item.copy(
                jumlah = qty,
                catatan = note,
                subtotal = qty * item.harga
            )
        }
    }

    fun removeCartItem(index: Int): ItemMenu? {
        return if (index in _cartItems.indices) {
            _cartItems.removeAt(index)
        } else null
    }

    fun restoreCartItem(index: Int, item: ItemMenu) {
        if (index <= _cartItems.size) {
            _cartItems.add(index, item)
        } else {
            _cartItems.add(item)
        }
    }
}
