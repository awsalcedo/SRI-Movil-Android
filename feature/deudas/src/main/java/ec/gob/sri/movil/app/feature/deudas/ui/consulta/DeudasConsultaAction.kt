package ec.gob.sri.movil.app.feature.deudas.ui.consulta

sealed interface DeudasConsultaAction {
    data class TipoContribuyenteSelected(val value: ContribuyenteType) : DeudasConsultaAction
    data class IdTypeSelected(val value: IdType) : DeudasConsultaAction
    data class RucChanged(val value: String) : DeudasConsultaAction
    data class CedulaChanged(val value: String) : DeudasConsultaAction
    data class ApellidosChanged(val value: String) : DeudasConsultaAction
    data class NombresChanged(val value: String) : DeudasConsultaAction

    data class RazonSocialChanged(val value: String) : DeudasConsultaAction
    data object ConsultarClicked : DeudasConsultaAction
}