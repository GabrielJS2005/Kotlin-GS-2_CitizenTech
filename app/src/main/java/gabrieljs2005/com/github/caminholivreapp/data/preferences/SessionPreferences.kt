package gabrieljs2005.com.github.caminholivreapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import gabrieljs2005.com.github.caminholivreapp.domain.model.AppUser
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionPreferences(context: Context) {

    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE,
    )

    private val _activeProfile = MutableStateFlow(readActiveProfile())
    val activeProfile: StateFlow<MobilityProfile> = _activeProfile.asStateFlow()

    private val _loggedUser = MutableStateFlow(readLoggedUser())
    val loggedUser: StateFlow<AppUser?> = _loggedUser.asStateFlow()

    fun shouldSkipOnboarding(): Boolean {
        return prefs.getBoolean(KEY_SKIP_ONBOARDING, false)
    }

    fun setSkipOnboarding(skip: Boolean) {
        prefs.edit().putBoolean(KEY_SKIP_ONBOARDING, skip).apply()
    }

    fun setActiveProfile(profile: MobilityProfile) {
        prefs.edit().putString(KEY_ACTIVE_PROFILE, profile.name).apply()
        _activeProfile.value = profile
    }

    fun saveLoggedUser(user: AppUser) {
        prefs.edit()
            .putString(KEY_USER_EMAIL, user.email)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_ROLE, user.role.name)
            .putString(KEY_USER_DEPT, user.department)
            .apply()
        _loggedUser.value = user
    }

    fun clearLoggedUser() {
        prefs.edit()
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_ROLE)
            .remove(KEY_USER_DEPT)
            .apply()
        _loggedUser.value = null
    }

    private fun readActiveProfile(): MobilityProfile {
        val raw = prefs.getString(KEY_ACTIVE_PROFILE, null)
        return MobilityProfile.fromName(raw)
    }

    private fun readLoggedUser(): AppUser? {
        val email = prefs.getString(KEY_USER_EMAIL, null) ?: return null
        val roleName = prefs.getString(KEY_USER_ROLE, null) ?: return null
        val name = prefs.getString(KEY_USER_NAME, null) ?: email
        val dept = prefs.getString(KEY_USER_DEPT, null) ?: "Caminho Livre"
        return AppUser(
            email = email,
            name = name,
            role = UserRole.fromName(roleName),
            department = dept,
        )
    }

    companion object {
        private const val PREFS_NAME = "caminho_livre_session_prefs"
        private const val KEY_SKIP_ONBOARDING = "skip_onboarding"
        private const val KEY_ACTIVE_PROFILE = "active_profile"
        private const val KEY_USER_EMAIL = "logged_user_email"
        private const val KEY_USER_NAME = "logged_user_name"
        private const val KEY_USER_ROLE = "logged_user_role"
        private const val KEY_USER_DEPT = "logged_user_dept"
    }
}

