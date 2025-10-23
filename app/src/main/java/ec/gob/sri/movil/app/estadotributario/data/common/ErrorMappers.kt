package ec.gob.sri.movil.app.estadotributario.data.common

import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import org.json.JSONObject
import retrofit2.Response

fun Response<*>.errorBodyAsString(maxChars: Int = 8192): String? =
    try {
        val raw = errorBody()?.string() ?: return null
        if (raw.length > maxChars) raw.take(maxChars) else raw
    } catch (_: Exception) {
        null
    }

fun extractServerMessage(errText: String?): String? =
    errText?.let {
        runCatching {
            JSONObject(it).optString("mensaje").takeIf { m -> m.isNotBlank() }
        }.getOrNull()
    }


fun mapHttpToNetworkError(code: Int, body: String?): DataError.Network.Http = when (code) {
    400 -> DataError.Network.BadRequest(body)
    401 -> DataError.Network.Unauthorized(body)
    403 -> DataError.Network.Forbidden(body)
    404 -> DataError.Network.NotFound(body)
    408 -> DataError.Network.RequestTimeout(body)
    413 -> DataError.Network.PayLoadTooLarge(body)
    409 -> DataError.Network.Conflict(body)
    422 -> DataError.Network.UnprocessableEntity(body)
    429 -> DataError.Network.TooManyRequests(body)
    in 500..599 -> DataError.Network.ServerError(code, body)
    else -> DataError.Network.UnexpectedHttp(code, body)
}