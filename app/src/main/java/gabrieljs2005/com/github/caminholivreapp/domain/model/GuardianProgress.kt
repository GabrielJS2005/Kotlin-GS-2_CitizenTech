package gabrieljs2005.com.github.caminholivreapp.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class GuardianBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val requiredXp: Int,
    val isUnlocked: Boolean,
)

data class GuardianProgress(
    val totalXp: Int,
    val level: Int,
    val rankTitle: String,
    val xpIntoCurrentLevel: Int,
    val xpForNextLevel: Int,
    val protectedCitizensEstimate: Int,
    val badges: List<GuardianBadge>,
) {
    val levelProgress: Float
        get() = if (xpForNextLevel == 0) {
            1f
        } else {
            (xpIntoCurrentLevel.toFloat() / xpForNextLevel.toFloat()).coerceIn(0f, 1f)
        }
}

