package ec.gob.sri.movil.app.consultas.ui

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel

data class ConsultasState(
    val isLoading: Boolean = false,
    val consultas: List<ConsultasModel> = emptyList(),
    val errorMessage: String? = null
)