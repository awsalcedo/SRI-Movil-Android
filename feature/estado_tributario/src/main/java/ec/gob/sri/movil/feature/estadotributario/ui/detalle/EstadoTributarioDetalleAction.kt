package ec.gob.sri.movil.feature.estadotributario.ui.detalle

sealed interface EstadoTributarioDetalleAction {
    data class OnLoad(val ruc: String): EstadoTributarioDetalleAction
}