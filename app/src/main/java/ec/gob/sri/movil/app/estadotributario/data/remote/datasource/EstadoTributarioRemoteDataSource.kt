package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.core.domain.Result
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRemoteDataSource {
    suspend fun consultarEstadoTributarioApi(ruc: String): Result<EstadoTributarioDomain>
}