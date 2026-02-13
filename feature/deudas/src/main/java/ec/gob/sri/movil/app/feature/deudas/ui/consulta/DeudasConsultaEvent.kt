package ec.gob.sri.movil.app.feature.deudas.ui.consulta

sealed interface DeudasConsultaEvent {
    data object NavigateBack : DeudasConsultaEvent
    data class ShowSnackbar(val message: String): DeudasConsultaEvent
    data class NavigateToResultados(val query: DeudasQuery): DeudasConsultaEvent
}

data class DeudasQuery(
    val contribuyenteType: ContribuyenteType,
    val idType: IdType,
    val ruc: String?,
    val cedula: String?,
    val apellidos: String?,
    val nombres: String?
)