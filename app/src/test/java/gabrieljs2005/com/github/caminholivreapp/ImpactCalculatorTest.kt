package gabrieljs2005.com.github.caminholivreapp

import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.domain.rules.ImpactCalculator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImpactCalculatorTest {

    @Test
    fun `ausencia de rampa is physical block for cadeirante`() {
        val result = ImpactCalculator.evaluate(
            category = BarrierCategory.AUSENCIA_RAMPA,
            profile = MobilityProfile.CADEIRANTE,
            severity = 5,
        )
        assertTrue(result.isPhysicalBlock)
        assertTrue(result.summaryLabel.contains("BLOQUEIO FÍSICO"))
    }

    @Test
    fun `sem piso tatil is critical penalty for deficiencia visual`() {
        val result = ImpactCalculator.evaluate(
            category = BarrierCategory.SEM_PISO_TATIL,
            profile = MobilityProfile.DEFICIENCIA_VISUAL,
            severity = 4,
        )
        assertFalse(result.isPhysicalBlock)
        assertTrue(result.isCriticalPenalty)
        assertTrue(result.summaryLabel.contains("+250%"))
    }

    @Test
    fun `guardiao profile treats barriers as audit missions`() {
        val result = ImpactCalculator.evaluate(
            category = BarrierCategory.BURACO_PISO_QUEBRADO,
            profile = MobilityProfile.GUARDIAO,
            severity = 5,
        )
        assertFalse(result.isPhysicalBlock)
        assertTrue(result.summaryLabel.contains("+15 XP"))
    }
}

