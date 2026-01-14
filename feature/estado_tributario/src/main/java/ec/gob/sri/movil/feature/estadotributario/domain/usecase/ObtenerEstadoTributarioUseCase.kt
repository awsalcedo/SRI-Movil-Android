package ec.gob.sri.movil.feature.estadotributario.domain.usecase

import ec.gob.sri.movil.app.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class ObtenerEstadoTributarioUseCase @Inject constructor(private val repository: EstadoTributarioRepository) {
    suspend operator fun invoke(ruc: String) = repository.consultarEstadoTributario(ruc)
}