package gabrieljs2005.com.github.caminholivreapp.ui.screens.report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.EditLocationAlt
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Signpost
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.rules.AppConstants
import gabrieljs2005.com.github.caminholivreapp.domain.rules.ReportValidator
import gabrieljs2005.com.github.caminholivreapp.ui.components.PriorityScoreBadge
import gabrieljs2005.com.github.caminholivreapp.ui.components.SeverityIndicator
import gabrieljs2005.com.github.caminholivreapp.ui.components.XpFeedbackBanner
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFormScreen(
    viewModel: ReportFormViewModel,
    onNavigateBack: () -> Unit,
    onSavedSuccess: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.completedMessage) {
        val message = uiState.completedMessage
        if (message != null) {
            onSavedSuccess(message)
        }
    }

    Scaffold(
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
                        text = if (uiState.isEditMode) {
                            "Editar Relato (Guardião)"
                        } else {
                            "Relato em 3 Toques (Guardião)"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            if (!uiState.isEditMode) {
                XpFeedbackBanner(
                    xpGained = AppConstants.XP_PER_NEW_REPORT,
                    title = "Missão Guardiões da Calçada (+50 XP)",
                    subtitle = "Fotos têm rostos e placas borrados localmente (On-Device LGPD) e o registro é salvo no Room.",
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // TOQUE 1: Categoria da Barreira (Grid de 2 colunas)
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
                    Text(
                        text = "Toque 1 • Categoria da Barreira (Pré-classificada IA)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HighContrastText,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Selecione o tipo de patologia urbana identificada na calçada:",
                        fontSize = 13.sp,
                        color = SecondaryText,
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val categories = BarrierCategory.entries
                    val rows = categories.chunked(2)
                    rows.forEachIndexed { rowIdx, rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            rowItems.forEach { category ->
                                val selected = uiState.selectedCategory == category
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp)
                                        .background(
                                            color = if (selected) {
                                                category.color.copy(alpha = 0.14f)
                                            } else {
                                                Color.White
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                        .border(
                                            width = if (selected) 2.2.dp else 1.2.dp,
                                            color = if (selected) category.color else BorderGray,
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                        .clickable { viewModel.selectCategory(category) }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                ) {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = null,
                                        tint = category.color,
                                        modifier = Modifier.size(22.dp),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = category.label,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 12.5.sp,
                                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                        color = HighContrastText,
                                    )
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        if (rowIdx < rows.lastIndex) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TOQUE 2: Nível de Gravidade (1 a 5)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Toque 2 • Gravidade Técnica (1 a 5)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HighContrastText,
                            modifier = Modifier.weight(1f),
                        )
                        SeverityIndicator(severity = uiState.selectedSeverity)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        (1..5).forEach { level ->
                            val selected = uiState.selectedSeverity == level
                            OutlinedButton(
                                onClick = { viewModel.selectSeverity(level) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selected) PrimaryNavy else Color.White,
                                    contentColor = if (selected) Color.White else PrimaryNavy,
                                ),
                                border = BorderStroke(1.8.dp, PrimaryNavy),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                            ) {
                                Text(
                                    text = "$level",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TOQUE 3: Proximidade + Endereço Obrigatório + Observação Opcional
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
                    Text(
                        text = "Toque 3 • Proximidade & Localização",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HighContrastText,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Define o multiplicador espacial para priorização de obras B2G:",
                        fontSize = 13.sp,
                        color = SecondaryText,
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ProximityOptionTile(
                        title = "Próximo a UBS / Hospital (< 100m)",
                        subtitle = "Multiplicador máximo de urgência: 2,5x",
                        icon = Icons.Filled.LocalHospital,
                        factor = AppConstants.PROXIMITY_UBS_HOSPITAL,
                        selectedFactor = uiState.selectedProximityFactor,
                        onSelect = { viewModel.selectProximityFactor(it) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProximityOptionTile(
                        title = "Próximo a Escola / Parada de Ônibus (< 200m)",
                        subtitle = "Multiplicador prioritário: 2,0x",
                        icon = Icons.Filled.School,
                        factor = AppConstants.PROXIMITY_SCHOOL_TRANSPORT,
                        selectedFactor = uiState.selectedProximityFactor,
                        onSelect = { viewModel.selectProximityFactor(it) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProximityOptionTile(
                        title = "Rua Residencial / Comercial Comum",
                        subtitle = "Multiplicador base padrão: 1,0x",
                        icon = Icons.Filled.Signpost,
                        factor = AppConstants.PROXIMITY_REGULAR_STREET,
                        selectedFactor = uiState.selectedProximityFactor,
                        onSelect = { viewModel.selectProximityFactor(it) },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Endereço Obrigatório (mín. ${ReportValidator.MIN_ADDRESS_LENGTH} caracteres) *",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HighContrastText,
                            modifier = Modifier.weight(1f),
                        )
                        TextButton(onClick = { viewModel.fillSampleAddress() }) {
                            Icon(
                                imageVector = Icons.Filled.EditLocationAlt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Exemplo SP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    val addressErr = uiState.addressError
                    OutlinedTextField(
                        value = uiState.address,
                        onValueChange = { viewModel.updateAddress(it) },
                        label = {
                            Text("Endereço / Ponto de Referência GPS *")
                        },
                        placeholder = {
                            Text("Ex.: Av. Paulista, 1106 - Bela Vista, São Paulo/SP")
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.MyLocation, contentDescription = null)
                        },
                        isError = addressErr != null,
                        supportingText = {
                            if (addressErr != null) {
                                Text(
                                    text = addressErr,
                                    color = CriticalRed,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else {
                                Text(
                                    text = "Campo validado (${uiState.address.trim().length} caracteres).",
                                    color = SecondaryText,
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val obsErr = uiState.observationError
                    OutlinedTextField(
                        value = uiState.observation,
                        onValueChange = { viewModel.updateObservation(it) },
                        label = {
                            Text("Observação Complementar (Opcional)")
                        },
                        placeholder = {
                            Text("Ex.: Guia com degrau de 18cm em frente à entrada principal")
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = null)
                        },
                        isError = obsErr != null,
                        supportingText = {
                            val counter = "${uiState.observation.length}/${ReportValidator.MAX_OBSERVATION_LENGTH}"
                            if (obsErr != null) {
                                Text(
                                    text = "$obsErr ($counter)",
                                    color = CriticalRed,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else {
                                Text(
                                    text = "Máximo de ${ReportValidator.MAX_OBSERVATION_LENGTH} caracteres ($counter).",
                                    color = SecondaryText,
                                )
                            }
                        },
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pré-visualização do Score B2G e botão Salvar
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
                    Text(
                        text = "Prévia de Impacto no Painel B2G da Prefeitura:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HighContrastText,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PriorityScoreBadge(score = uiState.previewScore)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.submit() },
                        enabled = uiState.isFormValid && !uiState.isSubmitting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryNavy,
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.isEditMode) {
                                "Salvar Alterações no Room"
                            } else {
                                "Confirmar Relato em 3 Toques (+50 XP)"
                            },
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProximityOptionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    factor: Double,
    selectedFactor: Double,
    onSelect: (Double) -> Unit,
) {
    val isSelected = ReportFormViewModel.Companion.isSameFactor(factor, selectedFactor)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) PrimaryNavy.copy(alpha = 0.08f) else Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .border(
                width = if (isSelected) 2.dp else 1.2.dp,
                color = if (isSelected) PrimaryNavy else BorderGray,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable { onSelect(factor) }
            .padding(12.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) PrimaryNavy else SecondaryText,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HighContrastText,
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = SecondaryText,
            )
        }
        Icon(
            imageVector = if (isSelected) {
                Icons.Filled.RadioButtonChecked
            } else {
                Icons.Filled.RadioButtonUnchecked
            },
            contentDescription = null,
            tint = if (isSelected) PrimaryNavy else MutedGray,
        )
    }
}

