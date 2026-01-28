package ec.gob.sri.movil.common.data.networking

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Ejecuta una llamada Retrofit de forma segura y convierte el resultado a [DataResult].
 *
 * Responsabilidades:
 * - Capturar excepciones comunes de red/conectividad y mapearlas a [AppError.Network]
 * - Capturar fallos de serialización (kotlinx.serialization y casos frecuentes) → [AppError.Serialization]
 * - Preservar correctamente la cancelación de corrutinas (re-lanza [CancellationException])
 * - Delegar el mapeo HTTP a [HttpErrorMapper] para mantener el código abierto a extensión
 *
 * @param httpErrorMapper Mapper de errores HTTP (permite agregar más reglas sin modificar esta función).
 * @param execute lambda suspend que ejecuta la llamada Retrofit y retorna [Response].
 *
 * @return [DataResult.Success] si la llamada fue exitosa y el body es válido,
 *         o [DataResult.Error] con un [AppError] si ocurre cualquier fallo controlado.
 */
suspend inline fun <reified T> safeCall(
    httpErrorMapper: HttpErrorMapper,
    crossinline execute: suspend () -> Response<T>
): DataResult<T, AppError> {
    val response = try {
        execute()
    } catch (e: CancellationException) {
        throw e
    } catch (_: UnknownHostException) {
        // Host no resuelto / sin DNS / sin conexión.
        return DataResult.Error(AppError.Network.NoInternet)
    } catch (_: ConnectException) {
        // Fallo al establecer conexión.
        return DataResult.Error(AppError.Network.NoInternet)
    } catch (_: SocketTimeoutException) {
        // Tiempo de espera excedido.
        return DataResult.Error(AppError.Network.Timeout)
    } catch (_: SerializationException) {
        // Error de parseo con kotlinx.serialization.
        return DataResult.Error(AppError.Serialization)
    } catch (_: EOFException) {
        // Respuesta incompleta o JSON truncado; tratado como serialización.
        return DataResult.Error(AppError.Serialization)
    } catch (_: IllegalArgumentException) {
        // Casos donde el converter/adaptador lanza IllegalArgumentException al mapear.
        return DataResult.Error(AppError.Serialization)
    } catch (_: IOException) {
        // Errores IO genéricos de red (reset, broken pipe, SSL issues, etc.).
        return DataResult.Error(AppError.Network.Unavailable)
    } catch (e: Exception) {
        // Asegura que si el contexto fue cancelado, se propague la cancelación y no se reporte como error.
        currentCoroutineContext().ensureActive()
        return DataResult.Error(AppError.Unknown(e.message))
    }

    return responseToResult(response, httpErrorMapper)
}

/**
 * Convierte un [Response] de Retrofit a [DataResult].
 *
 * - En respuestas exitosas (2xx): retorna [DataResult.Success] con el body.
 * - Soporta endpoints sin body: si [T] es [Unit] o el código es 204, retorna Success(Unit).
 * - En respuestas no exitosas: extrae el cuerpo de error (si existe) y delega el mapeo a [HttpErrorMapper].
 *
 * @param response Respuesta Retrofit ya obtenida.
 * @param httpErrorMapper Mapper para convertir (statusCode, errorBody) a [AppError].
 */
inline fun <reified T> responseToResult(
    response: Response<T>,
    httpErrorMapper: HttpErrorMapper
): DataResult<T, AppError> {
    if (response.isSuccessful) {
        val body = response.body()
        @Suppress("UNCHECKED_CAST")
        return when {
            body != null -> DataResult.Success(body)
            T::class == Unit::class || response.code() == 204 -> DataResult.Success(Unit as T)
            else -> DataResult.Error(AppError.Serialization)
        }
    }

    val code = response.code()
    val errorBody = try {
        response.errorBody()?.string()
    } catch (_: Exception) {
        null
    }

    val normalized = normalizeErrorBody(errorBody)
    return DataResult.Error(httpErrorMapper.map(code, normalized))
}

