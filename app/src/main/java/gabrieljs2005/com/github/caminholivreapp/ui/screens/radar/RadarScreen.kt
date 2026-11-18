package gabrieljs2005.com.github.caminholivreapp.ui.screens.radar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.R
import gabrieljs2005.com.github.caminholivreapp.ui.components.BarrierCard
import gabrieljs2005.com.github.caminholivreapp.ui.components.EmptyFilterStateWidget
import gabrieljs2005.com.github.caminholivreapp.ui.components.ErrorStateWidget
import gabrieljs2005.com.github.caminholivreapp.ui.components.InitialStateWidget
import gabrieljs2005.com.github.caminholivreapp.ui.components.LoadingStateWidget
import gabrieljs2005.com.github.caminholivreapp.ui.components.ProfileSelectorBar
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RadarScreen(
    viewModel: RadarViewModel,
    externalSnackbarMessage: String?,
    onConsumeExternalSnackbar: () -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenNewReport: () -> Unit,
    onOpenGuardianB2G: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showContextCard by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.consumeSnackbarMessage()
        }
    }

    LaunchedEffect(externalSnackbarMessage) {
        if (!externalSnackbarMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(externalSnackbarMessage)
            onConsumeExternalSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(2.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_caminho_livre),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Caminho Livre • Radar",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleOnlyMonitored() }) {
                        Icon(
                            imageVector = if (uiState.onlyMonitored) {
                                Icons.Filled.NotificationsActive
                            } else {
                                Icons.Filled.NotificationsNone
                            },
                            contentDescription = if (uiState.onlyMonitored) {
                                "Mostrar todas as barreiras"
                            } else {
                                "Filtrar apenas barreiras monitoradas"
                            },
                            tint = if (uiState.onlyMonitored) Color(0xFFFFD54F) else Color.White,
                        )
                    }
                    IconButton(onClick = onOpenGuardianB2G) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = "Painel do Guardião (${uiState.guardianXp} XP) e Fila B2G",
                            tint = Color.White,
                        )
                    }
                    IconButton(onClick = { viewModel.scanUrbanMesh() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Sincronizar novamente com a API Caminho Livre",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryNavy,
                    titleContentColor = Color.White,
                ),
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenNewReport,
                containerColor = SecondaryTeal,
                contentColor = Color.White,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.AddLocationAlt,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(
                        text = "Relatar em 3 Toques (+50 XP)",
                        fontWeight = FontWeight.ExtraBold,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // Faixa de contextualização CitizenTech (Critério 1 da rubrica)
            CitizenTechContextBanner(
                expanded = showContextCard,
                onToggleExpanded = { showContextCard = !showContextCard },
            )

            // Barra superior de seleção dinâmica de Perfil Biomecânico
            ProfileSelectorBar(
                selectedProfile = uiState.activeProfile,
                onProfileSelected = { viewModel.selectProfile(it) },
            )

            // Barra de busca e filtros rápidos quando já existem barreiras no Room
            if (uiState.hasBarriersInDb) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = {
                            Text("Buscar por rua, bairro ou tipo de barreira...")
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Limpar busca",
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        FilterChip(
                            selected = uiState.onlyBlockingForProfile,
                            onClick = {
                                viewModel.setOnlyBlockingForProfile(!uiState.onlyBlockingForProfile)
                            },
                            label = {
                                Text("Bloqueio Físico (${uiState.blockingCount})")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Block,
                                    contentDescription = null,
                                    tint = if (uiState.onlyBlockingForProfile) Color.White else CriticalRed,
                                    modifier = Modifier.size(16.dp),
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryNavy,
                                selectedLabelColor = Color.White,
                            ),
                        )

                        FilterChip(
                            selected = uiState.onlyMonitored,
                            onClick = {
                                viewModel.setOnlyMonitored(!uiState.onlyMonitored)
                            },
                            label = {
                                Text("Monitoradas (${uiState.monitoredCount})")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.NotificationsActive,
                                    contentDescription = null,
                                    tint = if (uiState.onlyMonitored) Color.White else AccentAmber,
                                    modifier = Modifier.size(16.dp),
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryNavy,
                                selectedLabelColor = Color.White,
                            ),
                        )

                        AssistChip(
                            onClick = onOpenGuardianB2G,
                            label = {
                                Text(
                                    text = "${uiState.guardianXp} XP • Fila B2G",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = null,
                                    tint = SecondaryTeal,
                                    modifier = Modifier.size(16.dp),
                                )
                            },
                        )
                    }
                }
                HorizontalDivider(color = BorderGray, thickness = 1.dp)
            }

            // Corpo principal gerenciando os estados (Inicial, Carregando, Erro, Lista, Vazio)
            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.syncStatus == SyncStatus.LOADING && !uiState.hasBarriersInDb -> {
                        LoadingStateWidget(
                            message = "Sincronizando malha de calçadas e barreiras...",
                        )
                    }
                    uiState.syncStatus == SyncStatus.ERROR && !uiState.hasBarriersInDb -> {
                        ErrorStateWidget(
                            errorMessage = uiState.errorMessage,
                            onRetry = { viewModel.scanUrbanMesh() },
                        )
                    }
                    !uiState.hasBarriersInDb -> {
                        InitialStateWidget(
                            onStartScan = { viewModel.scanUrbanMesh() },
                        )
                    }
                    uiState.visibleBarriers.isEmpty() -> {
                        EmptyFilterStateWidget(
                            onClearFilters = { viewModel.clearFilters() },
                        )
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(top = 6.dp, bottom = 92.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(
                                items = uiState.visibleBarriers,
                                key = { it.id },
                            ) { barrier ->
                                BarrierCard(
                                    barrier = barrier,
                                    activeProfile = uiState.activeProfile,
                                    onClick = { onOpenDetail(barrier.id) },
                                    onToggleMonitor = { viewModel.toggleMonitorBarrier(barrier.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CitizenTechContextBanner(
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFF8FF))
            .clickable(onClick = onToggleExpanded)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Filled.Public,
                contentDescription = null,
                tint = PrimaryNavy,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "CitizenTech • ODS 11, 10, 9 e 3 • Toque para ver problema e público",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = PrimaryNavy,
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 6.dp)) {
                Text(
                    text = "Objetivo: Mapear barreiras em calçadas, calcular rotas adaptadas por perfil biomecânico e priorizar obras municipais (Score B2G).",
                    fontSize = 12.sp,
                    color = HighContrastText,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Público Beneficiado: Cadeirantes, pessoas com deficiência visual, idosos, famílias com carrinho de bebê e gestores públicos.",
                    fontSize = 12.sp,
                    color = SecondaryText,
                )
            }
        }
    }
    HorizontalDivider(color = BorderGray, thickness = 1.dp)
}

