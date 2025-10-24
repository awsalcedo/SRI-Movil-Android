package ec.gob.sri.movil.app.estadotributario.ui

import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

data class EstadoTributarioState(
    val ruc: String = "",
    val isLoading: Boolean = false,
    val estadoTributario: EstadoTributarioDomain? = null
)