package ec.gob.sri.movil.feature.estadotributario.ui.detalle

import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.models.ObligacionesPendientesDomain

data class EstadoTributarioDetalleState(
    val estadoTributario: EstadoTributarioDomain? = null,
    val isLoading: Boolean = false,
    val obligacionSeleccionada: ObligacionesPendientesDomain? = null
)