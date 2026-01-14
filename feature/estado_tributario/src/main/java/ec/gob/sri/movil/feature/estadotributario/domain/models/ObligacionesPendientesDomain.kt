package ec.gob.sri.movil.feature.estadotributario.domain.models

data class ObligacionesPendientesDomain (
    val descripcion: String,
    val periodos: List<String>? = emptyList()
)