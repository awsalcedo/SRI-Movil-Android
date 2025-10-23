package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import com.google.gson.JsonSyntaxException
import ec.gob.sri.movil.app.estadotributario.data.common.mapHttpToNetworkError
import ec.gob.sri.movil.app.estadotributario.data.common.errorBodyAsString
import ec.gob.sri.movil.app.estadotributario.data.common.extractServerMessage
import ec.gob.sri.movil.app.estadotributario.data.remote.dto.EstadoTributarioDto
import ec.gob.sri.movil.app.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EstadoTributarioRemoteDataSourceImpl @Inject constructor(private val apiService: EstadoTributarioService) :
    EstadoTributarioRemoteDataSource {
    override suspend fun consultarEstadoTributarioApi(ruc: String): DataResult<EstadoTributarioDto, DataError.Network> {
        return try {
            val response = apiService.consultarApi(ruc)
            if (response.isSuccessful) {
                response.body()?.let { DataResult.Success(it) }
                    ?: DataResult.Error(
                        DataError.Network.ServerError(
                            response.code().takeIf { it in 200..299 } ?: 500,
                            null
                        )
                    )
            } else {
                val raw = response.errorBodyAsString()
                val errorMessage = extractServerMessage(raw) ?: raw
                DataResult.Error(
                    mapHttpToNetworkError(
                        response.code(),
                        errorMessage
                    )
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: SocketTimeoutException) {
            DataResult.Error(DataError.Network.RequestTimeout(null))
        } catch (e: UnknownHostException) {
            DataResult.Error(DataError.Network.NoInternet)
        } catch (e: IOException) {
            DataResult.Error(DataError.Network.NoInternet)
        } catch (e: SerializationException) {
            DataResult.Error(DataError.Network.Serialization(e))
        } catch (_: Exception) {
            DataResult.Error(DataError.Network.Unknown)
        }
    }
}