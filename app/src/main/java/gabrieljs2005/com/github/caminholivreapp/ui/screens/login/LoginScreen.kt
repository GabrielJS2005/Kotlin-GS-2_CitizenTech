package gabrieljs2005.com.github.caminholivreapp.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabrieljs2005.com.github.caminholivreapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateBack: () -> Unit,
    onAuthenticated: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.authenticatedUser) {
        if (uiState.authenticatedUser != null) {
            onAuthenticated()
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
                        text = "Acesso ao Painel • Controle de Perfil",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = PrimaryNavy.copy(alpha = 0.1f),
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    imageVector = Icons.Filled.AdminPanelSettings,
                    contentDescription = null,
                    tint = PrimaryNavy,
                    modifier = Modifier.size(46.dp),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Autenticação de Acesso ao Painel",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = HighContrastText,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Por regras de governança municipal, a \"Fila de Obras B2G\" é restrita a gestores públicos credenciados. Usuários comuns acessam a visão Guardião Scout.",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                color = SecondaryText,
                lineHeight = 18.sp,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Card de sessão existente (se houver)
            val existingUser = uiState.existingUser
            if (existingUser != null) {
                val isGestor = existingUser.role.isGestor
                val accent = if (isGestor) ManagerGreen else PrimaryNavy
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isGestor) Color(0xFFE8F5E9) else Color(0xFFE3F2FD),
                    ),
                    border = BorderStroke(1.5.dp, accent),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isGestor) Icons.Filled.VerifiedUser else Icons.Filled.Person,
                                contentDescription = null,
                                tint = accent,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sessão Ativa: ${existingUser.name}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.5.sp,
                                color = HighContrastText,
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Perfil: ${existingUser.role.label} (${existingUser.department})",
                            fontSize = 12.5.sp,
                            color = SecondaryText,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.continueWithExistingSession() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent,
                                contentColor = Color.White,
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Continuar com Esta Conta",
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGray)
                    Text(
                        text = "Ou entre com outro perfil",
                        fontSize = 12.sp,
                        color = SecondaryText,
                        modifier = Modifier.padding(horizontal = 10.dp),
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGray)
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Mensagem de erro de credenciais
            val credentialError = uiState.credentialError
            if (credentialError != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = CriticalRed.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .border(
                            width = 1.2.dp,
                            color = CriticalRed,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .padding(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        tint = CriticalRed,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = credentialError,
                        color = CriticalRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Campo de Login / E-mail
            OutlinedTextField(
                value = uiState.login,
                onValueChange = { viewModel.updateLogin(it) },
                label = { Text("E-mail ou Usuário") },
                placeholder = { Text("cidadao@caminholivre.com.br ou gestor@sp.gov.br") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                },
                isError = uiState.loginError != null,
                supportingText = {
                    uiState.loginError?.let {
                        Text(text = it, color = CriticalRed, fontWeight = FontWeight.Bold)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Senha
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Senha") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleObscurePassword() }) {
                        Icon(
                            imageVector = if (uiState.obscurePassword) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = "Alternar visibilidade da senha",
                        )
                    }
                },
                visualTransformation = if (uiState.obscurePassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let {
                        Text(text = it, color = CriticalRed, fontWeight = FontWeight.Bold)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { viewModel.submitLogin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryNavy,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Entrar no Painel", fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seção de Atalhos Rápidos para Teste
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
                            imageVector = Icons.Filled.Science,
                            contentDescription = null,
                            tint = AccentAmber,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Atalhos de Teste para Avaliação:",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = HighContrastText,
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Toque em um dos botões abaixo para preencher automaticamente as credenciais:",
                        fontSize = 12.5.sp,
                        color = SecondaryText,
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { viewModel.fillCitizenProfile() },
                        border = BorderStroke(1.8.dp, PrimaryNavy),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Preencher Cidadão Comum (Scout)",
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        text = "Login: cidadao@caminholivre.com.br • Senha: 123456\n(Visão: apenas Guardião Scout / XP)",
                        fontSize = 11.5.sp,
                        color = SecondaryText,
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 8.dp),
                    )

                    HorizontalDivider(color = BorderGray, modifier = Modifier.padding(vertical = 8.dp))

                    OutlinedButton(
                        onClick = { viewModel.fillManagerProfile() },
                        border = BorderStroke(1.8.dp, ManagerGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ManagerGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Icon(imageVector = Icons.Filled.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Preencher Gestor Público B2G",
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        text = "Login: gestor@sp.gov.br • Senha: gestor123\n(Visão: Fila de Obras B2G + Guardião Scout)",
                        fontSize = 11.5.sp,
                        color = SecondaryText,
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                    )
                }
            }
        }
    }
}

