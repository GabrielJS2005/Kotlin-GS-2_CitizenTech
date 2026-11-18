package gabrieljs2005.com.github.caminholivreapp.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class BarrierStatus(
    val label: String,
    val color: Color,
    val icon: ImageVector,
) {
    ABERTO(
        label = "Aberto",
        color = Color(0xFFB71C1C),
        icon = Icons.Filled.ErrorOutline,
    ),
    EM_ANALISE(
        label = "Em Análise",
        color = Color(0xFFE65100),
        icon = Icons.Filled.Engineering,
    ),
    RESOLVIDO(
        label = "Resolvido",
        color = Color(0xFF1B5E20),
        icon = Icons.Filled.CheckCircleOutline,
    );

    companion object {
        fun fromLabel(label: String?): BarrierStatus {
            val normalized = (label ?: "").trim().lowercase()
            return entries.firstOrNull {
                it.label.lowercase() == normalized || it.name.lowercase() == normalized
            } ?: ABERTO
        }
    }
}

