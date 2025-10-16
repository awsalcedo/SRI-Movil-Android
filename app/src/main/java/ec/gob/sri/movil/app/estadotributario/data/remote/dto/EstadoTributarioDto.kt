package ec.gob.sri.movil.app.estadotributario.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstadoTributarioDto(
    @SerialName("ruc")
    val ruc: String,
    @SerialName("razonSocial")
    val razonSocial: String,
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("plazoVigenciaDoc")
    val plazoVigenciaDoc: String,
    @SerialName("claseContribuyente")
    val claseContribuyente: String,
    @SerialName("obligacionesPendientes")
    val obligacionesPendientes: List<ObligacionesPendientesDto>
)
