package gabrieljs2005.com.github.caminholivreapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BarrierDao {

    @Query("SELECT * FROM barriers ORDER BY isUserReported DESC, updatedAt DESC")
    fun observeAll(): Flow<List<BarrierEntity>>

    @Query("SELECT * FROM barriers WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<BarrierEntity?>

    @Query("SELECT * FROM barriers WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BarrierEntity?

    @Query("SELECT COUNT(*) FROM barriers")
    suspend fun count(): Int

    /**
     * Insere barreiras vindas da API sem sobrescrever registros já existentes no banco,
     * preservando confirmações, status B2G, favoritos e edições feitas localmente.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIgnoringExisting(items: List<BarrierEntity>)

    @Upsert
    suspend fun upsert(item: BarrierEntity)

    @Delete
    suspend fun delete(item: BarrierEntity)

    @Query("DELETE FROM barriers WHERE id = :id AND isUserReported = 1")
    suspend fun deleteUserReportedById(id: String): Int
}

