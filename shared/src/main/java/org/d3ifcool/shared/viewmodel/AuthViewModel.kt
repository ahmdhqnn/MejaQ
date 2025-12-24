package org.d3ifcool.shared.viewmodel

import androidx.lifecycle. ViewModel
import androidx.lifecycle. viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase. auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines. flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx. coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx. coroutines.tasks. await
import org.d3ifcool.shared.model.User
import org.d3ifcool.shared.repository.FirestoreRepository

data class AuthUiState(
    val currentUser:  FirebaseUser? = null,
    val userData: User? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String?  = null,
    val successMessage: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestoreRepository = FirestoreRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(
                currentUser = currentUser,
                isLoggedIn = true
            )
            loadUserData(currentUser. uid)
        }
    }

    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                if (user != null) {
                    val userData = firestoreRepository.getOrCreateUser(
                        userId = user.uid,
                        email = user.email ?: "",
                        name = user.displayName ?: "User"
                    )
                    _uiState.value = _uiState.value.copy(userData = userData)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value. copy(errorMessage = e.message)
            }
        }
    }

    fun signInWithEmailPassword(email: String, password: String) {
        viewModelScope. launch {
            _uiState.value = _uiState. value.copy(isLoading = true, errorMessage = null)

            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null) {
                    _uiState. value = _uiState.value.copy(
                        currentUser = user,
                        isLoggedIn = true,
                        isLoading = false,
                        successMessage = "Login berhasil"
                    )
                    loadUserData(user.uid)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Login gagal"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = getErrorMessage(e)
                )
            }
        }
    }

    fun signUpWithEmailPassword(email: String, password:  String, name: String, role: String = "user") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result. user

                if (user != null) {
                    // Create user document in Firestore
                    val newUser = User(
                        id = user.uid,
                        email = email,
                        name = name,
                        role = role
                    )
                    firestoreRepository.updateUser(newUser)

                    _uiState.value = _uiState.value. copy(
                        currentUser = user,
                        userData = newUser,
                        isLoggedIn = true,
                        isLoading = false,
                        successMessage = "Registrasi berhasil"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Registrasi gagal"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = getErrorMessage(e)
                )
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.value = AuthUiState()
    }

    fun onAuthSuccess(firebaseUser: FirebaseUser) {
        _uiState.value = _uiState.value. copy(
            currentUser = firebaseUser,
            isLoggedIn = true,
            isLoading = false
        )
        loadUserData(firebaseUser.uid)
    }

    fun updateUserProfile(name: String, photoUrl: String = "") {
        viewModelScope. launch {
            _uiState.value = _uiState. value.copy(isLoading = true)

            try {
                val currentUserData = _uiState.value. userData
                if (currentUserData != null) {
                    val updatedUser = currentUserData.copy(
                        name = name,
                        photoUrl = photoUrl
                    )
                    val result = firestoreRepository.updateUser(updatedUser)

                    if (result.isSuccess) {
                        _uiState.value = _uiState.value.copy(
                            userData = updatedUser,
                            isLoading = false,
                            successMessage = "Profil berhasil diupdate"
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal mengupdate profil"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                auth.sendPasswordResetEmail(email).await()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Email reset password telah dikirim ke $email"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = getErrorMessage(e)
                )
            }
        }
    }

    private fun getErrorMessage(exception: Exception): String {
        return when {
            exception.message?. contains("email address is badly formatted") == true ->
                "Format email tidak valid"
            exception.message?.contains("no user record") == true ->
                "Email tidak terdaftar"
            exception. message?.contains("password is invalid") == true ->
                "Password salah"
            exception.message?. contains("email address is already in use") == true ->
                "Email sudah terdaftar"
            exception.message?.contains("weak password") == true ->
                "Password terlalu lemah (minimal 6 karakter)"
            exception.message?.contains("network error") == true ->
                "Tidak ada koneksi internet"
            else -> exception.message ?: "Terjadi kesalahan"
        }
    }

    fun clearError() {
        _uiState.value = _uiState. value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState. value.copy(successMessage = null)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}