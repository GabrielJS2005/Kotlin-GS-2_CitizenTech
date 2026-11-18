package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.ui.theme.BorderGray
import gabrieljs2005.com.github.caminholivreapp.ui.theme.HighContrastText
import gabrieljs2005.com.github.caminholivreapp.ui.theme.PrimaryNavy
import gabrieljs2005.com.github.caminholivreapp.ui.theme.SecondaryText
import java.util.Locale

@Composable
fun ProfileSelectorBar(
    selectedProfile: MobilityProfile,
    onProfileSelected: (MobilityProfile) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = null,
                    tint = PrimaryNavy,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Perfil Biomecânico Ativo (Recálculo em Tempo Real):",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HighContrastText,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                MobilityProfile.entries.forEach { profile ->
                    val isSelected = profile == selectedProfile
                    FilterChip(
                        selected = isSelected,
                        onClick = { onProfileSelected(profile) },
                        label = {
                            Text(
                                text = profile.label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = profile.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else PrimaryNavy,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryNavy,
                            selectedLabelColor = Color.White,
                        ),
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryNavy.copy(alpha = 0.06f),
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Icon(
                    imageVector = selectedProfile.icon,
                    contentDescription = null,
                    tint = PrimaryNavy,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                val minWidthStr = String.format(Locale.US, "%.2f", selectedProfile.minWidthMeters)
                val maxInclineStr = String.format(Locale.US, "%.1f", selectedProfile.maxInclinePercent)
                Text(
                    text = "${selectedProfile.shortDescription} (Largura mín.: ${minWidthStr}m • Inclin. máx.: ${maxInclineStr}%)",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryText,
                )
            }
        }
        HorizontalDivider(color = BorderGray, thickness = 1.2.dp)
    }
}

