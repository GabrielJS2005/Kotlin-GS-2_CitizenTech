package gabrieljs2005.com.github.caminholivreapp.domain.model

enum class UserRole(
    val label: String,
    val description: String,
) {
    CIDADAO(
        label = "Cidadão / Scout",
        description = "Acesso restrito ao Painel Guardião Scout (gamificação, missões de auditoria, XP e insígnias).",
    ),
    GESTOR(
        label = "Gestor Público B2G",
        description = "Acesso irrestrito: Painel Guardião Scout e Fila de Obras da Prefeitura ordenada por Score B2G.",
    );

    val isGestor: Boolean get() = this == GESTOR
    val isCidadao: Boolean get() = this == CIDADAO

    companion object {
        fun fromName(name: String?): UserRole {
            return entries.firstOrNull {
                it.name.equals(name, ignoreCase = true)
            } ?: CIDADAO
        }
    }
}

data class AppUser(
    val email: String,
    val name: String,
    val role: UserRole,
    val department: String,
)

