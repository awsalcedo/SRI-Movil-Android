package ec.gob.sri.movil.feature.estadotributario.data.remote.datasource

import ec.gob.sri.movil.common.data.networking.safeCall
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper
import ec.gob.sri.movil.feature.estadotributario.data.remote.dto.EstadoTributarioDto
import ec.gob.sri.movil.feature.estadotributario.data.remote.service.EstadoTributarioService
import javax.inject.Inject

class EstadoTributarioRemoteDataSourceImpl @Inject constructor(
    private val service: EstadoTributarioService,
    private val httpErrorMapper: HttpErrorMapper
) : EstadoTributarioRemoteDataSource {

    override suspend fun consultarEstadoTributarioApi(
        ruc: String
    ): DataResult<EstadoTributarioDto, AppError> {
        return safeCall(httpErrorMapper) {
            service.consultarApi(ruc)
        }
    }
}