package gabrieljs2005.com.github.caminholivreapp.data.repository

import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierDao
import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.data.remote.BarrierApi
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.domain.rules.BarrierRules
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class BarrierRepository(
    private val dao: BarrierDao,
    private val api: BarrierApi,
) {
    val barriers: Flow<List<BarrierEntity>> = dao.observeAll()

    fun observeById(id: String): Flow<BarrierEntity?> = dao.observeById(id)

    suspend fun getById(id: String): BarrierEntity? = dao.getById(id)

    suspend fun hasCachedData(): Boolean = dao.count() > 0

    suspend fun syncFromApi(): Result<Int> {
        return try {
            val remoteDtos = api.fetchBarriers()
            if (remoteDtos.isEmpty()) {
                return Result.failure(
                    IllegalStateException("Nenhuma barreira urbana retornada pela API no momento.")
                )
            }
            val now = System.currentTimeMillis()
            val entities = remoteDtos.mapIndexed { idx, dto ->
                dto.toEntity(nowMillis = now - idx * 1000L)
            }
            dao.insertAllIgnoringExisting(entities)
            Result.success(entities.size)
        } catch (e: Throwable) {
            Result.failure(Exception(mapNetworkErrorToMessage(e), e))
        }
    }

    suspend fun createReport(
        category: BarrierCategory,
        severity: Int,
        proximityFactor: Double,
        address: String,
        observation: String,
    ): BarrierEntity {
        val now = System.currentTimeMillis()
        val seed = (now % 100000).toInt()
        val entity = BarrierEntity(
            id = "scout_$seed",
            title = BarrierRules.buildAutoTitle(category),
            category = category,
            address = address.trim(),
            observation = observation.trim(),
            severity = severity.coerceIn(1, 5),
            confirmations = 1,
            proximityFactor = proximityFactor,
            ageDecay = AppConstants.DEFAULT_AGE_DECAY,
            status = BarrierStatus.ABERTO,
            imageUrl = "https://picsum.photos/seed/scout$seed/600/400",
            isMonitored = true,
            isUserReported = true,
            createdAt = now,
            updatedAt = now,
        )
        dao.upsert(entity)
        return entity
    }

    suspend fun updateReport(
        barrierId: String,
        category: BarrierCategory,
        severity: Int,
        proximityFactor: Double,
        address: String,
        observation: String,
    ): BarrierEntity? {
        val existing = dao.getById(barrierId) ?: return null
        if (!existing.isUserReported) return null

        val updated = existing.copy(
            title = BarrierRules.buildAutoTitle(category),
            category = category,
            severity = severity.coerceIn(1, 5),
            proximityFactor = proximityFactor,
            address = address.trim(),
            observation = observation.trim(),
            updatedAt = System.currentTimeMillis(),
        )
        dao.upsert(updated)
        return updated
    }

    suspend fun deleteUserReported(barrierId: String): Boolean {
        val existing = dao.getById(barrierId) ?: return false
        if (!existing.isUserReported) return false
        dao.delete(existing)
        return true
    }

    suspend fun toggleMonitored(barrierId: String): BarrierEntity? {
        val existing = dao.getById(barrierId) ?: return null
        val updated = existing.copy(
            isMonitored = !existing.isMonitored,
            updatedAt = System.currentTimeMillis(),
        )
        dao.upsert(updated)
        return updated
    }

    data class ConfirmationOutcome(
        val updatedBarrier: BarrierEntity,
        val wasReopened: Boolean,
    )

    suspend fun confirmStillThere(barrierId: String): ConfirmationOutcome? {
        val existing = dao.getById(barrierId) ?: return null
        val wasReopened = existing.status == BarrierStatus.RESOLVIDO
        val newStatus = if (wasReopened) BarrierStatus.ABERTO else existing.status
        val updated = existing.copy(
            confirmations = existing.confirmations + 1,
            status = newStatus,
            updatedAt = System.currentTimeMillis(),
        )
        dao.upsert(updated)
        return ConfirmationOutcome(updatedBarrier = updated, wasReopened = wasReopened)
    }

    suspend fun advanceStatus(barrierId: String): BarrierEntity? {
        val existing = dao.getById(barrierId) ?: return null
        val next = BarrierRules.nextLifecycleStatus(existing.status)
        val updated = existing.copy(
            status = next,
            updatedAt = System.currentTimeMillis(),
        )
        dao.upsert(updated)
        return updated
    }

    companion object {
        fun mapNetworkErrorToMessage(error: Throwable): String {
            return when (error) {
                is SocketTimeoutException ->
                    "Tempo limite excedido ao consultar a API de barreiras de São Paulo. Verifique sua conexão e tente novamente."
                is HttpException ->
                    "O servidor da API retornou HTTP ${error.code()}. Tente novamente."
                is IOException ->
                    "Não foi possível conectar ao servidor da API. Verifique sua internet."
                else -> error.message?.takeIf { it.isNotBlank() }
                    ?: "Falha ao sincronizar os dados de barreiras de São Paulo. Tente novamente."
            }
        }
    }
}

