package org.d3ifcool.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.Menu
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
        loadDashboardSummary()
    }

    fun loadDashboardSummary() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val activeEvents = repository.getActiveEventsCount()

                val weeklyRevenue = calculateWeeklyRevenue()

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfMonth = calendar.timeInMillis

                calendar.add(Calendar.MONTH, 1)
                val endOfMonth = calendar.timeInMillis

                val totalRevenue =
                    repository.getTotalRevenue(startOfMonth, endOfMonth)

                _uiState.value = _uiState.value.copy(
                    totalRevenue = totalRevenue,
                    activeEventsCount = activeEvents,
                    weeklyRevenue = weeklyRevenue,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
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

    private suspend fun calculateWeeklyRevenue(): List<DailyRevenue> {
        val dayNames = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
        val result = mutableListOf<DailyRevenue>()

        val calendar = Calendar.getInstance()

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val diff = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY
        calendar.add(Calendar.DAY_OF_MONTH, -diff)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        for (i in 0 until 7) {
            val start = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val end = calendar.timeInMillis

            val revenue = repository.getTotalRevenue(start, end)
            result.add(DailyRevenue(dayNames[i], revenue))
        }

        return result
    }

    fun refresh() {
        loadDashboardSummary()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
