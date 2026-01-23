package ec.gob.sri.movil.feature.estadotributario.domain.repository

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRepository {
    suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, AppError>
}