package ec.gob.sri.movil.app.feature.deudas.domain.models

data class DeudaPorDenominacionDomain(
    val identificacion: String,
    val denominacion: String?,
    val tipo: String?,
    val clase: String?,
    val tipoIdentificacion: String,
    val resolucion: String?,
    val nombreComercial: String,
    val direccionMatriz: String?,
    val fechaInformacion: Long?,
    val mensaje: String?,
    val estado: String?
)
