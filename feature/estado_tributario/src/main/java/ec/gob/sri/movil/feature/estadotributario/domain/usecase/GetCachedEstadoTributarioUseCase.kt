package ec.gob.sri.movil.feature.estadotributario.domain.usecase

import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class GetCachedEstadoTributarioUseCase @Inject constructor(private val repository: EstadoTributarioRepository) {
    operator fun invoke(ruc: String): EstadoTributarioDomain? =
        repository.getCachedEstadoTributario(ruc)
}