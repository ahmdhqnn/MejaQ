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
    val errorMessage:  String? = null,
    val successMessage: String? = null
)

class EventViewModel : ViewModel() {
    private val firestoreRepository = FirestoreRepository()

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState. asStateFlow()

    init {
        loadActiveEvents()
    }

    fun loadAllEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            firestoreRepository.getAllEventsFlow().collect { events ->
                _uiState.value = _uiState.value. copy(
                    events = events,
                    isLoading = false
                )
            }
        }
    }

    private fun loadActiveEvents() {
        viewModelScope.launch {
            firestoreRepository.getActiveEventsFlow().collect { events ->
                _uiState.value = _uiState.value.copy(activeEvents = events)
            }
        }
    }

    fun selectEvent(event: Event) {
        _uiState.value = _uiState.value. copy(selectedEvent = event)
    }

    fun clearSelectedEvent() {
        _uiState.value = _uiState. value.copy(selectedEvent = null)
    }

    // Tambah event dengan imageUrl dari internet
    fun addEvent(
        title: String,
        description: String,
        eventDate: String,
        eventTime: String,
        location: String,
        imageUrl:  String = ""  // URL gambar dari internet (ImgBB, dll)
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
                    isActive = true
                )

                val result = firestoreRepository.addEvent(event)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Event berhasil ditambahkan"
                    )
                } else {
                    _uiState. value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal menambahkan event"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e. message
                )
            }
        }
    }

    fun updateEvent(event: Event, newImageUrl: String? = null) {
        viewModelScope. launch {
            _uiState.value = _uiState. value.copy(isLoading = true)

            try {
                val updatedEvent = if (newImageUrl != null) {
                    event.copy(imageUrl = newImageUrl)
                } else {
                    event
                }

                val result = firestoreRepository.updateEvent(updatedEvent)

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Event berhasil diupdate"
                    )
                } else {
                    _uiState.value = _uiState. value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengupdate event"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e. message
                )
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            _uiState.value = _uiState.value. copy(isLoading = true)

            try {
                val result = firestoreRepository.deleteEvent(event.id)
                if (result.isSuccess) {
                    _uiState.value = _uiState. value.copy(
                        isLoading = false,
                        successMessage = "Event berhasil dihapus"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal menghapus event"
                    )
                }
            } catch (e:  Exception) {
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
                val updatedEvent = event.copy(isActive = !event.isActive)
                val result = firestoreRepository. updateEvent(updatedEvent)

                if (result.isSuccess) {
                    val statusText = if (updatedEvent.isActive) "diaktifkan" else "dinonaktifkan"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Event berhasil $statusText"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengubah status event"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e. message
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}