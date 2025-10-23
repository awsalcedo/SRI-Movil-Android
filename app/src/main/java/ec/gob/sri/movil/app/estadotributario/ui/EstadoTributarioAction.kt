package ec.gob.sri.movil.app.estadotributario.ui

sealed interface EstadoTributarioAction {
    data class onConsultaEstadoTributarioClick(val ruc: String) : EstadoTributarioAction
}