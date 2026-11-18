package gabrieljs2005.com.github.caminholivreapp.domain.rules

import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import kotlin.math.roundToInt

data class ProfileImpactResult(
    val summaryLabel: String,
    val technicalExplanation: String,
    val routeCostDisplay: String,
    val isPhysicalBlock: Boolean,
    val isCriticalPenalty: Boolean,
)

object ImpactCalculator {

    fun getImpactLabel(
        category: BarrierCategory,
        profile: MobilityProfile,
    ): String {
        if (profile == MobilityProfile.GUARDIAO) {
            return "Missão de Auditoria (+15 XP)"
        }
        if (category == BarrierCategory.AUSENCIA_RAMPA &&
            profile == MobilityProfile.CADEIRANTE
        ) {
            return "BLOQUEIO FÍSICO (Custo = ∞)"
        }
        if (category == BarrierCategory.CALCADA_ESTREITA &&
            (profile == MobilityProfile.CADEIRANTE || profile == MobilityProfile.CARRINHO_BEBE)
        ) {
            return "BLOQUEIO FÍSICO (Custo = ∞)"
        }
        if (category == BarrierCategory.BURACO_PISO_QUEBRADO &&
            profile == MobilityProfile.CADEIRANTE
        ) {
            return "BLOQUEIO FÍSICO (Custo = ∞)"
        }
        if (category == BarrierCategory.SEM_PISO_TATIL &&
            profile == MobilityProfile.DEFICIENCIA_VISUAL
        ) {
            return "Penalidade Crítica (+250%)"
        }
        if (category == BarrierCategory.OBSTACULO_PASSEIO &&
            profile == MobilityProfile.DEFICIENCIA_VISUAL
        ) {
            return "Penalidade Crítica (+250%)"
        }
        if ((category == BarrierCategory.BURACO_PISO_QUEBRADO ||
                category == BarrierCategory.ILUMINACAO_PRECARIA) &&
            profile == MobilityProfile.IDOSO
        ) {
            return "Penalidade Crítica (+250%)"
        }
        return "Penalidade Severa (+120% esforço)"
    }

    fun isPhysicalBlock(
        category: BarrierCategory,
        profile: MobilityProfile,
    ): Boolean {
        return getImpactLabel(category, profile).contains("BLOQUEIO FÍSICO")
    }

    fun evaluate(
        category: BarrierCategory,
        profile: MobilityProfile,
        severity: Int,
    ): ProfileImpactResult {
        val label = getImpactLabel(category = category, profile = profile)
        val blocked = label.contains("BLOQUEIO FÍSICO")
        val critical = label.contains("+250%")

        val baseLength = AppConstants.REFERENCE_SEGMENT_METERS
        val baseWeight = 1.0

        if (profile == MobilityProfile.GUARDIAO) {
            return ProfileImpactResult(
                summaryLabel = label,
                technicalExplanation = "No perfil Guardião Scout, barreiras ativas têm custo de atração para incentivar a validação comunitária em campo.",
                routeCostDisplay = "Custo_Trecho (100m) = ${baseLength.toInt()} u.c. (Prioridade de Auditoria)",
                isPhysicalBlock = false,
                isCriticalPenalty = false,
            )
        }

        if (blocked) {
            return ProfileImpactResult(
                summaryLabel = label,
                technicalExplanation = "Aresta removida virtualmente do grafo (P_barreira = ∞). O motor A* desvia automaticamente este quarteirão para ${profile.label}.",
                routeCostDisplay = "Custo_Trecho (100m) = ∞ (Aresta Intransponível)",
                isPhysicalBlock = true,
                isCriticalPenalty = true,
            )
        }

        val penaltyMultiplier = if (critical) 2.5 else 1.2
        val severityFactor = 1.0 + (severity - 1) * 0.15
        val totalCost = baseLength * (baseWeight + (penaltyMultiplier * severityFactor))

        return ProfileImpactResult(
            summaryLabel = label,
            technicalExplanation = if (critical) {
                "Risco acentuado de acidente ou desorientação espacial para ${profile.label}. Recomenda-se rota alternativa."
            } else {
                "Exige esforço biomecânico adicional (+120%) para ${profile.label} segundo parâmetros da ABNT NBR 9050."
            },
            routeCostDisplay = "Custo_Trecho (100m) = ${totalCost.roundToInt()} m equivalentes",
            isPhysicalBlock = false,
            isCriticalPenalty = critical,
        )
    }
}

