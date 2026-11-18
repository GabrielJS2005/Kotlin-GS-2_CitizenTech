package gabrieljs2005.com.github.caminholivreapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gabrieljs2005.com.github.caminholivreapp.data.preferences.SessionPreferences
import gabrieljs2005.com.github.caminholivreapp.data.repository.AuthRepository
import gabrieljs2005.com.github.caminholivreapp.domain.model.AppUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val obscurePassword: Boolean = true,
    val loginError: String? = null,
    val passwordError: String? = null,
    val credentialError: String? = null,
    val existingUser: AppUser? = null,
    val authenticatedUser: AppUser? = null,
)

class LoginViewModel(
    private val sessionPreferences: SessionPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(existingUser = sessionPreferences.loggedUser.value)
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateLogin(value: String) {
        _uiState.update {
            it.copy(
                login = value,
                loginError = null,
                credentialError = null,
            )
        }
    }

    fun updatePassword(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                credentialError = null,
            )
        }
    }

    fun toggleObscurePassword() {
        _uiState.update { it.copy(obscurePassword = !it.obscurePassword) }
    }

    fun fillCitizenProfile() {
        _uiState.update {
            it.copy(
                login = AuthRepository.citizenUser.email,
                password = AuthRepository.CITIZEN_PASSWORD,
                loginError = null,
                passwordError = null,
                credentialError = null,
            )
        }
    }

    fun fillManagerProfile() {
        _uiState.update {
            it.copy(
                login = AuthRepository.managerUser.email,
                password = AuthRepository.MANAGER_PASSWORD,
                loginError = null,
                passwordError = null,
                credentialError = null,
            )
        }
    }

    fun continueWithExistingSession() {
        val current = _uiState.value.existingUser ?: return
        sessionPreferences.saveLoggedUser(current)
        _uiState.update { it.copy(authenticatedUser = current) }
    }

    fun submitLogin() {
        val current = _uiState.value
        val loginErr = if (current.login.trim().isEmpty()) {
            "Informe o e-mail ou usuário."
        } else {
            null
        }
        val passErr = if (current.password.trim().isEmpty()) {
            "Informe a senha de acesso."
        } else {
            null
        }

        if (loginErr != null || passErr != null) {
            _uiState.update {
                it.copy(
                    loginError = loginErr,
                    passwordError = passErr,
                    credentialError = null,
                )
            }
            return
        }

        val user = AuthRepository.authenticate(
            login = current.login,
            password = current.password,
        )

        if (user != null) {
            sessionPreferences.saveLoggedUser(user)
            _uiState.update { it.copy(authenticatedUser = user) }
        } else {
            _uiState.update {
                it.copy(
                    credentialError = "Credenciais inválidas. Verifique o login e a senha ou utilize um dos perfis de teste rápidos abaixo.",
                )
            }
        }
    }

    companion object {
        fun factory(sessionPreferences: SessionPreferences): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    LoginViewModel(sessionPreferences)
                }
            }
    }
}

