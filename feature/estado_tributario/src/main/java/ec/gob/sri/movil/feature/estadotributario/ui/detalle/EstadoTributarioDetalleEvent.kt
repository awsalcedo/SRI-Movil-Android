package ec.gob.sri.movil.feature.estadotributario.ui.detalle

import ec.gob.sri.movil.common.framework.ui.text.UiText

sealed interface EstadoTributarioDetalleEvent {
    data class OnError(val message: UiText): EstadoTributarioDetalleEvent
}