package ec.gob.sri.movil.feature.estadotributario.ui

import ec.gob.sri.movil.common.framework.ui.text.UiText
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain

sealed interface EstadoTributarioEvent {
    data class OnError(val message: UiText) : EstadoTributarioEvent
    data class OnNavigateDetail(val estadoTributario: EstadoTributarioDomain) :
        EstadoTributarioEvent
}