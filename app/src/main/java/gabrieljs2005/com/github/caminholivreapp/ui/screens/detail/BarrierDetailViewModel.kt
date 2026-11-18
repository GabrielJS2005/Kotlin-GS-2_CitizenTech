package gabrieljs2005.com.github.caminholivreapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.data.preferences.SessionPreferences
import gabrieljs2005.com.github.caminholivreapp.data.repository.BarrierRepository
import gabrieljs2005.com.github.caminholivreapp.data.repository.GuardianRepository
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class DetailSessionState(
    val simulatedProfile: MobilityProfile,
    val hasValidatedInSession: Boolean = false,
    val sessionXpEarned: Int = 0,
    val applyLgpdBlur: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val snackbarMessage: String? = null,
    val isDeleted: Boolean = false,
)

data class BarrierDetailUiState(
    val barrier: BarrierEntity? = null,
    val selectedProfile: MobilityProfile = MobilityProfile.CADEIRANTE,
    val hasValidatedInSession: Boolean = false,
    val sessionXpEarned: Int = 0,
    val applyLgpdBlur: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val snackbarMessage: String? = null,
    val isDeleted: Boolean = false,
)

class BarrierDetailViewModel(
    private val barrierId: String,
    private val barrierRepository: BarrierRepository,
    private val guardianRepository: GuardianRepository,
    sessionPreferences: SessionPreferences,
) : ViewModel() {

    private val sessionState = MutableStateFlow(
        DetailSessionState(simulatedProfile = sessionPreferences.activeProfile.value)
    )

    val uiState: StateFlow<BarrierDetailUiState> = combine(
        barrierRepository.observeById(barrierId),
        sessionState,
    ) { barrier, session ->
        BarrierDetailUiState(
            barrier = barrier,
            selectedProfile = session.simulatedProfile,
            hasValidatedInSession = session.hasValidatedInSession,
            sessionXpEarned = session.sessionXpEarned,
            applyLgpdBlur = session.applyLgpdBlur,
            showDeleteDialog = session.showDeleteDialog,
            snackbarMessage = session.snackbarMessage,
            isDeleted = session.isDeleted,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BarrierDetailUiState(
            selectedProfile = sessionPreferences.activeProfile.value
        ),
    )

    fun selectSimulatedProfile(profile: MobilityProfile) {
        sessionState.update { it.copy(simulatedProfile = profile) }
    }

    fun toggleLgpdBlur() {
        sessionState.update { it.copy(applyLgpdBlur = !it.applyLgpdBlur) }
    }

    fun toggleMonitor() {
        viewModelScope.launch {
            val updated = barrierRepository.toggleMonitored(barrierId) ?: return@launch
            val msg = if (updated.isMonitored) {
                "Barreira adicionada ao monitoramento de obras municipais."
            } else {
                "Monitoramento removido para esta barreira."
            }
            sessionState.update { it.copy(snackbarMessage = msg) }
        }
    }

    fun confirmStillThere() {
        if (sessionState.value.hasValidatedInSession) return
        viewModelScope.launch {
            val outcome = barrierRepository.confirmStillThere(barrierId) ?: return@launch
            guardianRepository.addXp(AppConstants.XP_PER_VALIDATION)
            val newScoreStr = String.format(Locale.US, "%.1f", outcome.updatedBarrier.priorityScore)
            val message = if (outcome.wasReopened) {
                "Chamado reaberto para \"Aberto\"! +${AppConstants.XP_PER_VALIDATION} XP • Novo Score B2G: $newScoreStr"
            } else {
                "Validação registrada no Room! +${AppConstants.XP_PER_VALIDATION} XP • Novo Score B2G: $newScoreStr"
            }
            sessionState.update {
                it.copy(
                    hasValidatedInSession = true,
                    sessionXpEarned = it.sessionXpEarned + AppConstants.XP_PER_VALIDATION,
                    snackbarMessage = message,
                )
            }
        }
    }

    fun requestDeleteConfirmation() {
        sessionState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        sessionState.update { it.copy(showDeleteDialog = false) }
    }

    fun confirmDelete() {
        viewModelScope.launch {
            sessionState.update { it.copy(showDeleteDialog = false) }
            val deleted = barrierRepository.deleteUserReported(barrierId)
            if (deleted) {
                sessionState.update { it.copy(isDeleted = true) }
            }
        }
    }

    fun consumeSnackbar() {
        sessionState.update { it.copy(snackbarMessage = null) }
    }

    companion object {
        fun factory(
            barrierId: String,
            barrierRepository: BarrierRepository,
            guardianRepository: GuardianRepository,
            sessionPreferences: SessionPreferences,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BarrierDetailViewModel(
                    barrierId = barrierId,
                    barrierRepository = barrierRepository,
                    guardianRepository = guardianRepository,
                    sessionPreferences = sessionPreferences,
                )
            }
        }
    }
}

