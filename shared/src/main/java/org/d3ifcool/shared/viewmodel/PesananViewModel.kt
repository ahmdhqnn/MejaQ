package org.d3ifcool.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.ItemMenu
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
    val selectedPesanan: Pesanan?  = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class PesananViewModel : ViewModel() {
    private val firestoreRepository = FirestoreRepository()

    private val _uiState = MutableStateFlow(PesananUiState())
    val uiState: StateFlow<PesananUiState> = _uiState.asStateFlow()

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

    fun selectPesanan(pesanan:  Pesanan) {
        _uiState.value = _uiState.value.copy(selectedPesanan = pesanan)
    }

    fun clearSelectedPesanan() {
        _uiState.value = _uiState.value.copy(selectedPesanan = null)
    }

    fun createPesanan(
        userId:  String,
        namaPelanggan: String,
        meja: String,
        items: List<ItemMenu>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                val now = Date()

                val totalHarga = items.sumOf { it.subtotal }

                val pesanan = Pesanan(
                    userId = userId,
                    namaPelanggan = namaPelanggan,
                    meja = meja,
                    tanggal = dateFormat.format(now),
                    waktu = timeFormat.format(now),
                    daftarMenu = items,
                    totalHarga = totalHarga,
                    status = "Pending"
                )

                val result = firestoreRepository.addPesanan(pesanan)
                if (result.isSuccess) {
                    _uiState.value = _uiState. value.copy(
                        isLoading = false,
                        successMessage = "Pesanan berhasil dibuat"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal membuat pesanan"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState. value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun updatePesananStatus(pesananId: String, newStatus: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val result = firestoreRepository.updatePesananStatus(pesananId, newStatus)

                if (result.isSuccess) {
                    // Jika status "Selesai", buat transaksi
                    if (newStatus == "Selesai") {
                        val pesanan = _uiState.value.pendingPesanan.find { it.id == pesananId }
                            ?: _uiState.value.selectedPesanan

                        pesanan?.let {
                            createTransaksiFromPesanan(it)
                        }
                    }

                    _uiState.value = _uiState.value. copy(
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
                _uiState. value = _uiState.value.copy(
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