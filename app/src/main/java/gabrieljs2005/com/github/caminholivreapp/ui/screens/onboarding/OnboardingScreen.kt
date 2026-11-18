package gabrieljs2005.com.github.caminholivreapp.ui.screens.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.R
import gabrieljs2005.com.github.caminholivreapp.domain.model.OnboardingPage
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinishOnboarding: () -> Unit,
) {
    val pages = viewModel.pages
    val dontShowAgain by viewModel.dontShowAgain.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    val isLastPage = pagerState.currentPage == pages.lastIndex

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bem-vindo ao Caminho Livre",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.persistPreferenceOnFinish()
                            onFinishOnboarding()
                        },
                    ) {
                        Text(
                            text = "Pular",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryNavy,
                    titleContentColor = Color.White,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { pageIndex ->
                OnboardingPageContent(page = pages[pageIndex])
            }

            // Indicador visual de página (3 pontos)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                pages.indices.forEach { index ->
                    val active = index == pagerState.currentPage
                    val dotWidth by animateDpAsState(
                        targetValue = if (active) 28.dp else 10.dp,
                        label = "dotWidth",
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .width(dotWidth)
                            .height(10.dp)
                            .background(
                                color = if (active) PrimaryNavy else MutedGray,
                                shape = RoundedCornerShape(8.dp),
                            ),
                    )
                }
            }

            HorizontalDivider(color = BorderGray, thickness = 1.2.dp)

            // Rodapé com opção "Não mostrar novamente" + Botões Voltar e Avançar/Concluir
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setDontShowAgain(!dontShowAgain) }
                        .padding(vertical = 4.dp),
                ) {
                    Checkbox(
                        checked = dontShowAgain,
                        onCheckedChange = { viewModel.setDontShowAgain(it) },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryNavy),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Não mostrar novamente",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighContrastText,
                        )
                        Text(
                            text = "Salva sua preferência no dispositivo.",
                            fontSize = 12.5.sp,
                            color = SecondaryText,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = {
                            if (pagerState.currentPage > 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        },
                        enabled = pagerState.currentPage > 0,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Voltar", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (!isLastPage) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                viewModel.persistPreferenceOnFinish()
                                onFinishOnboarding()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryNavy,
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                    ) {
                        Icon(
                            imageVector = if (isLastPage) {
                                Icons.Filled.CheckCircleOutline
                            } else {
                                Icons.AutoMirrored.Filled.ArrowForward
                            },
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isLastPage) "Concluir" else "Avançar",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(118.dp)
                    .background(
                        color = page.accentColor.copy(alpha = 0.12f),
                        shape = CircleShape,
                    )
                    .border(
                        width = 2.2.dp,
                        color = page.accentColor,
                        shape = CircleShape,
                    )
                    .padding(14.dp)
                    .clip(CircleShape),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_caminho_livre),
                    contentDescription = "Logo Caminho Livre",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .background(page.accentColor, CircleShape)
                    .border(2.5.dp, Color.White, CircleShape),
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .background(
                    color = page.accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp),
                )
                .border(
                    width = 1.2.dp,
                    color = page.accentColor,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(horizontal = 14.dp, vertical = 6.dp),
        ) {
            Text(
                text = page.badgeText,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = page.accentColor,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.title,
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HighContrastText,
            lineHeight = 28.sp,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = page.description,
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            color = SecondaryText,
            lineHeight = 22.sp,
        )

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.2.dp, BorderGray),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Public,
                    contentDescription = null,
                    tint = page.accentColor,
                    modifier = Modifier.size(26.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = page.odsHighlight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = HighContrastText,
                    lineHeight = 18.sp,
                )
            }
        }
    }
}

