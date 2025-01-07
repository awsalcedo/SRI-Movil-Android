package ec.gob.sri.movil.app.consultas.domain.model

data class ConsultasModel(
    val id: String,
    val nombre: String,
    val posicion: Int,
    val habilitado: Boolean,
    val autenticada: Boolean
)
