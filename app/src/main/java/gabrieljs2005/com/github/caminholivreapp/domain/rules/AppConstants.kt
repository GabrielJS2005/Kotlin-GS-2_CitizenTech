package gabrieljs2005.com.github.caminholivreapp.domain.rules

object AppConstants {
    const val APP_NAME = "Caminho Livre"
    const val APP_TAGLINE = "Mobilidade Urbana Inclusiva & Zeladoria Preditiva (CitizenTech)"

    // Tempo de exibição da Splash Screen antes do redirecionamento automático
    const val SPLASH_DELAY_MS = 2000L

    // Regras de pontuação de experiência (XP) - Guardiões da Calçada
    const val XP_PER_VALIDATION = 15
    const val XP_PER_NEW_REPORT = 50
    const val XP_PER_LEVEL = 100
    const val INITIAL_DEMO_XP = 120

    // Fatores de proximidade espacial a equipamentos públicos (Score B2G)
    const val PROXIMITY_UBS_HOSPITAL = 2.5   // < 100m UBS / Hospital
    const val PROXIMITY_SCHOOL_TRANSPORT = 2.0 // < 200m Escola / Ônibus
    const val PROXIMITY_REGULAR_STREET = 1.0  // Via comum

    // Decaimento de idade padrão para fase de testes
    const val DEFAULT_AGE_DECAY = 1.0

    // Comprimento médio de referência de quadra (metros) para exibição de Custo(e,P)
    const val REFERENCE_SEGMENT_METERS = 100.0
}

