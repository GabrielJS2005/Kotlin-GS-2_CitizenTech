package gabrieljs2005.com.github.caminholivreapp.data.repository

import gabrieljs2005.com.github.caminholivreapp.data.local.GuardianProgressDao
import gabrieljs2005.com.github.caminholivreapp.domain.model.GuardianProgress
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.domain.rules.GuardianRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GuardianRepository(
    private val dao: GuardianProgressDao,
) {
    val progress: Flow<GuardianProgress> = dao.observe().map { entity ->
        val xp = entity?.totalXp ?: AppConstants.INITIAL_DEMO_XP
        GuardianRules.buildProgress(xp)
    }

    suspend fun addXp(deltaXp: Int): Int {
        return dao.addXp(deltaXp)
    }
}

