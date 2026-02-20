package ec.gob.sri.movil.app.feature.deudas.domain.usecase

import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import javax.inject.Inject

class ConsultarPorIdentificacionUseCase @Inject constructor(private val repository: DeudasRepository) {
    suspend operator fun invoke(identificacion: String, tipoPersona: String) =
        repository.consultarPorIdentificacion(
            identificacion = identificacion,
            tipoPersona = tipoPersona
        )
}