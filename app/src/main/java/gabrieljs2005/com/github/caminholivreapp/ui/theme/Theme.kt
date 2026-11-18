package gabrieljs2005.com.github.caminholivreapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightHighContrastColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0EAFF),
    onPrimaryContainer = PrimaryNavy,
    secondary = SecondaryTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2F1),
    onSecondaryContainer = SecondaryTeal,
    tertiary = AccentAmber,
    onTertiary = Color.White,
    error = CriticalRed,
    onError = Color.White,
    background = SurfaceBackground,
    onBackground = HighContrastText,
    surface = CardSurface,
    onSurface = HighContrastText,
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = SecondaryText,
    outline = BorderGray,
)

@Composable
fun CaminhoLivreTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightHighContrastColorScheme,
        typography = AppTypography,
        content = content,
    )
}

