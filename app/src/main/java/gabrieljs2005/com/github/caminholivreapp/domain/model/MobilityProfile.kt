package gabrieljs2005.com.github.caminholivreapp.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AccessibleForward
import androidx.compose.material.icons.filled.ChildFriendly
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector

enum class MobilityProfile(
    val label: String,
    val shortDescription: String,
    val icon: ImageVector,
    val maxInclinePercent: Double,
    val minWidthMeters: Double,
    val requiresRamp: Boolean,
    val requiresTactilePaving: Boolean,
) {
    CADEIRANTE(
        label = "Cadeirante",
        shortDescription = "Exige rampa (rebaixamento de guia), largura livre ≥ 1,20 m e inclinação ≤ 8,33%.",
        icon = Icons.AutoMirrored.Filled.AccessibleForward,
        maxInclinePercent = 8.33,
        minWidthMeters = 1.20,
        requiresRamp = true,
        requiresTactilePaving = false,
    ),
    DEFICIENCIA_VISUAL(
        label = "Deficiência Visual",
        shortDescription = "Prioriza piso tátil alerta/direcional, travessia segura e calçadas livres de obstáculos aéreos.",
        icon = Icons.Filled.VisibilityOff,
        maxInclinePercent = 10.0,
        minWidthMeters = 1.20,
        requiresRamp = false,
        requiresTactilePaving = true,
    ),
    IDOSO(
        label = "Idoso",
        shortDescription = "Evita desníveis abruptos, buracos com risco de queda e trechos com iluminação precária.",
        icon = Icons.Filled.Elderly,
        maxInclinePercent = 8.33,
        minWidthMeters = 1.10,
        requiresRamp = true,
        requiresTactilePaving = false,
    ),
    CARRINHO_BEBE(
        label = "Carrinho de Bebê",
        shortDescription = "Exige largura livre contínua ≥ 1,20 m, travessia rebaixada e superfície sem buracos.",
        icon = Icons.Filled.ChildFriendly,
        maxInclinePercent = 8.33,
        minWidthMeters = 1.20,
        requiresRamp = true,
        requiresTactilePaving = false,
    ),
    GUARDIAO(
        label = "Guardião Scout",
        shortDescription = "Auditor comunitário gamificado: busca barreiras ativas no território para validar e ganhar XP.",
        icon = Icons.Filled.Shield,
        maxInclinePercent = 15.0,
        minWidthMeters = 0.90,
        requiresRamp = false,
        requiresTactilePaving = false,
    );

    companion object {
        fun fromName(name: String?): MobilityProfile {
            return entries.firstOrNull {
                it.name.equals(name, ignoreCase = true)
            } ?: CADEIRANTE
        }
    }
}

