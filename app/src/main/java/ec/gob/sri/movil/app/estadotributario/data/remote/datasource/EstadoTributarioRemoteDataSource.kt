package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.estadotributario.data.remote.dto.EstadoTributarioDto
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult

interface EstadoTributarioRemoteDataSource {
    suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDto, DataError.Network>
}