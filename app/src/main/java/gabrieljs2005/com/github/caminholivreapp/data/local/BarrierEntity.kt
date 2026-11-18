package gabrieljs2005.com.github.caminholivreapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.domain.rules.ImpactCalculator
import gabrieljs2005.com.github.caminholivreapp.domain.rules.PriorityScore

@Entity(tableName = "barriers")
data class BarrierEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: BarrierCategory,
    val address: String,
    val observation: String = "",
    val severity: Int,
    val confirmations: Int,
    val proximityFactor: Double,
    val ageDecay: Double = AppConstants.DEFAULT_AGE_DECAY,
    val status: BarrierStatus = BarrierStatus.ABERTO,
    val imageUrl: String,
    val isMonitored: Boolean = false,
    val isUserReported: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    /**
     * Equação Oficial de Score B2G do Caminho Livre:
     * Score = Gravidade * (1 + Confirmações) * Fator_Proximidade * Decaimento_Idade
     */
    val priorityScore: Double
        get() = PriorityScore.calculate(
            severity = severity,
            confirmations = confirmations,
            proximityFactor = proximityFactor,
            ageDecay = ageDecay,
        )

    val proximityDescription: String
        get() = PriorityScore.proximityDescription(proximityFactor)

    fun getImpactForProfile(profile: MobilityProfile): String {
        return ImpactCalculator.getImpactLabel(
            category = category,
            profile = profile,
        )
    }

    fun isBlockingForProfile(profile: MobilityProfile): Boolean {
        return ImpactCalculator.isPhysicalBlock(
            category = category,
            profile = profile,
        )
    }
}

