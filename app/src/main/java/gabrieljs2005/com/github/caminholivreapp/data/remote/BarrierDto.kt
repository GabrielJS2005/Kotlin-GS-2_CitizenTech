package gabrieljs2005.com.github.caminholivreapp.data.remote

import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import com.google.gson.annotations.SerializedName

data class BarrierDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("severity")
    val severity: Int? = null,
    @SerializedName("confirmations")
    val confirmations: Int? = null,
    @SerializedName("proximityFactor")
    val proximityFactor: Double? = null,
    @SerializedName("ageDecay")
    val ageDecay: Double? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
) {
    fun toEntity(nowMillis: Long = System.currentTimeMillis()): BarrierEntity {
        val cleanId = id?.takeIf { it.isNotBlank() } ?: "SP-0000"
        return BarrierEntity(
            id = cleanId,
            title = title?.takeIf { it.isNotBlank() } ?: "Barreira Urbana",
            category = BarrierCategory.fromLabel(category),
            address = address?.takeIf { it.isNotBlank() } ?: "São Paulo - SP",
            observation = "",
            severity = (severity ?: 4).coerceIn(1, 5),
            confirmations = (confirmations ?: 1).coerceAtLeast(1),
            proximityFactor = proximityFactor ?: 2.0,
            ageDecay = ageDecay ?: 1.0,
            status = BarrierStatus.fromLabel(status),
            imageUrl = imageUrl?.takeIf { it.isNotBlank() }
                ?: "https://picsum.photos/seed/calcada/600/400",
            isMonitored = false,
            isUserReported = false,
            createdAt = nowMillis,
            updatedAt = nowMillis,
        )
    }
}

