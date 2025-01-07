package ec.gob.sri.movil.app.consultas.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultasDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("posicion")
    val posicion: Int,
    @SerializedName("habilitado")
    val habilitado: Boolean,
    @SerializedName("autenticada")
    val autenticada: Boolean
)

