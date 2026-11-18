package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonPinCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BarrierCard(
    barrier: BarrierEntity,
    activeProfile: MobilityProfile,
    onClick: () -> Unit,
    onToggleMonitor: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPhysicalBlock = barrier.isBlockingForProfile(activeProfile)
    val impactText = barrier.getImpactForProfile(activeProfile)
    val borderColor = if (isPhysicalBlock) CriticalRed else BorderGray

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPhysicalBlock) 4.dp else 2.dp
        ),
        border = BorderStroke(
            width = if (isPhysicalBlock) 2.4.dp else 1.2.dp,
            color = borderColor,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .semantics {
                contentDescription =
                    "${barrier.title}, categoria ${barrier.category.label}, impacto $impactText"
            }
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = barrier.category.color.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .border(
                            width = 1.2.dp,
                            color = barrier.category.color,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Icon(
                        imageVector = barrier.category.icon,
                        contentDescription = null,
                        tint = barrier.category.color,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = barrier.category.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = barrier.category.color,
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = barrier.status.color.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = barrier.status.label,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = barrier.status.color,
                    )
                }

                if (barrier.isUserReported) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = SecondaryTeal.copy(alpha = 0.14f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(horizontal = 7.dp, vertical = 4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PersonPinCircle,
                            contentDescription = null,
                            tint = SecondaryTeal,
                            modifier = Modifier.size(13.dp),
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Seu Relato",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SecondaryTeal,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onToggleMonitor) {
                    Icon(
                        imageVector = if (barrier.isMonitored) {
                            Icons.Filled.NotificationsActive
                        } else {
                            Icons.Filled.NotificationsNone
                        },
                        contentDescription = if (barrier.isMonitored) {
                            "Remover monitoramento de reparo"
                        } else {
                            "Monitorar reparo municipal"
                        },
                        tint = if (barrier.isMonitored) AccentAmber else SecondaryText,
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = barrier.title,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HighContrastText,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = null,
                    tint = SecondaryText,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = barrier.address,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecondaryText,
                )
            }

            if (barrier.observation.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Obs.: ${barrier.observation}",
                    fontSize = 12.5.sp,
                    color = SecondaryText,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val impactTint = when {
                isPhysicalBlock -> CriticalRed
                activeProfile == MobilityProfile.GUARDIAO -> SecondaryTeal
                else -> PrimaryNavy
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = impactTint.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(10.dp),
                    )
                    .border(
                        width = 1.2.dp,
                        color = impactTint.copy(alpha = if (isPhysicalBlock) 1f else 0.5f),
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Icon(
                    imageVector = if (isPhysicalBlock) Icons.Filled.Block else activeProfile.icon,
                    contentDescription = null,
                    tint = impactTint,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${activeProfile.label}: $impactText",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = impactTint,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SeverityIndicator(severity = barrier.severity)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.VerifiedUser,
                        contentDescription = null,
                        tint = SecondaryTeal,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${barrier.confirmations} validações",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryTeal,
                    )
                }
                PriorityScoreBadge(
                    score = barrier.priorityScore,
                    compact = true,
                )
            }
        }
    }
}

