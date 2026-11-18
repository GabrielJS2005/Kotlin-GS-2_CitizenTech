package gabrieljs2005.com.github.caminholivreapp

import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import gabrieljs2005.com.github.caminholivreapp.domain.rules.BarrierRules
import org.junit.Assert.assertEquals
import org.junit.Test

class BarrierRulesTest {

    @Test
    fun `nextLifecycleStatus cycles through Aberto EmAnalise Resolvido`() {
        assertEquals(BarrierStatus.EM_ANALISE, BarrierRules.nextLifecycleStatus(BarrierStatus.ABERTO))
        assertEquals(BarrierStatus.RESOLVIDO, BarrierRules.nextLifecycleStatus(BarrierStatus.EM_ANALISE))
        assertEquals(BarrierStatus.ABERTO, BarrierRules.nextLifecycleStatus(BarrierStatus.RESOLVIDO))
    }

    @Test
    fun `rankAndFilterB2GQueue orders descending by priorityScore`() {
        val low = BarrierEntity(
            id = "1",
            title = "Baixa",
            category = BarrierCategory.ILUMINACAO_PRECARIA,
            address = "Rua A, 100",
            severity = 2,
            confirmations = 1,
            proximityFactor = 1.0,
            status = BarrierStatus.ABERTO,
            imageUrl = "",
        )
        val high = BarrierEntity(
            id = "2",
            title = "Alta",
            category = BarrierCategory.AUSENCIA_RAMPA,
            address = "Av. Paulista, 1000",
            severity = 5,
            confirmations = 10,
            proximityFactor = 2.5,
            status = BarrierStatus.ABERTO,
            imageUrl = "",
        )

        val ranked = BarrierRules.rankAndFilterB2GQueue(listOf(low, high))
        assertEquals("2", ranked.first().id)
    }
}

