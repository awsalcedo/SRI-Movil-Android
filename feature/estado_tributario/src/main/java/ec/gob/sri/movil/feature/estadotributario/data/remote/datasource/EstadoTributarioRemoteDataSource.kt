package ec.gob.sri.movil.feature.estadotributario.data.remote.datasource

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.feature.estadotributario.data.remote.dto.EstadoTributarioDto

interface EstadoTributarioRemoteDataSource {
    suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDto, AppError>
}