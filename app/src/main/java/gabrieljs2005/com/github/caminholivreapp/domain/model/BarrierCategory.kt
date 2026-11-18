package gabrieljs2005.com.github.caminholivreapp.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Texture
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class BarrierCategory(
    val label: String,
    val icon: ImageVector,
    val color: Color,
) {
    BURACO_PISO_QUEBRADO(
        label = "Buraco/Piso Quebrado",
        icon = Icons.Filled.BrokenImage,
        color = Color(0xFFD84315),
    ),
    AUSENCIA_RAMPA(
        label = "Ausência de Rampa",
        icon = Icons.Filled.Stairs,
        color = Color(0xFFB71C1C),
    ),
    OBSTACULO_PASSEIO(
        label = "Obstáculo no Passeio",
        icon = Icons.Filled.WarningAmber,
        color = Color(0xFFE65100),
    ),
    ILUMINACAO_PRECARIA(
        label = "Iluminação Precária",
        icon = Icons.Filled.Lightbulb,
        color = Color(0xFF4527A0),
    ),
    TRAVESSIA_PERIGOSA(
        label = "Travessia Perigosa",
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
        color = Color(0xFFC62828),
    ),
    CALCADA_ESTREITA(
        label = "Calçada Estreita",
        icon = Icons.Filled.Compress,
        color = Color(0xFFAD1457),
    ),
    SEM_PISO_TATIL(
        label = "Sem Piso Tátil",
        icon = Icons.Filled.Texture,
        color = Color(0xFF00695C),
    );

    companion object {
        fun fromLabel(label: String?): BarrierCategory {
            val normalized = (label ?: "").trim().lowercase()
            return entries.firstOrNull {
                it.label.lowercase() == normalized || it.name.lowercase() == normalized
            } ?: OBSTACULO_PASSEIO
        }
    }
}

