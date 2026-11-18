package gabrieljs2005.com.github.caminholivreapp.ui.navigation

import android.net.Uri

object AppRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val RADAR = "radar"

    const val ARG_BARRIER_ID = "barrierId"
    const val DETAIL = "detail/{$ARG_BARRIER_ID}"
    const val REPORT = "report?$ARG_BARRIER_ID={$ARG_BARRIER_ID}"

    const val LOGIN = "login"
    const val GUARDIAN = "guardian"

    fun detail(barrierId: String): String = "detail/${Uri.encode(barrierId)}"

    fun report(barrierId: String? = null): String {
        return if (barrierId.isNullOrBlank()) {
            "report"
        } else {
            "report?$ARG_BARRIER_ID=${Uri.encode(barrierId)}"
        }
    }
}

