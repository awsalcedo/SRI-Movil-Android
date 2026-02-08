package ec.gob.sri.movil.app.feature.deudas.domain.models


data class DeudasDomain(
    val contribuyente: ContribuyenteDomain,
    val deuda: DeudaDomain,
    val impugnacion: String?,
    val remision: String?
)

data class ContribuyenteDomain(
    val identificacion: String,
    val denominacion: String?,
    val tipo: String?,
    val clase: String,
    val tipoIdentificacion: String,
    val resolucion: String?,
    val direccionMatriz: String?,
    val fechaInformacion: Long,
    val mensaje: String?,
    val estado: String?
)

data class DeudaDomain(
    val descripcion: String,
    val valor: Double,
    val periodoFiscal: String?,
    val beneficiario: String?,
    val detallesRubro: List<DetalleRubroDomain>?
)

data class DetalleRubroDomain(
    val descripcion: String,
    val anio: Int,
    val valor: Double,
)
