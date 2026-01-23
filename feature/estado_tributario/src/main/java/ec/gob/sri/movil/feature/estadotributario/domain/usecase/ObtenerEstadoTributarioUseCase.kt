package ec.gob.sri.movil.feature.estadotributario.domain.usecase

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class ObtenerEstadoTributarioUseCase @Inject constructor(private val repository: EstadoTributarioRepository) {
    suspend operator fun invoke(ruc: String): DataResult<EstadoTributarioDomain, AppError> {
        return repository.consultarEstadoTributario(ruc)
    }
}