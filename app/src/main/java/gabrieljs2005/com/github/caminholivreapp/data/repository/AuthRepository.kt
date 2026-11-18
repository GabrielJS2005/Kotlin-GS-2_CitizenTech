package gabrieljs2005.com.github.caminholivreapp.data.repository

import gabrieljs2005.com.github.caminholivreapp.domain.model.AppUser
import gabrieljs2005.com.github.caminholivreapp.domain.model.UserRole

object AuthRepository {

    val citizenUser = AppUser(
        email = "cidadao@caminholivre.com.br",
        name = "Ana Cidadã (Scout da Calçada)",
        role = UserRole.CIDADAO,
        department = "Auditoria Comunitária & Engajamento Cívico",
    )
    const val CITIZEN_PASSWORD = "123456"

    val managerUser = AppUser(
        email = "gestor@sp.gov.br",
        name = "Dr. Carlos Mendes (Engenheiro B2G)",
        role = UserRole.GESTOR,
        department = "Secretaria Municipal de Obras e Mobilidade Urbana - SP",
    )
    const val MANAGER_PASSWORD = "gestor123"
    const val MANAGER_PASSWORD_ALT = "admin123"

    fun authenticate(login: String, password: String): AppUser? {
        val cleanLogin = login.trim().lowercase()
        val cleanPassword = password.trim()

        if ((cleanLogin == citizenUser.email || cleanLogin == "cidadao") &&
            cleanPassword == CITIZEN_PASSWORD
        ) {
            return citizenUser
        }

        if ((cleanLogin == managerUser.email || cleanLogin == "gestor") &&
            (cleanPassword == MANAGER_PASSWORD || cleanPassword == MANAGER_PASSWORD_ALT)
        ) {
            return managerUser
        }

        return null
    }
}

