package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.core.domain.DataResult
import ec.gob.sri.movil.app.core.domain.DataError
import ec.gob.sri.movil.app.estadotributario.data.common.mapHttpResponseError
import ec.gob.sri.movil.app.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.app.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class EstadoTributarioRemoteDataSourceImpl @Inject constructor(
    private val apiService: EstadoTributarioService
) : EstadoTributarioRemoteDataSource {
    
    override suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network> {
        return try {
            val response = apiService.consultarApi(ruc)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    DataResult.Success(body.toDomain())
                } else {
                    DataResult.Error(DataError.Network.Unknown)
                }
            } else {
                // Use the specific HTTP error mapper
                DataResult.Error(mapHttpResponseError(response))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: UnknownHostException) {
            DataResult.Error(DataError.Network.NoInternet)
        } catch (e: IOException) {
            DataResult.Error(DataError.Network.NoInternet)
        } catch (e: HttpException) {
            // Use the specific HTTP error mapper for HttpException
            DataResult.Error(mapHttpException(e))
        } catch (e: Exception) {
            DataResult.Error(DataError.Network.Unknown)
        }
    }
}