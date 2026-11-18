package gabrieljs2005.com.github.caminholivreapp.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.R
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToRadar: () -> Unit,
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Loading -> Unit
            SplashDestination.ToOnboarding -> onNavigateToOnboarding()
            SplashDestination.ToRadar -> onNavigateToRadar()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryNavy)
            .padding(horizontal = 32.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(136.dp)
                    .shadow(elevation = 16.dp, shape = CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(12.dp)
                    .clip(CircleShape),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_caminho_livre),
                    contentDescription = "Logo Caminho Livre",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = AppConstants.APP_NAME,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = AppConstants.APP_TAGLINE,
                textAlign = TextAlign.Center,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE0EAFF),
                lineHeight = 20.sp,
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                strokeWidth = 3.2.dp,
                color = Color.White,
                modifier = Modifier.size(34.dp),
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Verificando preferências de acessibilidade...",
                fontSize = 13.sp,
                color = BorderGray,
            )
        }
    }
}

