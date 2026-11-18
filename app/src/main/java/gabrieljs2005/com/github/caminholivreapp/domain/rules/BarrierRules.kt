package gabrieljs2005.com.github.caminholivreapp.domain.rules

import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile

object BarrierRules {

    fun filterBarriers(
        barriers: List<BarrierEntity>,
        activeProfile: MobilityProfile,
        searchQuery: String = "",
        onlyMonitored: Boolean = false,
        onlyBlockingForProfile: Boolean = false,
    ): List<BarrierEntity> {
        val query = searchQuery.trim().lowercase()
        return barriers.filter { b ->
            if (onlyMonitored && !b.isMonitored) return@filter false
            if (onlyBlockingForProfile && !b.isBlockingForProfile(activeProfile)) return@filter false
            if (query.isNotEmpty()) {
                val matchesTitle = b.title.lowercase().contains(query)
                val matchesAddress = b.address.lowercase().contains(query)
                val matchesCategory = b.category.label.lowercase().contains(query)
                val matchesObs = b.observation.lowercase().contains(query)
                return@filter matchesTitle || matchesAddress || matchesCategory || matchesObs
            }
            true
        }
    }

    fun countBlockingForProfile(
        barriers: List<BarrierEntity>,
        activeProfile: MobilityProfile,
    ): Int {
        return barriers.count { it.isBlockingForProfile(activeProfile) }
    }

    fun rankAndFilterB2GQueue(
        barriers: List<BarrierEntity>,
        statusFilter: BarrierStatus? = null,
    ): List<BarrierEntity> {
        val filtered = if (statusFilter == null) {
            barriers
        } else {
            barriers.filter { it.status == statusFilter }
        }
        return filtered.sortedByDescending { it.priorityScore }
    }

    fun nextLifecycleStatus(current: BarrierStatus): BarrierStatus {
        return when (current) {
            BarrierStatus.ABERTO -> BarrierStatus.EM_ANALISE
            BarrierStatus.EM_ANALISE -> BarrierStatus.RESOLVIDO
            BarrierStatus.RESOLVIDO -> BarrierStatus.ABERTO
        }
    }

    fun buildAutoTitle(category: BarrierCategory): String {
        return when (category) {
            BarrierCategory.AUSENCIA_RAMPA -> "Travessia sem Rebaixamento de Guia (Relato Guardião)"
            BarrierCategory.BURACO_PISO_QUEBRADO -> "Cratera / Piso Quebrado no Passeio Público"
            BarrierCategory.OBSTACULO_PASSEIO -> "Obstáculo Fixo Bloqueando Largura Livre (< 1,20m)"
            BarrierCategory.ILUMINACAO_PRECARIA -> "Trecho Pedonal com Iluminação Pública Inoperante"
            BarrierCategory.TRAVESSIA_PERIGOSA -> "Travessia Sem Foco Sonoro / Semáforo Pedonal Curto"
            BarrierCategory.CALCADA_ESTREITA -> "Estrangulamento Severo de Calçada"
            BarrierCategory.SEM_PISO_TATIL -> "Esquina Desprovida de Piso Tátil Alerta/Direcional"
        }
    }
}

