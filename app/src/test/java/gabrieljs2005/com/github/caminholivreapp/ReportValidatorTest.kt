package gabrieljs2005.com.github.caminholivreapp

import gabrieljs2005.com.github.caminholivreapp.domain.rules.ReportValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportValidatorTest {

    @Test
    fun `validateAddress rejects blank and short addresses`() {
        assertNotNull(ReportValidator.validateAddress(""))
        assertNotNull(ReportValidator.validateAddress("   "))
        assertNotNull(ReportValidator.validateAddress("Rua A, 1"))
        assertFalse(ReportValidator.isValid("Rua A, 1"))
    }

    @Test
    fun `validateAddress accepts addresses with 10 or more characters`() {
        val valid = "Av. Paulista, 1106 - Bela Vista"
        assertNull(ReportValidator.validateAddress(valid))
        assertTrue(ReportValidator.isValid(valid))
    }

    @Test
    fun `validateObservation rejects strings over 200 characters`() {
        val tooLong = "a".repeat(201)
        assertNotNull(ReportValidator.validateObservation(tooLong))
        assertFalse(ReportValidator.isValid("Av. Paulista, 1106", tooLong))
    }
}

