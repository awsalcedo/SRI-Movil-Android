package ec.gob.sri.movil.app.estadotributario.domain.repository

import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.error.DataResult
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRepository {
    suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network>
}