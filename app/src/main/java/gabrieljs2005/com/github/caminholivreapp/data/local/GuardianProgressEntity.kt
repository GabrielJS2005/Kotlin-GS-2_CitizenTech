package gabrieljs2005.com.github.caminholivreapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants

@Entity(tableName = "guardian_progress")
data class GuardianProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    val totalXp: Int = AppConstants.INITIAL_DEMO_XP,
    val updatedAt: Long = System.currentTimeMillis(),
)

