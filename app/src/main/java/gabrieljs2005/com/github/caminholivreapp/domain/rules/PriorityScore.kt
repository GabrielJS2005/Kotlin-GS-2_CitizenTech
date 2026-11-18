package gabrieljs2005.com.github.caminholivreapp.domain.rules

object PriorityScore {
    fun calculate(
        severity: Int,
        confirmations: Int,
        proximityFactor: Double,
        ageDecay: Double = AppConstants.DEFAULT_AGE_DECAY,
    ): Double {
        return severity * (1 + confirmations) * proximityFactor * ageDecay
    }

    fun proximityDescription(proximityFactor: Double): String {
        return when {
            proximityFactor >= 2.5 -> "Raio < 100m de UBS / Hospital (Fator 2,5x)"
            proximityFactor >= 2.0 -> "Raio < 200m de Escola / Transporte (Fator 2,0x)"
            else -> "Via Urbana Comum (Fator 1,0x)"
        }
    }
}

