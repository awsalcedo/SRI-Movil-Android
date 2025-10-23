package ec.gob.sri.movil.app.estadotributario.ui

sealed interface EstadoTributarioEvent {
    data class OnError(val errorMessage: String): EstadoTributarioEvent
}