package ec.gob.sri.movil.app.feature.deudas.domain.usecase

import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import javax.inject.Inject

class GetCachedDeudaPorIdentificacionUseCase @Inject constructor(private val repository: DeudasRepository) {
    operator fun invoke(identificacion: String, tipoPersona: String): DeudasDomain? =
        repository.getCacheConsultaPorIdentificacion(
            identificacion = identificacion,
            tipoPersona = tipoPersona
        )
}