package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import androidx.compose.runtime.Immutable

@Immutable
data class DeudasConsultaUiState(
    val tipoContribuyente: ContribuyenteType = ContribuyenteType.PERSONA_NATURAL,
    val idType: IdType = IdType.RUC,
    val ruc: String = "",
    val cedula: String = "",
    val apellidos: String = "",
    val nombres: String = "",
    val isLoading: Boolean = false
) {
    val isValid: Boolean
        get() = when(idType) {
            IdType.RUC -> ruc.length == 13
            IdType.CEDULA -> cedula.length == 10
            IdType.APELLIDOS_NOMBRES -> apellidos.isNotBlank() && nombres.isNotBlank()
        }
}

enum class ContribuyenteType(val label: String) {
    PERSONA_NATURAL("Persona Natural"),
    PERSONA_JURIDICA("Persona Jurídica")
}

enum class IdType(val label: String) {
    RUC("RUC"),
    CEDULA("Cédula"),
    APELLIDOS_NOMBRES("Apellidos y Nombres")
}