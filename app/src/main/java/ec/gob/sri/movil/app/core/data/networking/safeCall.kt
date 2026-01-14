package ec.gob.sri.movil.app.core.data.networking

import com.google.gson.JsonParseException
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    crossinline execute: suspend () -> Response<T>
): DataResult<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: CancellationException) {
        // Muy importante: no atrapamos cancelaciones
        throw e
    } catch (e: UnknownHostException) {
        return DataResult.Error(DataError.Network.NoInternet)
    } catch (e: ConnectException) {
        return DataResult.Error(DataError.Network.NoInternet)
    } catch (e: SocketTimeoutException) {
        return DataResult.Error(DataError.Network.RequestTimeout(e.message))
    } /*catch (e: SerializationException) {
        return DataResult.Error(DataError.Network.Serialization(e.cause))
    } catch (e: JsonParseException) {
        return DataResult.Error(NetworkError.SERIALIZATION)
    }*/ catch (e: Exception) {
        // Si la corrutina fue cancelada entre try y catch
        coroutineContext.ensureActive()
        return DataResult.Error(DataError.Network.Unknown)
    }

    return responseToResult(response)
}