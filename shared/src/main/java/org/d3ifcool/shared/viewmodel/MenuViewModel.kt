package org.d3ifcool.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.d3ifcool.shared.model.Menu
import org.d3ifcool.shared.repository.FirestoreRepository

data class MenuUiState(
    val menus: List<Menu> = emptyList(),
    val topMenus: List<Menu> = emptyList(),
    val selectedMenu: Menu? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class MenuViewModel :  ViewModel() {
    private val firestoreRepository = FirestoreRepository()

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        loadMenus()
        loadTopMenus()
    }

    private fun loadMenus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            firestoreRepository.getMenusFlow().collect { menus ->
                _uiState.value = _uiState.value. copy(
                    menus = menus,
                    isLoading = false
                )
            }
        }
    }

    private fun loadTopMenus() {
        viewModelScope.launch {
            firestoreRepository.getTopMenusFlow(5).collect { topMenus ->
                _uiState.value = _uiState.value.copy(topMenus = topMenus)
            }
        }
    }

    fun selectMenu(menu: Menu) {
        _uiState.value = _uiState.value.copy(selectedMenu = menu)
    }

    fun clearSelectedMenu() {
        _uiState.value = _uiState.value.copy(selectedMenu = null)
    }

    // Tambah menu dengan imageUrl dari internet (ImgBB, Cloudinary, dll)
    fun addMenu(
        name: String,
        description: String,
        price: Int,
        category: String,
        imageUrl:  String = ""  // URL gambar dari internet
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val menu = Menu(
                    name = name,
                    description = description,
                    price = price,
                    category = category,
                    imageUrl = imageUrl,
                    isAvailable = true,
                    quantity = 0,
                    soldCount = 0
                )

                val result = firestoreRepository.addMenu(menu)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Menu berhasil ditambahkan"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal menambahkan menu"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value. copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun updateMenu(
        menu: Menu,
        newImageUrl: String? = null  // URL gambar baru jika diubah
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val updatedMenu = if (newImageUrl != null) {
                    menu.copy(imageUrl = newImageUrl)
                } else {
                    menu
                }

                val result = firestoreRepository.updateMenu(updatedMenu)

                if (result.isSuccess) {
                    _uiState.value = _uiState. value.copy(
                        isLoading = false,
                        successMessage = "Menu berhasil diupdate"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengupdate menu"
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

    fun deleteMenu(menu: Menu) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val result = firestoreRepository.deleteMenu(menu.id)
                if (result.isSuccess) {
                    _uiState.value = _uiState. value.copy(
                        isLoading = false,
                        successMessage = "Menu berhasil dihapus"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal menghapus menu"
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

    fun toggleMenuAvailability(menu: Menu) {
        viewModelScope.launch {
            try {
                val updatedMenu = menu.copy(isAvailable = !menu.isAvailable)
                firestoreRepository.updateMenu(updatedMenu)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState. value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}