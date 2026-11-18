package gabrieljs2005.com.github.caminholivreapp.ui.screens.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AccessibleForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Shield
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gabrieljs2005.com.github.caminholivreapp.data.preferences.SessionPreferences
import gabrieljs2005.com.github.caminholivreapp.domain.model.OnboardingPage
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel(
    private val sessionPreferences: SessionPreferences,
) : ViewModel() {

    private val _dontShowAgain = MutableStateFlow(false)
    val dontShowAgain: StateFlow<Boolean> = _dontShowAgain.asStateFlow()

    val pages: List<OnboardingPage> = listOf(
        OnboardingPage(
            badgeText = "1/3 • Roteamento Biomecânico",
            title = "Mobilidade Adaptada ao Seu Corpo",
            description = "Calçadas esburacadas, degraus sem rampa e falta de piso tátil não " +
                "têm o mesmo impacto para todos. Selecione seu perfil (Cadeirante, " +
                "Deficiência Visual, Idoso ou Carrinho de Bebê) e veja o aplicativo " +
                "identificar bloqueios físicos (Custo = ∞) em tempo real.",
            odsHighlight = "ODS 11 (Metas 11.2 e 11.7) & ODS 10 (Meta 10.2): Espaços públicos seguros, inclusivos e acessíveis.",
            icon = Icons.AutoMirrored.Filled.AccessibleForward,
            accentColor = PrimaryNavy,
        ),
        OnboardingPage(
            badgeText = "2/3 • Crowdsourcing Gamificado",
            title = "Missão Guardiões da Calçada",
            description = "Para vencer a escassez inicial de dados nas cidades (cold start), " +
                "cidadãos e voluntários atuam como Scouts: relate barreiras em " +
                "apenas 3 toques ou responda \"Ainda está lá?\" para ganhar XP, " +
                "subir de nível e desbloquear insígnias de impacto social.",
            odsHighlight = "ODS 3 (Saúde e Bem-Estar): Prevenção direta de quedas severas em idosos e acidentes no passeio.",
            icon = Icons.Filled.Shield,
            accentColor = SecondaryTeal,
        ),
        OnboardingPage(
            badgeText = "3/3 • Inteligência Pública (B2G)",
            title = "Zeladoria Preditiva (B2G)",
            description = "Cada relato confirmado alimenta uma equação matemática de Score de " +
                "Priorização que multiplica a urgência de reparos próximos a UBSs, " +
                "hospitais e escolas, orientando o orçamento da prefeitura com " +
                "transparência auditável.",
            odsHighlight = "ODS 9 (Inovação e Infraestrutura): Alocação inteligente de obras públicas baseada em dados abertos.",
            icon = Icons.Filled.AccountBalance,
            accentColor = AccentAmber,
        ),
    )

    fun setDontShowAgain(checked: Boolean) {
        _dontShowAgain.value = checked
    }

    fun persistPreferenceOnFinish() {
        sessionPreferences.setSkipOnboarding(_dontShowAgain.value)
    }

    companion object {
        fun factory(sessionPreferences: SessionPreferences): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    OnboardingViewModel(sessionPreferences)
                }
            }
    }
}

