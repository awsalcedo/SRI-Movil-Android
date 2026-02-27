package ec.gob.sri.movil.app.feature.deudas.ui.detalle

import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.common.framework.ui.text.UiText

data class DeudasDetalleState(
    val isLoading: Boolean = false,
    val data: DeudasDomain? = null,
    val error: UiText? = null
)
