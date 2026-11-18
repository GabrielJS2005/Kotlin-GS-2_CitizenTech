package gabrieljs2005.com.github.caminholivreapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gabrieljs2005.com.github.caminholivreapp.data.preferences.SessionPreferences
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SplashDestination {
    data object Loading : SplashDestination
    data object ToOnboarding : SplashDestination
    data object ToRadar : SplashDestination
}

class SplashViewModel(
    private val sessionPreferences: SessionPreferences,
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Loading)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            val skipOnboarding = sessionPreferences.shouldSkipOnboarding()
            delay(AppConstants.SPLASH_DELAY_MS)
            _destination.value = if (skipOnboarding) {
                SplashDestination.ToRadar
            } else {
                SplashDestination.ToOnboarding
            }
        }
    }

    companion object {
        fun factory(sessionPreferences: SessionPreferences): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SplashViewModel(sessionPreferences)
                }
            }
    }
}

