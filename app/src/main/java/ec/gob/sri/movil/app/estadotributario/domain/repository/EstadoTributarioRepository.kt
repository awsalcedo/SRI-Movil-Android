package ec.gob.sri.movil.app.estadotributario.domain.repository

import ec.gob.sri.movil.app.core.domain.DataResult
import ec.gob.sri.movil.app.core.domain.DataError
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRepository {
    suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network>
}