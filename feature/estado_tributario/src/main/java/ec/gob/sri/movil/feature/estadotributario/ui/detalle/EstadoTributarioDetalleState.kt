package ec.gob.sri.movil.feature.estadotributario.ui.detalle

import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.common.framework.ui.text.UiText
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.models.ObligacionesPendientesDomain

data class EstadoTributarioDetalleState(
    val estadoTributario: EstadoTributarioDomain? = null,
    val isLoading: Boolean = false,
    val obligacionSeleccionada: ObligacionesPendientesDomain? = null,

    // Sheet Deudas
    val isDeudasSheetOpen: Boolean = false,
    val deudasLoading: Boolean = false,
    val deudasData: DeudasDomain? = null,
    val deudasError: UiText? = null
)