package br.com.fiap.citizentech.caminholivre

import gabrieljs2005.com.github.caminholivreapp.domain.rules.PriorityScore
import org.junit.Assert.assertEquals
import org.junit.Test

class PriorityScoreTest {

    @Test
    fun `calculate applies official B2G priority score equation`() {
        // Gravidade 5 * (1 + 12 confirmações) * Fator 2.0 * Decaimento 1.0 = 130.0
        val score = PriorityScore.calculate(
            severity = 5,
            confirmations = 12,
            proximityFactor = 2.0,
            ageDecay = 1.0,
        )
        assertEquals(130.0, score, 0.001)
    }

    @Test
    fun `proximityDescription returns expected description for each factor`() {
        assertEquals(
            "Raio < 100m de UBS / Hospital (Fator 2,5x)",
            PriorityScore.proximityDescription(2.5),
        )
        assertEquals(
            "Raio < 200m de Escola / Transporte (Fator 2,0x)",
            PriorityScore.proximityDescription(2.0),
        )
        assertEquals(
            "Via Urbana Comum (Fator 1,0x)",
            PriorityScore.proximityDescription(1.0),
        )
    }
}

