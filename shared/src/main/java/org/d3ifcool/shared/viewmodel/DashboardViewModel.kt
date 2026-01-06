package org.d3ifcool.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.Menu
import org.d3ifcool.shared.model.Pesanan
import org.d3ifcool.shared.model.Transaksi
import org.d3ifcool.shared.repository.FirestoreRepository
import java.util.Calendar

data class DashboardUiState(
    val totalRevenue: Int = 0,
    val activeEventsCount: Int = 0,
    val topMenus: List<Menu> = emptyList(),
    val weeklyRevenue: List<DailyRevenue> = emptyList(),
    val recentTransaksi: List<Transaksi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class DailyRevenue(
    val dayName: String,
    val revenue: Int
)

class DashboardViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeTopMenus()
        observeRecentTransaksi()
        observePesananSelesai()
        loadActiveEvents()
    }

    private fun observePesananSelesai() {
        viewModelScope.launch {
            repository.getCompletedPesananFlow().collect { pesananList ->
                val totalRevenue = pesananList.sumOf { it.totalHarga }
                val weeklyRevenue = calculateWeeklyRevenueFromPesanan(pesananList)

                _uiState.value = _uiState.value.copy(
                    totalRevenue = totalRevenue,
                    weeklyRevenue = weeklyRevenue,
                    isLoading = false
                )
            }
        }
    }

    private fun loadActiveEvents() {
        viewModelScope.launch {
            val count = repository.getActiveEventsCount()
            _uiState.value = _uiState.value.copy(activeEventsCount = count)
        }
    }

    private fun observeTopMenus() {
        viewModelScope.launch {
            repository.getTopMenusFlow(5).collect { menus ->
                _uiState.value = _uiState.value.copy(topMenus = menus)
            }
        }
    }

    private fun observeRecentTransaksi() {
        viewModelScope.launch {
            repository.getTransaksiFlow().collect { transaksi ->
                _uiState.value = _uiState.value.copy(
                    recentTransaksi = transaksi.take(10)
                )
            }
        }
    }

    private fun calculateWeeklyRevenueFromPesanan(
        pesananList: List<Pesanan>
    ): List<DailyRevenue> {

        val dayNames = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
        val result = mutableListOf<DailyRevenue>()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        for (i in 6 downTo 0) {
            val start = calendar.timeInMillis - (i * 24 * 60 * 60 * 1000)
            val end = start + 24 * 60 * 60 * 1000

            val revenue = pesananList.filter {
                it.createdAt != null &&
                        it.createdAt.toDate().time in start until end
            }.sumOf { it.totalHarga }

            result.add(DailyRevenue(dayNames[6 - i], revenue))
        }

        return result
    }
}
