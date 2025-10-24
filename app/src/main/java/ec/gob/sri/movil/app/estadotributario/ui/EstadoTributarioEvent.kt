package ec.gob.sri.movil.app.estadotributario.ui

import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

sealed interface EstadoTributarioEvent {
    data class OnError(val errorMessage: String): EstadoTributarioEvent
    data class OnNavigateDetail(val estadoTributario: EstadoTributarioDomain): EstadoTributarioEvent
}