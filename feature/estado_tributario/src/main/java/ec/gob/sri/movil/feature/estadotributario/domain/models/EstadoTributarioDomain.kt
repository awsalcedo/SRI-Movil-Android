package ec.gob.sri.movil.feature.estadotributario.domain.models

data class EstadoTributarioDomain(
    val ruc: String,
    val razonSocial: String,
    val descripcion: String,
    val plazoVigenciaDoc: String,
    val claseContribuyente: String,
    val obligacionesPendientes: List<ObligacionesPendientesDomain>?
)
