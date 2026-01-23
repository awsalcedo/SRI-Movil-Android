package ec.gob.sri.movil.feature.estadotributario.ui

import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain

data class EstadoTributarioState(
    val ruc: String = "",
    val isRucValid: Boolean = false,
    val isLoading: Boolean = false,
    val estadoTributario: EstadoTributarioDomain? = null
) {
    val isConsultarEnabled: Boolean get() = isRucValid && !isLoading
}