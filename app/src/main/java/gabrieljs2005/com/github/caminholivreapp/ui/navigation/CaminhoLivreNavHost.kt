package gabrieljs2005.com.github.caminholivreapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import gabrieljs2005.com.github.caminholivreapp.di.AppContainer
import gabrieljs2005.com.github.caminholivreapp.ui.screens.detail.BarrierDetailScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.detail.BarrierDetailViewModel
import gabrieljs2005.com.github.caminholivreapp.ui.screens.guardian.GuardianB2GScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.guardian.GuardianB2GViewModel
import gabrieljs2005.com.github.caminholivreapp.ui.screens.login.LoginScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.login.LoginViewModel
import gabrieljs2005.com.github.caminholivreapp.ui.screens.onboarding.OnboardingScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.onboarding.OnboardingViewModel
import gabrieljs2005.com.github.caminholivreapp.ui.screens.radar.RadarScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.radar.RadarViewModel
import gabrieljs2005.com.github.caminholivreapp.ui.screens.report.ReportFormScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.report.ReportFormViewModel
import gabrieljs2005.com.github.caminholivreapp.ui.screens.splash.SplashScreen
import gabrieljs2005.com.github.caminholivreapp.ui.screens.splash.SplashViewModel

@Composable
fun CaminhoLivreNavHost(
    appContainer: AppContainer,
) {
    val navController = rememberNavController()
    var pendingRadarSnackbar by rememberSaveable { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH,
    ) {
        composable(route = AppRoutes.SPLASH) {
            val vm: SplashViewModel = viewModel(
                factory = SplashViewModel.factory(appContainer.sessionPreferences)
            )
            SplashScreen(
                viewModel = vm,
                onNavigateToOnboarding = {
                    navController.navigate(AppRoutes.ONBOARDING) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToRadar = {
                    navController.navigate(AppRoutes.RADAR) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(route = AppRoutes.ONBOARDING) {
            val vm: OnboardingViewModel = viewModel(
                factory = OnboardingViewModel.factory(appContainer.sessionPreferences)
            )
            OnboardingScreen(
                viewModel = vm,
                onFinishOnboarding = {
                    navController.navigate(AppRoutes.RADAR) {
                        popUpTo(AppRoutes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }

        composable(route = AppRoutes.RADAR) {
            val vm: RadarViewModel = viewModel(
                factory = RadarViewModel.factory(
                    barrierRepository = appContainer.barrierRepository,
                    guardianRepository = appContainer.guardianRepository,
                    sessionPreferences = appContainer.sessionPreferences,
                )
            )
            RadarScreen(
                viewModel = vm,
                externalSnackbarMessage = pendingRadarSnackbar,
                onConsumeExternalSnackbar = { pendingRadarSnackbar = null },
                onOpenDetail = { barrierId ->
                    navController.navigate(AppRoutes.detail(barrierId))
                },
                onOpenNewReport = {
                    navController.navigate(AppRoutes.report(null))
                },
                onOpenGuardianB2G = {
                    val hasSession = appContainer.sessionPreferences.loggedUser.value != null
                    if (hasSession) {
                        navController.navigate(AppRoutes.GUARDIAN)
                    } else {
                        navController.navigate(AppRoutes.LOGIN)
                    }
                },
            )
        }

        composable(
            route = AppRoutes.DETAIL,
            arguments = listOf(
                navArgument(AppRoutes.ARG_BARRIER_ID) {
                    type = NavType.StringType
                }
            ),
        ) { backStackEntry ->
            val barrierId = backStackEntry.arguments?.getString(AppRoutes.ARG_BARRIER_ID).orEmpty()
            val vm: BarrierDetailViewModel = viewModel(
                factory = BarrierDetailViewModel.factory(
                    barrierId = barrierId,
                    barrierRepository = appContainer.barrierRepository,
                    guardianRepository = appContainer.guardianRepository,
                    sessionPreferences = appContainer.sessionPreferences,
                )
            )
            BarrierDetailScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onEditReport = { id ->
                    navController.navigate(AppRoutes.report(id))
                },
                onDeletedSuccess = {
                    pendingRadarSnackbar = "Relato excluído do banco local (Room)."
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = AppRoutes.REPORT,
            arguments = listOf(
                navArgument(AppRoutes.ARG_BARRIER_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
        ) { backStackEntry ->
            val editingId = backStackEntry.arguments?.getString(AppRoutes.ARG_BARRIER_ID)
            val vm: ReportFormViewModel = viewModel(
                factory = ReportFormViewModel.factory(
                    editingBarrierId = editingId,
                    barrierRepository = appContainer.barrierRepository,
                    guardianRepository = appContainer.guardianRepository,
                )
            )
            ReportFormScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onSavedSuccess = { feedbackMessage ->
                    pendingRadarSnackbar = feedbackMessage
                    navController.popBackStack()
                },
            )
        }

        composable(route = AppRoutes.LOGIN) {
            val vm: LoginViewModel = viewModel(
                factory = LoginViewModel.factory(appContainer.sessionPreferences)
            )
            LoginScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onAuthenticated = {
                    navController.navigate(AppRoutes.GUARDIAN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
            )
        }

        composable(route = AppRoutes.GUARDIAN) {
            val vm: GuardianB2GViewModel = viewModel(
                factory = GuardianB2GViewModel.factory(
                    barrierRepository = appContainer.barrierRepository,
                    guardianRepository = appContainer.guardianRepository,
                    sessionPreferences = appContainer.sessionPreferences,
                )
            )
            GuardianB2GScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onOpenDetail = { barrierId ->
                    navController.navigate(AppRoutes.detail(barrierId))
                },
                onLoggedOut = {
                    pendingRadarSnackbar = "Sessão encerrada. Faça login para alternar o perfil."
                    navController.popBackStack()
                },
            )
        }
    }
}

