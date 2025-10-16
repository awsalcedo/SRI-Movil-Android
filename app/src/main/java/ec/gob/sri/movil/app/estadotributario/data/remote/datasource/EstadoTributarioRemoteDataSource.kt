package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRemoteDataSource {
    suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network>
}