package org.d3ifcool.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.Pesanan
import org.d3ifcool.shared.model.Transaksi
import org.d3ifcool.shared.repository.FirestoreRepository

data class DapurUiState(
    val pesananAktif: List<Pesanan> = emptyList(),
    val riwayatPesanan: List<Pesanan> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class DapurViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _uiState = MutableStateFlow(DapurUiState())
    val uiState: StateFlow<DapurUiState> = _uiState.asStateFlow()

    init {
        observePesananAktif()
        observeRiwayatPesanan()
    }

    private fun observePesananAktif() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getActivePesananFlow().collect { pesanan ->
                _uiState.value = _uiState.value.copy(
                    pesananAktif = pesanan,
                    isLoading = false
                )
            }
        }
    }

    private fun observeRiwayatPesanan() {
        viewModelScope.launch {
            repository.getCompletedPesananFlow().collect { pesanan ->
                _uiState.value = _uiState.value.copy(riwayatPesanan = pesanan)
            }
        }
    }

    fun updateStatusPesanan(pesanan: Pesanan, status: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                repository.updatePesananStatus(pesanan.id, status)

                if (status == "Selesai") {
                    createTransaksiAndUpdateMenu(pesanan)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Status pesanan diperbarui"
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private suspend fun createTransaksiAndUpdateMenu(pesanan: Pesanan) {

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
        repository.addTransaksi(transaksi)

        pesanan.daftarMenu.forEach { item ->
            val menu = repository.getMenuById(item.menuId)
            menu?.let {
                repository.updateMenu(
                    it.copy(
                        soldCount = it.soldCount + item.jumlah
                    )
                )
            }
        }
    }
}