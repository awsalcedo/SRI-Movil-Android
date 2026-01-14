package ec.gob.sri.movil.feature.estadotributario.ui

sealed interface EstadoTributarioAction {
    data class onConsultaEstadoTributarioClick(val ruc: String) : EstadoTributarioAction
}