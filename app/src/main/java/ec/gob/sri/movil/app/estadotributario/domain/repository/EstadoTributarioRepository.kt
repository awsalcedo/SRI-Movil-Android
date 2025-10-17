package ec.gob.sri.movil.app.estadotributario.domain.repository

import ec.gob.sri.movil.app.core.domain.Result
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

interface EstadoTributarioRepository {
    suspend fun consultarEstadoTributario(ruc: String): Result<EstadoTributarioDomain>
}