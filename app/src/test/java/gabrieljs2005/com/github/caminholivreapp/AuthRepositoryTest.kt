package gabrieljs2005.com.github.caminholivreapp

import gabrieljs2005.com.github.caminholivreapp.data.remote.BarrierDto
import gabrieljs2005.com.github.caminholivreapp.data.repository.AuthRepository
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.UserRole
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AuthRepositoryTest {

    @Test
    fun `authenticate validates citizen and manager credentials`() {
        val citizen = AuthRepository.authenticate("cidadao@caminholivre.com.br", "123456")
        assertNotNull(citizen)
        assertEquals(UserRole.CIDADAO, citizen?.role)

        val manager = AuthRepository.authenticate("gestor@sp.gov.br", "gestor123")
        assertNotNull(manager)
        assertEquals(UserRole.GESTOR, manager?.role)

        val invalid = AuthRepository.authenticate("gestor@sp.gov.br", "senha_errada")
        assertNull(invalid)
    }

    @Test
    fun `BarrierDto toEntity applies safe defaults`() {
        val dto = BarrierDto(
            id = "SP-1001",
            title = "Meio-fio Elevado",
            category = "Ausência de Rampa",
            address = "Av. Paulista, 1106",
            severity = 5,
            confirmations = 12,
            proximityFactor = 2.0,
            ageDecay = 1.0,
            status = "Aberto",
            imageUrl = null,
        )
        val entity = dto.toEntity()
        assertEquals("SP-1001", entity.id)
        assertEquals(BarrierCategory.AUSENCIA_RAMPA, entity.category)
        assertTrue(entity.imageUrl.isNotBlank())
    }

    private fun assertTrue(condition: Boolean) = Assert.assertTrue(condition)
}

