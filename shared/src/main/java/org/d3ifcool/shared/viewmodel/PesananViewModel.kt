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
import org.d3ifcool.shared.model.Transaksi
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

    // =====================================================
    // 📥 LOAD PESANAN
    // =====================================================

    fun loadPendingPesanan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            firestoreRepository.getPendingPesananFlow().collect { pesananList ->
                _uiState.value = _uiState.value.copy(
                    pendingPesanan = pesananList,
                    isLoading = false
                )
            }
        }
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

    // =====================================================
    // 🎯 SELECT PESANAN
    // =====================================================

    fun selectPesanan(pesanan: Pesanan) {
        _uiState.value = _uiState.value.copy(selectedPesanan = pesanan)
    }

    fun clearSelectedPesanan() {
        _uiState.value = _uiState.value.copy(selectedPesanan = null)
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



    fun updatePesananStatus(pesananId: String, newStatus: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val result = firestoreRepository.updatePesananStatus(pesananId, newStatus)

                if (result.isSuccess) {
                    if (newStatus == "Selesai") {
                        val pesanan = _uiState.value.pendingPesanan.find { it.id == pesananId }
                            ?: _uiState.value.selectedPesanan

                        pesanan?.let { createTransaksiFromPesanan(it) }
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Status pesanan berhasil diupdate"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengupdate status"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private suspend fun createTransaksiFromPesanan(pesanan: Pesanan) {
        val transaksi = Transaksi(
            pesananId = pesanan.id,
            userId = pesanan.userId,
            namaPelanggan = pesanan.namaPelanggan,
            meja = pesanan.meja,
            tanggal = pesanan.tanggal,
            waktu = pesanan.waktu,
            items = pesanan.daftarMenu,
            total = pesanan.totalHarga,
            metodePembayaran = "Cash",
            status = "Lunas"
        )
        firestoreRepository.addTransaksi(transaksi)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}
