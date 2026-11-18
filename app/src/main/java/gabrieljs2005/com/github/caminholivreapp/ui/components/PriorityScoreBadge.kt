package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.ui.theme.AccentAmber
import gabrieljs2005.com.github.caminholivreapp.ui.theme.CriticalRed
import gabrieljs2005.com.github.caminholivreapp.ui.theme.PrimaryNavy
import java.util.Locale

@Composable
fun PriorityScoreBadge(
    score: Double,
    compact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val badgeColor = when {
        score >= 45.0 -> CriticalRed
        score >= 25.0 -> AccentAmber
        else -> PrimaryNavy
    }
    val urgencyLabel = when {
        score >= 45.0 -> "Urgência Máxima B2G"
        score >= 25.0 -> "Prioridade Alta B2G"
        else -> "Prioridade Moderada"
    }
    val formattedScore = String.format(Locale.US, "%.1f", score)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics {
                contentDescription = "Score de priorização B2G $formattedScore, $urgencyLabel"
            }
            .background(
                color = badgeColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
            )
            .border(
                width = 1.5.dp,
                color = badgeColor,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(
                horizontal = if (compact) 10.dp else 12.dp,
                vertical = if (compact) 5.dp else 7.dp,
            ),
    ) {
        Icon(
            imageVector = Icons.Filled.Analytics,
            contentDescription = null,
            tint = badgeColor,
            modifier = Modifier.size(if (compact) 16.dp else 18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (compact) {
                "Score B2G: $formattedScore"
            } else {
                "Score B2G: $formattedScore • $urgencyLabel"
            },
            fontSize = if (compact) 12.5.sp else 13.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = badgeColor,
        )
    }
}

