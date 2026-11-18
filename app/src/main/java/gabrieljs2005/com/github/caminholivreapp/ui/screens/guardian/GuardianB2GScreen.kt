package gabrieljs2005.com.github.caminholivreapp.ui.screens.guardian

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.data.local.BarrierEntity
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus
import gabrieljs2005.com.github.caminholivreapp.domain.model.GuardianProgress
import gabrieljs2005.com.github.caminholivreapp.ui.components.BadgeTile
import gabrieljs2005.com.github.caminholivreapp.ui.components.PriorityScoreBadge
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianB2GScreen(
    viewModel: GuardianB2GViewModel,
    onNavigateBack: () -> Unit,
    onOpenDetail: (String) -> Unit,
    onLoggedOut: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        val msg = uiState.snackbarMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.consumeSnackbar()
        }
    }

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) {
            onLoggedOut()
        }
    }

    val isGestor = uiState.isGestor

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar ao Radar",
                                tint = Color.White,
                            )
                        }
                    },
                    title = {
                        Text(
                            text = if (isGestor) {
                                "Painel B2G • Gestão Pública"
                            } else {
                                "Painel Guardião Scout"
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    color = if (isGestor) ManagerGreen else Color.White.copy(alpha = 0.18f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .border(1.2.dp, Color.White, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Icon(
                                imageVector = if (isGestor) {
                                    Icons.Filled.AdminPanelSettings
                                } else {
                                    Icons.Filled.Person
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isGestor) "Gestor B2G" else "Cidadão Scout",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                            )
                        }
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Trocar Usuário / Sair",
                                tint = Color.White,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryNavy,
                        titleContentColor = Color.White,
                    ),
                )

                if (isGestor) {
                    PrimaryTabRow(
                        selectedTabIndex = uiState.selectedTabIndex,
                        containerColor = PrimaryNavy,
                        contentColor = Color.White,
                    ) {
                        Tab(
                            selected = uiState.selectedTabIndex == 0,
                            onClick = { viewModel.selectTab(0) },
                            text = {
                                Text(
                                    text = "Guardião Scout (XP)",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                            },
                            icon = {
                                Icon(imageVector = Icons.Filled.Shield, contentDescription = null)
                            },
                        )
                        Tab(
                            selected = uiState.selectedTabIndex == 1,
                            onClick = { viewModel.selectTab(1) },
                            text = {
                                Text(
                                    text = "Fila de Obras B2G",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                            },
                            icon = {
                                Icon(imageVector = Icons.Filled.AccountBalance, contentDescription = null)
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (!isGestor) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFF8FF))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = PrimaryNavy,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Visão Cidadão Comum: Acesso focado em auditorias, XP e insígnias. A \"Fila de Obras B2G\" é de acesso restrito a gestores públicos.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryNavy,
                    )
                }
                GuardianProgressTab(progress = uiState.progress)
            } else {
                if (uiState.selectedTabIndex == 0) {
                    GuardianProgressTab(progress = uiState.progress)
                } else {
                    PublicManagerB2GTab(
                        allBarriers = uiState.allBarriers,
                        rankedList = uiState.rankedBarriers,
                        selectedStatusFilter = uiState.statusFilter,
                        onSelectStatusFilter = { viewModel.selectStatusFilter(it) },
                        onOpenDetail = onOpenDetail,
                        onCycleStatus = { viewModel.cycleBarrierStatus(it) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GuardianProgressTab(
    progress: GuardianProgress,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Card de nível e XP
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(58.dp)
                            .background(Color.White.copy(alpha = 0.16f), CircleShape),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nível ${progress.level} • ${progress.rankTitle}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${progress.totalXp} XP acumulados em auditorias comunitárias (Room)",
                            fontSize = 13.5.sp,
                            color = Color(0xFFE0EAFF),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress.levelProgress },
                    color = Color(0xFF00E5B8),
                    trackColor = Color.White.copy(alpha = 0.22f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Progresso para o Nível ${progress.level + 1}: ${progress.xpIntoCurrentLevel}/${progress.xpForNextLevel} XP",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Contador de Empatia Tangível
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = BorderStroke(1.2.dp, BorderGray),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = SecondaryTeal.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = SecondaryTeal,
                        modifier = Modifier.size(26.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Contador de Empatia Tangível",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HighContrastText,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Suas auditorias protegeram cerca de ${progress.protectedCitizensEstimate} cadeirantes, idosos e pessoas com deficiência visual nesta semana.",
                        fontSize = 13.sp,
                        color = SecondaryText,
                        lineHeight = 18.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Insígnias de Impacto Social",
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HighContrastText,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Valide barreiras (\"Ainda está lá?\") ou registre relatos em 3 toques para desbloquear:",
            fontSize = 13.sp,
            color = SecondaryText,
        )
        Spacer(modifier = Modifier.height(12.dp))

        val rows = progress.badges.chunked(2)
        rows.forEachIndexed { idx, pair ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                pair.forEach { badge ->
                    BadgeTile(
                        badge = badge,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (idx < rows.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PublicManagerB2GTab(
    allBarriers: List<BarrierEntity>,
    rankedList: List<BarrierEntity>,
    selectedStatusFilter: BarrierStatus?,
    onSelectStatusFilter: (BarrierStatus?) -> Unit,
    onOpenDetail: (String) -> Unit,
    onCycleStatus: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = "Fila de Obras Ordenada por Score Matemático B2G:",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HighContrastText,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Score = Gravidade × (1 + Confirmações) × Fator_Proximidade × Decaimento_Idade",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = SecondaryText,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                FilterChip(
                    selected = selectedStatusFilter == null,
                    onClick = { onSelectStatusFilter(null) },
                    label = { Text("Todos (${allBarriers.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryNavy,
                        selectedLabelColor = Color.White,
                    ),
                    modifier = Modifier.padding(end = 8.dp),
                )

                BarrierStatus.entries.forEach { status ->
                    val count = allBarriers.count { it.status == status }
                    val selected = selectedStatusFilter == status
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectStatusFilter(status) },
                        label = { Text("${status.label} ($count)") },
                        leadingIcon = {
                            Icon(
                                imageVector = status.icon,
                                contentDescription = null,
                                tint = if (selected) Color.White else status.color,
                                modifier = Modifier.size(16.dp),
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryNavy,
                            selectedLabelColor = Color.White,
                        ),
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
        }

        HorizontalDivider(color = BorderGray, thickness = 1.2.dp)

        if (rankedList.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(
                    text = "Nenhuma barreira encontrada no status selecionado.",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryText,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(
                    items = rankedList,
                    key = { _, item -> item.id },
                ) { index, item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = BorderStroke(1.2.dp, BorderGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { onOpenDetail(item.id) },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = PrimaryNavy,
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                ) {
                                    Text(
                                        text = "#${index + 1} Prioridade",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                PriorityScoreBadge(
                                    score = item.priorityScore,
                                    compact = true,
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = item.title,
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = HighContrastText,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${item.address} • ${item.proximityDescription}",
                                fontSize = 12.5.sp,
                                color = SecondaryText,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(
                                            color = item.status.color.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                ) {
                                    Icon(
                                        imageVector = item.status.icon,
                                        contentDescription = null,
                                        tint = item.status.color,
                                        modifier = Modifier.size(15.dp),
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = item.status.label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = item.status.color,
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                OutlinedButton(
                                    onClick = { onCycleStatus(item.id) },
                                    shape = RoundedCornerShape(10.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.SyncAlt,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Avançar Status B2G",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

