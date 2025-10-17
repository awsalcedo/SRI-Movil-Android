package ec.gob.sri.movil.app.estadotributario.domain.usecase

import ec.gob.sri.movil.app.core.domain.DataResult
import ec.gob.sri.movil.app.core.domain.DataError
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.app.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class ObtenerEstadoTributarioUseCase @Inject constructor(
    private val repository: EstadoTributarioRepository
) {
    suspend operator fun invoke(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network> = 
        repository.consultarEstadoTributario(ruc)
}