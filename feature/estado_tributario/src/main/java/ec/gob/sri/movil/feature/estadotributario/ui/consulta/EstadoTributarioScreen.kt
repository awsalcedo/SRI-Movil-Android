package ec.gob.sri.movil.feature.estadotributario.ui.consulta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.common.framework.ui.components.SriButton
import ec.gob.sri.movil.common.framework.ui.components.SriTextField
import ec.gob.sri.movil.common.framework.ui.util.ObserveAsEvents

@Composable
fun EstadoTributarioScreen(
    viewModel: EstadoTributarioViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackbarState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is EstadoTributarioEvent.OnError -> {
                snackbarState.showSnackbar(event.message.asString(context))
            }

            is EstadoTributarioEvent.OnNavigateDetail -> {
                onNavigateToDetail(event.estadoTributario.ruc)
            }
        }
    }

    EstadoTributarioContent(
        state = state,
        snackbarState = snackbarState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadoTributarioContent(
    state: EstadoTributarioState,
    snackbarState: SnackbarHostState,
    onAction: (EstadoTributarioAction) -> Unit
) {

    val focusManager = LocalFocusManager.current
    val showRucError = state.ruc.isNotBlank() && !state.isRucValid

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Estado Tributario",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarState)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            SriTextField(
                text = state.ruc,
                onValueChange = { onAction(EstadoTributarioAction.onRucChanged(it)) },
                label = "RUC:",
                hint = "Ej: 1700000000001",
                isInputSecret = false,
                isNumber = true,
                isLogin = false,
                isError = showRucError,
                supportingText = if (showRucError) "Este campo debe contener como mínimo 13 caracteres." else null,
                showClearIcon = true,
                onClear = {
                    onAction(EstadoTributarioAction.onRucChanged(""))
                },

                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    if (state.isConsultarEnabled) {
                        onAction(EstadoTributarioAction.OnConsultarClick)
                    }
                }),
                modifier = Modifier.fillMaxWidth()
            )

            if (showRucError) {
                Text(
                    text = "RUC inválido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.padding(10.0.dp))

            SriButton(
                text = if (state.isLoading) "Consultando..." else "Consultar",
                enabled = state.isConsultarEnabled,
                isLoading = state.isLoading,
                onClick = {
                    onAction(EstadoTributarioAction.OnConsultarClick)
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun EstadoTributarioContentPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        val snackbarState = remember {
            SnackbarHostState()
        }
        EstadoTributarioContent(
            state = EstadoTributarioState(ruc = ""),
            snackbarState = snackbarState,
            onAction = {}
        )
    }
}
