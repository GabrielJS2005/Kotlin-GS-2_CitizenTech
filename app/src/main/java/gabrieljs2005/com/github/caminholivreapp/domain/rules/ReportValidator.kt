package gabrieljs2005.com.github.caminholivreapp.domain.rules

object ReportValidator {
    const val MIN_ADDRESS_LENGTH = 10
    const val MAX_OBSERVATION_LENGTH = 200

    fun validateAddress(address: String): String? {
        val trimmed = address.trim()
        return when {
            trimmed.isEmpty() -> "Informe o endereço ou ponto de referência da barreira."
            trimmed.length < MIN_ADDRESS_LENGTH ->
                "Endereço muito curto (mínimo de $MIN_ADDRESS_LENGTH caracteres)."
            else -> null
        }
    }

    fun validateObservation(observation: String): String? {
        return if (observation.length > MAX_OBSERVATION_LENGTH) {
            "Observação excede o limite de $MAX_OBSERVATION_LENGTH caracteres."
        } else {
            null
        }
    }

    fun isValid(address: String, observation: String = ""): Boolean {
        return validateAddress(address) == null && validateObservation(observation) == null
    }
}

