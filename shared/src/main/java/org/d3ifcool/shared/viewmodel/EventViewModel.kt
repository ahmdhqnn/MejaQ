package org.d3ifcool.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.Event
import org.d3ifcool.shared.repository.FirestoreRepository

data class EventUiState(
    val events: List<Event> = emptyList(),
    val activeEvents: List<Event> = emptyList(),
    val selectedEvent: Event? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class EventViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        observeAllEvents()
        observeActiveEvents()
    }

    private fun observeAllEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllEventsFlow().collect { events ->
                _uiState.value = _uiState.value.copy(
                    events = events,
                    isLoading = false
                )
            }
        }
    }

    private fun observeActiveEvents() {
        viewModelScope.launch {
            repository.getActiveEventsFlow().collect { events ->
                _uiState.value = _uiState.value.copy(activeEvents = events)
            }
        }
    }

    fun addEvent(
        title: String,
        description: String,
        eventDate: String,
        eventTime: String,
        location: String,
        imageUrl: String = ""
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val event = Event(
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    eventDate = eventDate,
                    eventTime = eventTime,
                    location = location,
                    active = true // 🔥 FIX FIELD
                )

                val result = repository.addEvent(event)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = if (result.isSuccess)
                        "Event berhasil ditambahkan"
                    else
                        "Gagal menambahkan event"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun toggleEventStatus(event: Event) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val updatedEvent = event.copy(active = !event.active)
                repository.updateEvent(updatedEvent)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = if (updatedEvent.active)
                        "Event berhasil diaktifkan"
                    else
                        "Event berhasil dinonaktifkan"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                repository.deleteEvent(event.id)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Event berhasil dihapus"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun loadEventDetail(eventId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val event = repository.getEventById(eventId)

            _uiState.value = _uiState.value.copy(
                selectedEvent = event,
                isLoading = false
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}
