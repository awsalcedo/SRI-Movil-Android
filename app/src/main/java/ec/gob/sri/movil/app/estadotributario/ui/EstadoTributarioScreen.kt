package ec.gob.sri.movil.app.estadotributario.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.app.core.ui.util.ObserveAsEvents
import ec.gob.sri.movil.app.login.components.SriButton
import ec.gob.sri.movil.app.login.components.SriTextField

@Composable
fun EstadoTributarioScreen(
    viewModel: EstadoTributarioViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is EstadoTributarioEvent.OnError -> {
                snackbarState.showSnackbar(event.errorMessage)
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

    var rucValue by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

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
                text = rucValue,
                onValueChange = { rucValue = it },
                label = "RUC:",
                hint = "Ej: 1700000000001",
                isInputSecret = false,
                isLogin = false,
                keyboardActions = KeyboardActions(onAny = {
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(10.0.dp))

            SriButton(
                text = "Consultar",
                onClick = {
                    if (!state.isLoading) { //Evitar acciones concurrentes
                        onAction(EstadoTributarioAction.onConsultaEstadoTributarioClick(rucValue))
                    }
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


        /*Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                SriTextField(
                    text = rucValue,
                    onValueChange = { rucValue = it },
                    label = "RUC:",
                    hint = "Ej: 1700000000001",
                    isInputSecret = false,
                    isLogin = false,
                    keyboardActions = KeyboardActions(onAny = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.0.dp))

                SriButton(
                    text = "Consultar",
                    onClick = {
                        onAction(EstadoTributarioAction.onConsultaEstadoTributarioClick(rucValue))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }*/
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
