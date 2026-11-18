package gabrieljs2005.com.github.caminholivreapp.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class OnboardingPage(
    val badgeText: String,
    val title: String,
    val description: String,
    val odsHighlight: String,
    val icon: ImageVector,
    val accentColor: Color,
)

