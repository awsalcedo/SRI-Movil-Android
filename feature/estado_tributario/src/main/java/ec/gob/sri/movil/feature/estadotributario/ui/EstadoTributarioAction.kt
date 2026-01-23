package ec.gob.sri.movil.feature.estadotributario.ui

sealed interface EstadoTributarioAction {
    data object OnConsultarClick : EstadoTributarioAction
    data class onRucChanged(val ruc: String) : EstadoTributarioAction
}