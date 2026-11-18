# Roteiro de Capturas de Tela (Evidências Obrigatórias — Critério 7)

A rubrica da Global Solution exige **capturas de tela reais do aplicativo em execução**, comprovando:
1. O fluxo principal (contextualização, navegação e listagem);
2. **A criação de um registro** (incluindo a validação de campo obrigatório);
3. **A ação adicional implementada** (validar *"Ainda está lá?"*, favoritar/monitorar, editar/excluir ou avançar status B2G);
4. **A permanência dos dados após fechar e abrir novamente o aplicativo (Room)**.

Salve as capturas na pasta `docs/screenshots/` com os nomes abaixo (já referenciados automaticamente pelo `README.md` da raiz):

| Arquivo | O que capturar no Emulador Android Studio |
| :--- | :--- |
| `01_splash.png` | Tela de abertura (`SplashScreen`) exibindo o logo e nome do aplicativo. |
| `02_onboarding.png` | Uma das 3 páginas do `OnboardingScreen` mostrando a conexão com os ODS e o checkbox *"Não mostrar novamente"*. |
| `03_radar_inicial.png` | `RadarScreen` no primeiro uso (antes de sincronizar), exibindo o botão *"Escanear Malha Urbana (São Paulo - SP)"*. |
| `04_radar_lista.png` | `RadarScreen` após tocar em *"Escanear Malha Urbana"*, mostrando os cards de barreiras, seletor de Perfil Biomecânico e filtros. |
| `05_relato_validacao_erro.png` | `ReportFormScreen` com o campo **Endereço** vazio ou curto (`< 10` caracteres), destacando a mensagem de erro em vermelho e o botão de envio desabilitado. |
| `06_relato_preenchido.png` | `ReportFormScreen` com Categoria, Gravidade, Proximidade, Endereço válido e Observação preenchidos, mostrando a prévia do Score B2G. |
| `07_relato_criado_radar.png` | Retorno ao `RadarScreen` logo após salvar o relato, mostrando o card novo no topo com o selo **"Seu Relato"** e a Snackbar de `+50 XP`. |
| `08_detalhe_acoes.png` | `BarrierDetailScreen` exibindo a foto com **Blur LGPD**, o botão **"Ainda está lá?"** confirmado (`+15 XP`) e os botões **Editar / Excluir** do relato próprio. |
| `09_painel_gestor_b2g.png` | `GuardianB2GScreen` autenticado como Gestor Público (`gestor@sp.gov.br` / `gestor123`), na aba **Fila de Obras B2G**, após clicar em **"Avançar Status B2G"** (ex.: status alterado para *Em Análise* ou *Resolvido*). |
| `10_persistencia_apos_reabrir.png` | **Feche o aplicativo completamente (remova dos apps recentes do Android) e abra novamente.** Capture a `RadarScreen` mostrando que o relato criado (`Seu Relato`), os favoritos, o XP no chip superior e o status alterado permanecem salvos via **Room**. |

