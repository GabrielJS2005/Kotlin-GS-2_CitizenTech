package gabrieljs2005.com.github.caminholivreapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface GuardianProgressDao {

    @Query("SELECT * FROM guardian_progress WHERE id = 1 LIMIT 1")
    fun observe(): Flow<GuardianProgressEntity?>

    @Query("SELECT * FROM guardian_progress WHERE id = 1 LIMIT 1")
    suspend fun get(): GuardianProgressEntity?

    @Upsert
    suspend fun upsert(entity: GuardianProgressEntity)

    @Transaction
    suspend fun addXp(deltaXp: Int): Int {
        val current = get()?.totalXp ?: AppConstants.INITIAL_DEMO_XP
        val updated = (current + deltaXp).coerceAtLeast(0)
        upsert(
            GuardianProgressEntity(
                id = 1,
                totalXp = updated,
                updatedAt = System.currentTimeMillis(),
            )
        )
        return updated
    }
}

