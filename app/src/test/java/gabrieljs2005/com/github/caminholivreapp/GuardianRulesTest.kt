package br.com.fiap.citizentech.caminholivre

import gabrieljs2005.com.github.caminholivreapp.domain.rules.GuardianRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GuardianRulesTest {

    @Test
    fun `buildProgress computes level rank and unlocked badges`() {
        val progress = GuardianRules.buildProgress(170)
        assertEquals(2, progress.level)
        assertEquals(70, progress.xpIntoCurrentLevel)
        assertEquals("Auditor de Quarteirão", progress.rankTitle)

        val unlockedIds = progress.badges.filter { it.isUnlocked }.map { it.id }
        assertTrue(unlockedIds.contains("sentinela_guia"))
        assertTrue(unlockedIds.contains("olhos_tatil"))
        assertTrue(unlockedIds.contains("escudo_ubs"))
        assertFalse(unlockedIds.contains("mestre_zeladoria"))
    }
}

