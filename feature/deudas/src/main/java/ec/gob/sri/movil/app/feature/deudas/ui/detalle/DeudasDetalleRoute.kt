package ec.gob.sri.movil.app.feature.deudas.ui.detalle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasQuery

@Composable
fun DeudasDetalleRoute(
    query: DeudasQuery.PorIdentificacion,
    onBack: () -> Unit,
    viewModel: DeudasDetalleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(query.identificacion, query.tipoPersona) {
        viewModel.load(
            identificacion = query.identificacion,
            tipoPersona = query.tipoPersona
        )
    }

    DeudasDetalleScreen(
        state = state,
        onBack = onBack
    )
}