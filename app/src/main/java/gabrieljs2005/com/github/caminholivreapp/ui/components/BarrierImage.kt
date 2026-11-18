package gabrieljs2005.com.github.caminholivreapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BlurOff
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import coil.compose.SubcomposeAsyncImage
import gabrieljs2005.com.github.caminholivreapp.ui.theme.SecondaryText

@Composable
fun BarrierImage(
    imageUrl: String,
    category: BarrierCategory,
    applyLgpdBlur: Boolean,
    onToggleBlur: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(Color(0xFFE4E7EC)),
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Registro fotográfico da barreira (${category.label})",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (applyLgpdBlur) Modifier.blur(radius = 8.dp) else Modifier
                ),
            loading = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = category.color,
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Registro Fotográfico Georreferenciado (Modo Offline)",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        fontSize = 13.5.sp,
                    )
                }
            },
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.PrivacyTip,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (applyLgpdBlur) {
                    "LGPD Edge Blur Ativo (Rostos/Placas)"
                } else {
                    "Inspeção Visual Técnica"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        Button(
            onClick = onToggleBlur,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black.copy(alpha = 0.78f),
                contentColor = Color.White,
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
        ) {
            Icon(
                imageVector = if (applyLgpdBlur) Icons.Filled.BlurOff else Icons.Filled.BlurOn,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (applyLgpdBlur) "Inspecionar Piso" else "Ativar Blur LGPD",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

