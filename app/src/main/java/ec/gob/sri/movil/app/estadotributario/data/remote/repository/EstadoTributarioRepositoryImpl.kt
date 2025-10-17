package ec.gob.sri.movil.app.estadotributario.data.remote.repository

import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.error.DataResult
import ec.gob.sri.movil.app.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.app.estadotributario.domain.repository.EstadoTributarioRepository
import timber.log.Timber
import javax.inject.Inject

class EstadoTributarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: EstadoTributarioRemoteDataSource
) : EstadoTributarioRepository {
    
    override suspend fun consultarEstadoTributario(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network> {
        Timber.d("Repository: Consulting estado tributario for RUC: $ruc")
        
        return remoteDataSource.consultarEstadoTributarioApi(ruc)
            .also { result ->
                when (result) {
                    is DataResult.Success -> Timber.d("Repository: Successfully retrieved estado tributario")
                    is DataResult.Error -> Timber.w("Repository: Failed to retrieve estado tributario: ${result.error}")
                }
            }
    }
}