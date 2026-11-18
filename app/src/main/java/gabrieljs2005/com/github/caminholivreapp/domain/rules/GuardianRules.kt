package gabrieljs2005.com.github.caminholivreapp.domain.rules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AccessibleForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import gabrieljs2005.com.github.caminholivreapp.domain.model.GuardianBadge
import gabrieljs2005.com.github.caminholivreapp.domain.model.GuardianProgress

object GuardianRules {

    fun buildProgress(totalXp: Int): GuardianProgress {
        val safeXp = totalXp.coerceAtLeast(0)
        val level = (safeXp / AppConstants.XP_PER_LEVEL) + 1
        val xpIntoCurrentLevel = safeXp % AppConstants.XP_PER_LEVEL
        val rankTitle = rankTitleForLevel(level)

        // Estimativa tangível de empatia ("Suas auditorias protegeram X cidadãos")
        val protectedCitizens = (safeXp / 15) * 4

        val badges = listOf(
            GuardianBadge(
                id = "sentinela_guia",
                title = "Sentinela da Guia",
                description = "Iniciou auditoria comunitária de rampas e travessias.",
                icon = Icons.AutoMirrored.Filled.AccessibleForward,
                requiredXp = 50,
                isUnlocked = safeXp >= 50,
            ),
            GuardianBadge(
                id = "olhos_tatil",
                title = "Farol Tátil",
                description = "Validou barreiras críticas para pessoas com baixa visão.",
                icon = Icons.Filled.Visibility,
                requiredXp = 120,
                isUnlocked = safeXp >= 120,
            ),
            GuardianBadge(
                id = "escudo_ubs",
                title = "Guardião da Saúde (UBS)",
                description = "Auditou calçadas no raio de 200m de hospitais e UBSs.",
                icon = Icons.Filled.LocalHospital,
                requiredXp = 160,
                isUnlocked = safeXp >= 160,
            ),
            GuardianBadge(
                id = "mestre_zeladoria",
                title = "Mestre da Zeladoria B2G",
                description = "Contribuiu decisivamente para o Score de obras públicas.",
                icon = Icons.Filled.WorkspacePremium,
                requiredXp = 220,
                isUnlocked = safeXp >= 220,
            ),
            GuardianBadge(
                id = "pioneiro_bairro",
                title = "Explorador Anti-ColdStart",
                description = "Venceu a escassez de dados mapeando novos quarteirões.",
                icon = Icons.Filled.Explore,
                requiredXp = 300,
                isUnlocked = safeXp >= 300,
            ),
            GuardianBadge(
                id = "lenda_caminho_livre",
                title = "Lenda Caminho Livre",
                description = "Referência máxima de impacto cívico e inclusão urbana.",
                icon = Icons.Filled.EmojiEvents,
                requiredXp = 400,
                isUnlocked = safeXp >= 400,
            ),
        )

        return GuardianProgress(
            totalXp = safeXp,
            level = level,
            rankTitle = rankTitle,
            xpIntoCurrentLevel = xpIntoCurrentLevel,
            xpForNextLevel = AppConstants.XP_PER_LEVEL,
            protectedCitizensEstimate = protectedCitizens,
            badges = badges,
        )
    }

    fun rankTitleForLevel(level: Int): String {
        return when {
            level <= 1 -> "Scout Iniciante"
            level == 2 -> "Auditor de Quarteirão"
            level == 3 -> "Guardião da Calçada"
            level == 4 -> "Sentinela Urbano"
            else -> "Embaixador CitizenTech"
        }
    }
}

