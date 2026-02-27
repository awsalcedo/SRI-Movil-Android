package ec.gob.sri.movil.app.feature.deudas.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeudasDto(
    @SerialName("contribuyente")
    val contribuyente: ContribuyenteDto,
    @SerialName("deuda")
    val deuda: DeudaDto? = null,
    @SerialName("impugnacion")
    val impugnacion: String? = null,
    @SerialName("remision")
    val remision: String? = null
)


@Serializable
data class ContribuyenteDto(
    @SerialName("identificacion")
    val identificacion: String,
    @SerialName("denominacion")
    val denominacion: String? = null,
    @SerialName("tipo")
    val tipo: String? = null,
    @SerialName("clase")
    val clase: String? = null,
    @SerialName("tipoIdentificacion")
    val tipoIdentificacion: String,
    @SerialName("resolucion")
    val resolucion: String? = null,
    @SerialName("nombreComercial")
    val nombreComercial: String,
    @SerialName("direccionMatriz")
    val direccionMatriz: String? = null,
    @SerialName("fechaInformacion")
    val fechaInformacion: Long,
    @SerialName("mensaje")
    val mensaje: String? = null,
    @SerialName("estado")
    val estado: String? = null
)

@Serializable
data class DeudaDto(
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("valor")
    val valor: Double,
    @SerialName("periodoFiscal")
    val periodoFiscal: String?,
    @SerialName("beneficiario")
    val beneficiario: String?,
    @SerialName("detallesRubro")
    val detallesRubro: List<DetalleRubroDto>?
)

@Serializable
data class DetalleRubroDto(
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("anio")
    val anio: Int,
    @SerialName("valor")
    val valor: Double,
)