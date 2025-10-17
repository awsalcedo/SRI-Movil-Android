package ec.gob.sri.movil.app.estadotributario.data.remote.repository

import ec.gob.sri.movil.app.core.domain.Result
import ec.gob.sri.movil.app.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.app.estadotributario.domain.repository.EstadoTributarioRepository
import javax.inject.Inject

class EstadoTributarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: EstadoTributarioRemoteDataSource
) : EstadoTributarioRepository {
    
    override suspend fun consultarEstadoTributario(ruc: String): Result<EstadoTributarioDomain> {
        return remoteDataSource.consultarEstadoTributarioApi(ruc)
    }
}