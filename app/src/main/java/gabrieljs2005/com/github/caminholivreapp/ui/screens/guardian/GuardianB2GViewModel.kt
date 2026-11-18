package gabrieljs2005.com.github.caminholivreapp.ui.screens.guardian

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.data.preferences.SessionPreferences
import gabrieljs2005.com.github.caminholivreapp.data.repository.AuthRepository
import gabrieljs2005.com.github.caminholivreapp.data.repository.BarrierRepository
import gabrieljs2005.com.github.caminholivreapp.data.repository.GuardianRepository
import gabrieljs2005.com.github.caminholivreapp.domain.model.AppUser
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import gabrieljs2005.com.github.caminholivreapp.domain.model.GuardianProgress
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.domain.rules.BarrierRules
import gabrieljs2005.com.github.caminholivreapp.domain.rules.GuardianRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GuardianLocalState(
    val statusFilter: BarrierStatus? = null,
    val selectedTabIndex: Int = 0,
    val snackbarMessage: String? = null,
    val loggedOut: Boolean = false,
)

data class GuardianB2GUiState(
    val currentUser: AppUser = AuthRepository.citizenUser,
    val progress: GuardianProgress = GuardianRules.buildProgress(AppConstants.INITIAL_DEMO_XP),
    val allBarriers: List<BarrierEntity> = emptyList(),
    val rankedBarriers: List<BarrierEntity> = emptyList(),
    val statusFilter: BarrierStatus? = null,
    val selectedTabIndex: Int = 0,
    val snackbarMessage: String? = null,
    val loggedOut: Boolean = false,
) {
    val isGestor: Boolean get() = currentUser.role.isGestor
}

class GuardianB2GViewModel(
    private val barrierRepository: BarrierRepository,
    guardianRepository: GuardianRepository,
    private val sessionPreferences: SessionPreferences,
) : ViewModel() {

    private val localState = MutableStateFlow(GuardianLocalState())

    val uiState: StateFlow<GuardianB2GUiState> = combine(
        sessionPreferences.loggedUser,
        guardianRepository.progress,
        barrierRepository.barriers,
        localState,
    ) { loggedUser, progress, barriers, local ->
        val user = loggedUser ?: AuthRepository.citizenUser
        val ranked = BarrierRules.rankAndFilterB2GQueue(
            barriers = barriers,
            statusFilter = local.statusFilter,
        )
        GuardianB2GUiState(
            currentUser = user,
            progress = progress,
            allBarriers = barriers,
            rankedBarriers = ranked,
            statusFilter = local.statusFilter,
            selectedTabIndex = local.selectedTabIndex,
            snackbarMessage = local.snackbarMessage,
            loggedOut = local.loggedOut,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GuardianB2GUiState(),
    )

    fun selectTab(index: Int) {
        localState.update { it.copy(selectedTabIndex = index.coerceIn(0, 1)) }
    }

    fun selectStatusFilter(status: BarrierStatus?) {
        localState.update { it.copy(statusFilter = status) }
    }

    fun cycleBarrierStatus(barrierId: String) {
        val user = sessionPreferences.loggedUser.value ?: AuthRepository.citizenUser
        if (!user.role.isGestor) {
            localState.update {
                it.copy(snackbarMessage = "Ação restrita a funcionários públicos e gestores municipais.")
            }
            return
        }

        viewModelScope.launch {
            val updated = barrierRepository.advanceStatus(barrierId) ?: return@launch
            val msg = if (updated.status == BarrierStatus.RESOLVIDO) {
                "Obra concluída no Room! Notificação enviada aos cidadãos que monitoram a barreira."
            } else {
                "Status municipal atualizado no Room para \"${updated.status.label}\"."
            }
            localState.update { it.copy(snackbarMessage = msg) }
        }
    }

    fun logout() {
        sessionPreferences.clearLoggedUser()
        localState.update { it.copy(loggedOut = true) }
    }

    fun consumeSnackbar() {
        localState.update { it.copy(snackbarMessage = null) }
    }

    companion object {
        fun factory(
            barrierRepository: BarrierRepository,
            guardianRepository: GuardianRepository,
            sessionPreferences: SessionPreferences,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GuardianB2GViewModel(
                    barrierRepository = barrierRepository,
                    guardianRepository = guardianRepository,
                    sessionPreferences = sessionPreferences,
                )
            }
        }
    }
}

