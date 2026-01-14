package ec.gob.sri.movil.app.core.data.networking

import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import retrofit2.Response

suspend inline fun <reified T> responseToResult(
    response: Response<T>
): DataResult<T, DataError.Network> {
    if (response.isSuccessful) {
        val body = response.body()
        return when {
            body != null -> DataResult.Success(body)
            // Soporte para respuestas sin contenido (204) o endpoints que devuelven Unit
            T::class == Unit::class -> DataResult.Success(Unit as T)
            response.code() == 204 -> DataResult.Success(Unit as T)
            else -> DataResult.Error(DataError.Network.Serialization(cause = itCause(), jsonPath = null)) // cuerpo nulo inesperado
        }
    } else {
        return when (response.code()) {
            408 -> DataResult.Error(DataError.Network.RequestTimeout(response.message()))
            429 -> DataResult.Error(DataError.Network.TooManyRequests(response.message()))
            in 500..599 -> DataResult.Error(DataError.Network.ServerError(code = response.code(), body = response.message()))
            else -> DataResult.Error(DataError.Network.Unknown)
        }
    }
}

fun itCause(): Throwable = RuntimeException("Serialization/Parsing error")