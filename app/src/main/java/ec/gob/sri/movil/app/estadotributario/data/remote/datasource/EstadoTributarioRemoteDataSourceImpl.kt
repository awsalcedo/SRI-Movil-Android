package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.app.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import javax.inject.Inject

class EstadoTributarioRemoteDataSourceImpl @Inject constructor(private val apiService: EstadoTributarioService) :
    EstadoTributarioRemoteDataSource {
    override suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDomain, DataError.Network> {
        return try {
            val result = apiService.consultarApi(ruc)
            if (result.isSuccessful) {
                result.body()?.let { DataResult.Success(it) }
                    ?: DataResult.Error(DataError.Network.ServerError)
            } else {
                DataResult.Error()
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            when (e.code()) {
                403 -> DataResult.Error(DataError.Network.ClientError)
                500 -> DataResult.Error(DataError.Network.ServerError)
                else -> DataResult.Error(DataError.Network.Unknown)
            }
        }
    }
}