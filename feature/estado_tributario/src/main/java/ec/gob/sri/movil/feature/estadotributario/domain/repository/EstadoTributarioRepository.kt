package ec.gob.sri.movil.feature.estadotributario.domain.repository

import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRepository {
    suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network>
}