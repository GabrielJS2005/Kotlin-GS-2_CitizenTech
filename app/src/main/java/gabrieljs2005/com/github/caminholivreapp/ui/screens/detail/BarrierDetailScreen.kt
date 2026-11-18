package gabrieljs2005.com.github.caminholivreapp.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonPinCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.domain.model.MobilityProfile
import gabrieljs2005.com.github.caminholivreapp.domain.rules.ImpactCalculator
import gabrieljs2005.com.github.caminholivreapp.ui.components.BarrierImage
import gabrieljs2005.com.github.caminholivreapp.ui.components.PriorityScoreBadge
import gabrieljs2005.com.github.caminholivreapp.ui.components.SeverityIndicator
import gabrieljs2005.com.github.caminholivreapp.ui.components.XpFeedbackBanner
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BarrierDetailScreen(
    viewModel: BarrierDetailViewModel,
    onNavigateBack: () -> Unit,
    onEditReport: (String) -> Unit,
    onDeletedSuccess: () -> Unit,
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

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onDeletedSuccess()
        }
    }

    val barrier = uiState.barrier

    if (uiState.showDeleteDialog && barrier != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = {
                Text(
                    text = "Excluir Relato Comunitário?",
                    fontWeight = FontWeight.ExtraBold,
                )
            },
            text = {
                Text(
                    text = "Tem certeza de que deseja remover permanentemente o relato \"${barrier.title}\" do banco de dados local (Room)?",
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDelete() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CriticalRed,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = "Excluir", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text(text = "Cancelar", fontWeight = FontWeight.Bold)
                }
            },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                        )
                    }
                },
                title = {
                    Text(
                        text = "Auditoria & Detalhes",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                },
                actions = {
                    if (barrier != null) {
                        IconButton(onClick = { viewModel.toggleMonitor() }) {
                            Icon(
                                imageVector = if (barrier.isMonitored) {
                                    Icons.Filled.NotificationsActive
                                } else {
                                    Icons.Filled.NotificationsNone
                                },
                                contentDescription = if (barrier.isMonitored) {
                                    "Monitorando reparo municipal"
                                } else {
                                    "Monitorar reparo municipal"
                                },
                                tint = if (barrier.isMonitored) Color(0xFFFFD54F) else Color.White,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryNavy,
                    titleContentColor = Color.White,
                ),
            )
        },
    ) { innerPadding ->
        if (barrier == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                CircularProgressIndicator(color = PrimaryNavy)
            }
            return@Scaffold
        }

        val impactResult = ImpactCalculator.evaluate(
            category = barrier.category,
            profile = uiState.selectedProfile,
            severity = barrier.severity,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // Card principal com foto anonimizada + dados da barreira
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.2.dp, BorderGray),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    BarrierImage(
                        imageUrl = barrier.imageUrl,
                        category = barrier.category,
                        applyLgpdBlur = uiState.applyLgpdBlur,
                        onToggleBlur = { viewModel.toggleLgpdBlur() },
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(
                                        color = barrier.category.color.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                    .border(
                                        width = 1.2.dp,
                                        color = barrier.category.color,
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                            ) {
                                Icon(
                                    imageVector = barrier.category.icon,
                                    contentDescription = null,
                                    tint = barrier.category.color,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = barrier.category.label,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = barrier.category.color,
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(
                                        color = barrier.status.color.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                            ) {
                                Icon(
                                    imageVector = barrier.status.icon,
                                    contentDescription = null,
                                    tint = barrier.status.color,
                                    modifier = Modifier.size(15.dp),
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Status B2G: ${barrier.status.label}",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = barrier.status.color,
                                )
                            }

                            if (barrier.isUserReported) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(
                                            color = SecondaryTeal.copy(alpha = 0.14f),
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PersonPinCircle,
                                        contentDescription = null,
                                        tint = SecondaryTeal,
                                        modifier = Modifier.size(15.dp),
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Relatado por Você",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SecondaryTeal,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = barrier.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = HighContrastText,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = null,
                                tint = SecondaryText,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = barrier.address,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SecondaryText,
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = barrier.proximityDescription,
                            fontSize = 12.5.sp,
                            color = SecondaryText,
                        )

                        if (barrier.observation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFF2F4F7),
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                    .padding(10.dp),
                            ) {
                                Text(
                                    text = "Observação do relato: ${barrier.observation}",
                                    fontSize = 13.sp,
                                    color = HighContrastText,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        SeverityIndicator(severity = barrier.severity)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.sessionXpEarned > 0) {
                XpFeedbackBanner(
                    xpGained = uiState.sessionXpEarned,
                    title = "Auditoria Comunitária Confirmada!",
                    subtitle = "O consenso comunitário foi salvo no Room e o Score B2G da prefeitura subiu.",
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Seção de Impacto no Cálculo de Rota (Custo_Trecho) por Perfil Biomecânico
            val impactColor = if (impactResult.isPhysicalBlock) CriticalRed else PrimaryNavy
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.8.dp, impactColor),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = uiState.selectedProfile.icon,
                            contentDescription = null,
                            tint = impactColor,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Impacto Biomecânico: ${uiState.selectedProfile.label}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HighContrastText,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = impactColor.copy(
                                    alpha = if (impactResult.isPhysicalBlock) 0.12f else 0.08f
                                ),
                                shape = RoundedCornerShape(10.dp),
                            )
                            .padding(12.dp),
                    ) {
                        Text(
                            text = impactResult.summaryLabel,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            color = impactColor,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = impactResult.routeCostDisplay,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighContrastText,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = impactResult.technicalExplanation,
                            fontSize = 13.sp,
                            color = SecondaryText,
                            lineHeight = 18.sp,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Simular impacto em outro perfil biomecânico:",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                    ) {
                        MobilityProfile.entries.forEach { profile ->
                            val selected = profile == uiState.selectedProfile
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.selectSimulatedProfile(profile) },
                                label = { Text(profile.label) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = profile.icon,
                                        contentDescription = null,
                                        tint = if (selected) Color.White else PrimaryNavy,
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seção de Validação Comunitária ("Guardiões da Calçada")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.2.dp, BorderGray),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.VerifiedUser,
                            contentDescription = null,
                            tint = SecondaryTeal,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Validação Comunitária (\"Guardiões da Calçada\")",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HighContrastText,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    PriorityScoreBadge(score = barrier.priorityScore)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Confirmações registradas: ${barrier.confirmations} auditoria(s)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryTeal,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Você passou por este local recentemente? Confirme se o obstáculo permanece ativo para atualizar a priorização de zeladoria urbana da prefeitura no banco local (Room) e ganhar +15 XP.",
                        fontSize = 13.5.sp,
                        color = SecondaryText,
                        lineHeight = 19.sp,
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.confirmStillThere() },
                        enabled = !uiState.hasValidatedInSession,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryNavy,
                            contentColor = Color.White,
                            disabledContainerColor = SecondaryTeal,
                            disabledContentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                    ) {
                        Icon(
                            imageVector = if (uiState.hasValidatedInSession) {
                                Icons.Filled.Verified
                            } else {
                                Icons.Filled.ThumbUpOffAlt
                            },
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.hasValidatedInSession) {
                                "Confirmado nesta sessão (+15 XP ganhos)"
                            } else {
                                "Ainda está lá? (+15 XP • Validar Barreira)"
                            },
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Ações adicionais de Edição e Exclusão para relatos criados pelo usuário
            if (barrier.isUserReported) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = BorderStroke(1.5.dp, SecondaryTeal),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Gerenciar Meu Relato (Persistência Room)",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HighContrastText,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Este registro foi cadastrado por você. Você pode editar os dados ou excluí-lo do banco local.",
                            fontSize = 13.sp,
                            color = SecondaryText,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { onEditReport(barrier.id) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                            ) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Editar", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            OutlinedButton(
                                onClick = { viewModel.requestDeleteConfirmation() },
                                border = BorderStroke(1.5.dp, CriticalRed),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CriticalRed),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                            ) {
                                Icon(imageVector = Icons.Filled.DeleteOutline, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Excluir", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

