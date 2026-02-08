package ec.gob.sri.movil.app.feature.deudas.domain.usecase

import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import javax.inject.Inject

class ConsultarDeudasUseCase @Inject constructor(
    private val repository: DeudasRepository
) {
    suspend operator fun invoke(ruc: String): DataResult<DeudasDomain, AppError> {
        return repository.consultarDeudas(ruc = ruc)
    }
}