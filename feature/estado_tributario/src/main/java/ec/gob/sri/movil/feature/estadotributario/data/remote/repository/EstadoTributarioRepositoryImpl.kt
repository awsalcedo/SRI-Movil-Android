package ec.gob.sri.movil.feature.estadotributario.data.remote.repository

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.feature.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.feature.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class EstadoTributarioRepositoryImpl @Inject constructor(private val remoteDataSource: EstadoTributarioRemoteDataSource) :
    EstadoTributarioRepository {
    override suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, AppError> {
        return when (val result = remoteDataSource.consultarEstadoTributarioApi(ruc)) {
            is DataResult.Success -> DataResult.Success(result.data.toDomain())
            is DataResult.Error -> DataResult.Error(result.error)
        }
    }
}