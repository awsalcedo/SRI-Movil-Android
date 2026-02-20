package ec.gob.sri.movil.app.feature.deudas.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeudaPorDenominacionDto(
    @SerialName("identificacion")
    val identificacion: String,
    @SerialName("denominacion")
    val denominacion: String? = null,
    @SerialName("tipo")
    val tipo: String? = null,
    @SerialName("clase")
    val clase: String,
    @SerialName("tipoIdentificacion")
    val tipoIdentificacion: String,
    @SerialName("resolucion")
    val resolucion: String? = null,
    @SerialName("nombreComercial")
    val nombreComercial: String,
    @SerialName("direccionMatriz")
    val direccionMatriz: String? = null,
    @SerialName("fechaInformacion")
    val fechaInformacion: Long? = null,
    @SerialName("mensaje")
    val mensaje: String? = null,
    @SerialName("estado")
    val estado: String? = null
)
