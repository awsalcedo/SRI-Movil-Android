package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.error.DataResult
import ec.gob.sri.movil.app.core.domain.error.ErrorHandler
import ec.gob.sri.movil.app.core.domain.error.safeDataResult
import ec.gob.sri.movil.app.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.app.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject

class EstadoTributarioRemoteDataSourceImpl @Inject constructor(
    private val apiService: EstadoTributarioService,
    private val errorHandler: ErrorHandler
) : EstadoTributarioRemoteDataSource {
    
    override suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network> {
        return safeDataResult(
            errorMapper = errorHandler::handleNetworkError
        ) {
            Timber.d("Consulting estado tributario for RUC: $ruc")
            
            val response = apiService.consultarApi(ruc)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Timber.d("Estado tributario response received successfully")
                    body.toDomain()
                } else {
                    Timber.w("Estado tributario response body is null")
                    throw IllegalStateException("Response body is null")
                }
            } else {
                Timber.w("Estado tributario API returned error: ${response.code()} - ${response.message()}")
                throw IllegalStateException("API returned error: ${response.code()}")
            }
        }
    }
}