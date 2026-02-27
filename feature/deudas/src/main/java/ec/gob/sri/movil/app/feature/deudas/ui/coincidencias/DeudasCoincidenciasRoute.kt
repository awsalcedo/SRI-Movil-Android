package ec.gob.sri.movil.app.feature.deudas.ui.coincidencias

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasQuery

@Composable
fun DeudasCoincidenciasRoute(
    query: DeudasQuery.PorDenominacion,
    onBack: () -> Unit,
    onSelectedIdentificacion: (String) -> Unit,
    viewModel: DeudasCoincidenciasViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(query.razonSocial, query.tipoPersona, query.resultados) {
        viewModel.load(
            razonSocial = query.razonSocial,
            tipoPersona = query.tipoPersona,
            resultados = query.resultados
        )
    }

    DeudasCoincidenciasScreen(
        state = state,
        onBack = onBack,
        onSelected = onSelectedIdentificacion
    )
}