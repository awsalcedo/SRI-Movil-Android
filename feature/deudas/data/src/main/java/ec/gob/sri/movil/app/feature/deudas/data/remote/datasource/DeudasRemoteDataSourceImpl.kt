package ec.gob.sri.movil.app.feature.deudas.data.remote.datasource

import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudasDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.service.DeudasService
import ec.gob.sri.movil.common.data.networking.safeCall
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper

import javax.inject.Inject

class DeudasRemoteDataSourceImpl @Inject constructor(
    private val service: DeudasService,
    private val httpErrorMapper: HttpErrorMapper
) : DeudasRemoteDataSource {
    override suspend fun consultarDeudasApi(ruc: String): DataResult<DeudasDto, AppError> {
        return safeCall(httpErrorMapper) {
            service.consultarDeudasApi(ruc = ruc)
        }
    }
}