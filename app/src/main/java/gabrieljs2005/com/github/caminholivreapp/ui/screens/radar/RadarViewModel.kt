package gabrieljs2005.com.github.caminholivreapp.ui.screens.radar

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
import gabrieljs2005.com.github.caminholivreapp.domain.rules.BarrierRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SyncStatus {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR,
}

data class RadarFilterState(
    val searchQuery: String = "",
    val onlyMonitored: Boolean = false,
    val onlyBlockingForProfile: Boolean = false,
)

data class RadarSyncState(
    val status: SyncStatus = SyncStatus.IDLE,
    val errorMessage: String = "",
    val snackbarMessage: String? = null,
)

data class RadarUiState(
    val allBarriers: List<BarrierEntity> = emptyList(),
    val visibleBarriers: List<BarrierEntity> = emptyList(),
    val activeProfile: MobilityProfile = MobilityProfile.CADEIRANTE,
    val searchQuery: String = "",
    val onlyMonitored: Boolean = false,
    val onlyBlockingForProfile: Boolean = false,
    val blockingCount: Int = 0,
    val monitoredCount: Int = 0,
    val guardianXp: Int = AppConstants.INITIAL_DEMO_XP,
    val syncStatus: SyncStatus = SyncStatus.IDLE,
    val errorMessage: String = "",
    val snackbarMessage: String? = null,
) {
    val hasBarriersInDb: Boolean get() = allBarriers.isNotEmpty()
}

class RadarViewModel(
    private val barrierRepository: BarrierRepository,
    private val guardianRepository: GuardianRepository,
    private val sessionPreferences: SessionPreferences,
) : ViewModel() {

    private val filters = MutableStateFlow(RadarFilterState())
    private val syncState = MutableStateFlow(RadarSyncState())

    val uiState: StateFlow<RadarUiState> = combine(
        barrierRepository.barriers,
        sessionPreferences.activeProfile,
        guardianRepository.progress,
        filters,
        syncState,
    ) { allBarriers, profile, progress, filterState, sync ->
        val filtered = BarrierRules.filterBarriers(
            barriers = allBarriers,
            activeProfile = profile,
            searchQuery = filterState.searchQuery,
            onlyMonitored = filterState.onlyMonitored,
            onlyBlockingForProfile = filterState.onlyBlockingForProfile,
        )
        RadarUiState(
            allBarriers = allBarriers,
            visibleBarriers = filtered,
            activeProfile = profile,
            searchQuery = filterState.searchQuery,
            onlyMonitored = filterState.onlyMonitored,
            onlyBlockingForProfile = filterState.onlyBlockingForProfile,
            blockingCount = BarrierRules.countBlockingForProfile(allBarriers, profile),
            monitoredCount = allBarriers.count { it.isMonitored },
            guardianXp = progress.totalXp,
            syncStatus = sync.status,
            errorMessage = sync.errorMessage,
            snackbarMessage = sync.snackbarMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RadarUiState(),
    )

    fun scanUrbanMesh() {
        if (syncState.value.status == SyncStatus.LOADING) return
        viewModelScope.launch {
            syncState.update { it.copy(status = SyncStatus.LOADING, errorMessage = "") }
            val result = barrierRepository.syncFromApi()
            result.fold(
                onSuccess = { count ->
                    syncState.update {
                        it.copy(
                            status = SyncStatus.SUCCESS,
                            errorMessage = "",
                            snackbarMessage = "Malha urbana sincronizada ($count barreiras verificadas no Room).",
                        )
                    }
                },
                onFailure = { error ->
                    val msg = error.message ?: "Falha inesperada ao consultar a API de barreiras urbanas."
                    val hasCached = barrierRepository.hasCachedData()
                    syncState.update {
                        it.copy(
                            status = if (hasCached) SyncStatus.SUCCESS else SyncStatus.ERROR,
                            errorMessage = msg,
                            snackbarMessage = if (hasCached) "Modo offline (dados do Room): $msg" else null,
                        )
                    }
                },
            )
        }
    }

    fun selectProfile(profile: MobilityProfile) {
        sessionPreferences.setActiveProfile(profile)
    }

    fun updateSearchQuery(query: String) {
        filters.update { it.copy(searchQuery = query) }
    }

    fun toggleOnlyMonitored() {
        filters.update { it.copy(onlyMonitored = !it.onlyMonitored) }
    }

    fun setOnlyMonitored(value: Boolean) {
        filters.update { it.copy(onlyMonitored = value) }
    }

    fun setOnlyBlockingForProfile(value: Boolean) {
        filters.update { it.copy(onlyBlockingForProfile = value) }
    }

    fun clearFilters() {
        filters.value = RadarFilterState()
    }

    fun toggleMonitorBarrier(barrierId: String) {
        viewModelScope.launch {
            val updated = barrierRepository.toggleMonitored(barrierId) ?: return@launch
            val feedback = if (updated.isMonitored) {
                "Barreira adicionada ao monitoramento de obras municipais."
            } else {
                "Monitoramento removido para esta barreira."
            }
            syncState.update { it.copy(snackbarMessage = feedback) }
        }
    }

    fun showSnackbarMessage(message: String) {
        syncState.update { it.copy(snackbarMessage = message) }
    }

    fun consumeSnackbarMessage() {
        syncState.update { it.copy(snackbarMessage = null) }
    }

    companion object {
        fun factory(
            barrierRepository: BarrierRepository,
            guardianRepository: GuardianRepository,
            sessionPreferences: SessionPreferences,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RadarViewModel(
                    barrierRepository = barrierRepository,
                    guardianRepository = guardianRepository,
                    sessionPreferences = sessionPreferences,
                )
            }
        }
    }
}

