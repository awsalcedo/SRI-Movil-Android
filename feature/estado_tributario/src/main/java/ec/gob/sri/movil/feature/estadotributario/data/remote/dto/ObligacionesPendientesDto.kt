package ec.gob.sri.movil.feature.estadotributario.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ObligacionesPendientesDto(
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("periodos")
    val periodos: List<String>? = emptyList()
)