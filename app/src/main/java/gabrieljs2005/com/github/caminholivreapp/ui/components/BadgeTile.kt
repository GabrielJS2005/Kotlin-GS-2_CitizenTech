package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.domain.model.GuardianBadge
import gabrieljs2005.com.github.caminholivreapp.ui.theme.BorderGray
import gabrieljs2005.com.github.caminholivreapp.ui.theme.CardSurface
import gabrieljs2005.com.github.caminholivreapp.ui.theme.HighContrastText
import gabrieljs2005.com.github.caminholivreapp.ui.theme.MutedGray
import gabrieljs2005.com.github.caminholivreapp.ui.theme.SecondaryTeal
import gabrieljs2005.com.github.caminholivreapp.ui.theme.SecondaryText

@Composable
fun BadgeTile(
    badge: GuardianBadge,
    modifier: Modifier = Modifier,
) {
    val unlocked = badge.isUnlocked
    val accentColor = if (unlocked) SecondaryTeal else MutedGray

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (unlocked) 2.dp else 0.dp),
        border = BorderStroke(
            width = if (unlocked) 1.8.dp else 1.dp,
            color = if (unlocked) SecondaryTeal else BorderGray,
        ),
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Insígnia ${badge.title}, " +
                    if (unlocked) "desbloqueada" else "bloqueada, requer ${badge.requiredXp} XP"
            },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.14f),
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    imageVector = if (unlocked) badge.icon else Icons.Filled.Lock,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = badge.title,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (unlocked) HighContrastText else SecondaryText,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = badge.description,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 11.5.sp,
                lineHeight = 15.sp,
                color = SecondaryText,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = accentColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = if (unlocked) "DESBLOQUEADA" else "Meta: ${badge.requiredXp} XP",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor,
                )
            }
        }
    }
}

