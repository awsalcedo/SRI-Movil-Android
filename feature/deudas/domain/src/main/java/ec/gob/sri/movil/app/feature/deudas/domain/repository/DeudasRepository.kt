package ec.gob.sri.movil.app.feature.deudas.domain.repository

import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult

interface DeudasRepository {
    suspend fun consultarDeudas(ruc: String): DataResult<DeudasDomain, AppError>
}