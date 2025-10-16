package ec.gob.sri.movil.app.estadotributario.data.common

import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import retrofit2.HttpException
import retrofit2.Response

fun Response<*>.safeErrorBodyString(): String? = try {
    errorBody()?.string()
} catch (_: Throwable) {
    null
}

fun mapHttpResponseError(resp: Response<*>): DataError.Network {
    val code = resp.code()
    val body = resp.safeErrorBodyString()

    return when (code) {
        400 -> DataError.Network.BadRequest(body)
        401 -> DataError.Network.Unauthorized(body)
        403 -> DataError.Network.Forbidden(body)
        404 -> DataError.Network.NotFound(body)
        409 -> DataError.Network.Conflict(body)
        422 -> DataError.Network.UnprocessableEntity(body)
        413 -> DataError.Network.PayLoadTooLarge(body)
        408 -> DataError.Network.RequestTimeout(body)
        429 -> DataError.Network.TooManyRequests(body)
        in 500..599 -> DataError.Network.ServerError(code, body)
        else -> DataError.Network.UnexpectedHttp(code, body)
    }
}

// -- HTTP map desde HttpException --
fun mapHttpException(e: HttpException): DataError.Network {
    val code = e.code()
    val body = try {
        e.response()?.errorBody()?.string()
    } catch (_: Throwable) {
        null
    }

    return when (code) {

    }
}