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
    val errorMessage: String?  = null
)

data class DailyRevenue(
    val dayName: String,
    val revenue:  Int
)

class DashboardViewModel : ViewModel() {
    private val firestoreRepository = FirestoreRepository()

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Load active events count
                val eventsCount = firestoreRepository. getActiveEventsCount()

                // Load weekly revenue
                val weeklyRevenue = calculateWeeklyRevenue()

                // Calculate total revenue for current month
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar. MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar. set(Calendar.MILLISECOND, 0)
                val startOfMonth = calendar.timeInMillis

                calendar.add(Calendar.MONTH, 1)
                val endOfMonth = calendar.timeInMillis

                val totalRevenue = firestoreRepository.getTotalRevenue(startOfMonth, endOfMonth)

                _uiState. value = _uiState.value.copy(
                    totalRevenue = totalRevenue,
                    activeEventsCount = eventsCount,
                    weeklyRevenue = weeklyRevenue,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState. value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }

        // Load top menus (real-time)
        viewModelScope.launch {
            firestoreRepository.getTopMenusFlow(5).collect { menus ->
                _uiState.value = _uiState.value.copy(topMenus = menus)
            }
        }

        // Load recent transactions (real-time)
        viewModelScope.launch {
            firestoreRepository.getTransaksiFlow().collect { transaksiList ->
                _uiState.value = _uiState. value.copy(
                    recentTransaksi = transaksiList. take(10)
                )
            }
        }
    }

    private suspend fun calculateWeeklyRevenue(): List<DailyRevenue> {
        val dayNames = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
        val weeklyData = mutableListOf<DailyRevenue>()

        val calendar = Calendar.getInstance()

        // Find Monday of current week
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysFromMonday = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY
        calendar.add(Calendar.DAY_OF_MONTH, -daysFromMonday)

        calendar.set(Calendar. HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar. SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        for (i in 0 until 7) {
            val startOfDay = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endOfDay = calendar.timeInMillis

            val dailyRevenue = firestoreRepository.getTotalRevenue(startOfDay, endOfDay)
            weeklyData.add(DailyRevenue(dayNames[i], dailyRevenue))
        }

        return weeklyData
    }

    fun refreshData() {
        loadDashboardData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}