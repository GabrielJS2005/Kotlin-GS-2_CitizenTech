package gabrieljs2005.com.github.caminholivreapp.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gabrieljs2005.com.github.caminholivreapp.data.repository.BarrierRepository
import gabrieljs2005.com.github.caminholivreapp.data.repository.GuardianRepository
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.domain.rules.PriorityScore
import gabrieljs2005.com.github.caminholivreapp.domain.rules.ReportValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

data class ReportFormUiState(
    val editingBarrierId: String? = null,
    val selectedCategory: BarrierCategory = BarrierCategory.AUSENCIA_RAMPA,
    val selectedSeverity: Int = 5,
    val selectedProximityFactor: Double = AppConstants.PROXIMITY_UBS_HOSPITAL,
    val address: String = "",
    val observation: String = "",
    val addressTouched: Boolean = false,
    val isSubmitting: Boolean = false,
    val completedMessage: String? = null,
) {
    val isEditMode: Boolean get() = editingBarrierId != null

    val addressError: String?
        get() = ReportValidator.validateAddress(address)

    val observationError: String?
        get() = ReportValidator.validateObservation(observation)

    val isFormValid: Boolean
        get() = addressError == null && observationError == null

    val previewScore: Double
        get() = PriorityScore.calculate(
            severity = selectedSeverity,
            confirmations = 1,
            proximityFactor = selectedProximityFactor,
            ageDecay = AppConstants.DEFAULT_AGE_DECAY,
        )
}

class ReportFormViewModel(
    private val editingBarrierId: String?,
    private val barrierRepository: BarrierRepository,
    private val guardianRepository: GuardianRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReportFormUiState(editingBarrierId = editingBarrierId)
    )
    val uiState: StateFlow<ReportFormUiState> = _uiState.asStateFlow()

    init {
        if (editingBarrierId != null) {
            viewModelScope.launch {
                val existing = barrierRepository.getById(editingBarrierId) ?: return@launch
                _uiState.update {
                    it.copy(
                        selectedCategory = existing.category,
                        selectedSeverity = existing.severity,
                        selectedProximityFactor = existing.proximityFactor,
                        address = existing.address,
                        observation = existing.observation,
                        addressTouched = true,
                    )
                }
            }
        }
    }

    fun selectCategory(category: BarrierCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectSeverity(severity: Int) {
        _uiState.update { it.copy(selectedSeverity = severity.coerceIn(1, 5)) }
    }

    fun selectProximityFactor(factor: Double) {
        _uiState.update { it.copy(selectedProximityFactor = factor) }
    }

    fun updateAddress(value: String) {
        _uiState.update {
            it.copy(
                address = value,
                addressTouched = true,
            )
        }
    }

    fun fillSampleAddress() {
        _uiState.update {
            it.copy(
                address = "Av. Paulista, 1106 - Bela Vista, São Paulo/SP",
                addressTouched = true,
            )
        }
    }

    fun updateObservation(value: String) {
        _uiState.update { it.copy(observation = value) }
    }

    fun submit() {
        val current = _uiState.value
        if (current.isSubmitting) return
        if (!current.isFormValid) {
            _uiState.update { it.copy(addressTouched = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val editId = current.editingBarrierId
            if (editId == null) {
                barrierRepository.createReport(
                    category = current.selectedCategory,
                    severity = current.selectedSeverity,
                    proximityFactor = current.selectedProximityFactor,
                    address = current.address,
                    observation = current.observation,
                )
                guardianRepository.addXp(AppConstants.XP_PER_NEW_REPORT)
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        completedMessage = "Relato em 3 toques salvo no Room! +50 XP adicionados ao seu perfil Guardião.",
                    )
                }
            } else {
                barrierRepository.updateReport(
                    barrierId = editId,
                    category = current.selectedCategory,
                    severity = current.selectedSeverity,
                    proximityFactor = current.selectedProximityFactor,
                    address = current.address,
                    observation = current.observation,
                )
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        completedMessage = "Relato atualizado com sucesso no banco local (Room).",
                    )
                }
            }
        }
    }

    companion object {
        fun isSameFactor(a: Double, b: Double): Boolean = abs(a - b) < 0.01

        fun factory(
            editingBarrierId: String?,
            barrierRepository: BarrierRepository,
            guardianRepository: GuardianRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ReportFormViewModel(
                    editingBarrierId = editingBarrierId,
                    barrierRepository = barrierRepository,
                    guardianRepository = guardianRepository,
                )
            }
        }
    }
}

