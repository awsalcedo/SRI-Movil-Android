package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
data class DeudasConsultaUiState(
    val tipoContribuyente: ContribuyenteType = ContribuyenteType.PERSONA_NATURAL,
    val idType: IdType = IdType.RUC,
    val ruc: String = "",
    val cedula: String = "",
    val apellidos: String = "",
    val nombres: String = "",
    val razonSocialInput: String = "",
    val isLoading: Boolean = false
) {
    val isValid: Boolean
        get() = when (idType) {
            IdType.RUC -> ruc.length == 13
            IdType.CEDULA -> cedula.length == 10
            IdType.APELLIDOS_NOMBRES -> apellidos.isNotBlank()
            IdType.RAZON_SOCIAL -> razonSocialInput.isNotBlank()
        }

    val tipoPersona: String
        get() = when (tipoContribuyente) {
            ContribuyenteType.PERSONA_NATURAL -> "N"
            ContribuyenteType.PERSONA_JURIDICA -> "J"
        }

    val razonSocial: String
        get() = when (idType) {
            IdType.RAZON_SOCIAL -> razonSocialInput.trim()
            IdType.APELLIDOS_NOMBRES -> "${apellidos.trim()} ${nombres.trim()}".trim()
            else -> "" // no aplica para RUC/CEDULA
        }
}

@Serializable
enum class ContribuyenteType(val label: String) {
    PERSONA_NATURAL("Persona Natural"),
    PERSONA_JURIDICA("Sociedades")
}

@Serializable
enum class IdType(val label: String) {
    RUC("RUC"),
    CEDULA("Cédula"),
    APELLIDOS_NOMBRES("Apellidos y Nombres"),
    RAZON_SOCIAL("Razón Social")
}