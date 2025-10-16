package ec.gob.sri.movil.app.estadotributario.domain.models

data class ObligacionesPendientesDomain (
    val descripcion: String,
    val periodos: List<String>? = emptyList()
)