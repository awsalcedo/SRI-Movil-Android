package ec.gob.sri.movil.feature.estadotributario.data.remote.repository

import ec.gob.sri.movil.app.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.app.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult.*
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.app.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class EstadoTributarioRepositoryImpl @Inject constructor(private val remoteDataSource: EstadoTributarioRemoteDataSource) :
    EstadoTributarioRepository {
    override suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network> {
        return when (val result = remoteDataSource.consultarEstadoTributarioApi(ruc)) {
            is Success -> Success(result.data.toDomain())
            is Error -> Error(result.error)
        }
    }
}