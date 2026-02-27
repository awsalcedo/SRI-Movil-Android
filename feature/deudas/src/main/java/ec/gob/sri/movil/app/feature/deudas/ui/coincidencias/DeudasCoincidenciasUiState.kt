package ec.gob.sri.movil.app.feature.deudas.ui.coincidencias

import androidx.compose.runtime.Immutable
import ec.gob.sri.movil.common.framework.ui.text.UiText

@Immutable
data class DeudasCoincidenciasUiState(
    val isLoading: Boolean = false,
    val razonSocial: String = "",
    val items: List<DeudasCoincidenciaItemUi> = emptyList(),
    val error: UiText? = null
)

@Immutable
data class DeudasCoincidenciaItemUi(
    val identificacion: String,
    val titulo: String,      // Nombre comercial / razón
    val subtitulo: String    // Clase + tipoIdentificación
)
