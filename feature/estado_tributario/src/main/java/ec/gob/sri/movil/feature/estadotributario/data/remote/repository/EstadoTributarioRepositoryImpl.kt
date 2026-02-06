package ec.gob.sri.movil.feature.estadotributario.data.remote.repository

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.domain.error.map
import ec.gob.sri.movil.feature.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.feature.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class EstadoTributarioRepositoryImpl @Inject constructor(private val remoteDataSource: EstadoTributarioRemoteDataSource) :
    EstadoTributarioRepository {

    private var last: EstadoTributarioDomain? = null

    override suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, AppError> {
        return remoteDataSource.consultarEstadoTributarioApi(ruc)
            .map { dto ->
                dto.toDomain().also { domain ->
                    dto.toDomain().also { domain -> last = domain }
                }
            }


    }

    override fun getCachedEstadoTributario(ruc: String): EstadoTributarioDomain? =
        last?.takeIf { it.ruc == ruc }

}