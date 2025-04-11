package ec.gob.sri.movil.app.consultas.data.remote.error

import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.error.ErrorHandler
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ConsultasErrorHandlerImpl: ErrorHandler {
    override fun handleNetWorkError(throwable: Throwable): DataError.Network {
        return when (throwable) {
            is CancellationException -> throw throwable
            is IOException -> handleIOException(throwable)
            is HttpException -> handleHttpException(throwable)

            else -> handleUnknownError(throwable)
        }.also { error ->

            Timber.w("Network", "Api error: $error", throwable)
        }
    }

    override fun parseHttpError(exception: HttpException): String {
        return try {
            exception.response()?.errorBody()?.string()
                ?: exception.message
                ?: "HTTP error ${exception.code()}"
        } catch (e: Exception) {
            Timber.w(e, "Error al analizar el cuerpo del error HTTP")
            e.message ?: "Error desconocido"
        }
    }

    private fun handleIOException(e: IOException): DataError.Network {
        Timber.w(e, "Red no disponible")
        return DataError.Network.NoInternet(e.message ?: "Red no disponible")
    }

    private fun handleHttpException(e: HttpException): DataError.Network {
        val errorMessage = parseHttpError(e)
        return when (e.code()) {
            in 400..499 -> {
                Timber.w("Error de cliente: ${e.code()} - $errorMessage")
                DataError.Network.ClientError(e.code(), errorMessage)
            }

            in 500..599 -> {
                Timber.e(e, "Server error: ${e.code()}")
                DataError.Network.ServerError(e.code(), errorMessage)
            }

            else -> {
                Timber.e(e, "Error de red desconocido")
                DataError.Network.Unknown(errorMessage)
            }
        }
    }

    private fun handleUnknownError(e: Throwable): DataError.Network {
        Timber.e(e, "Error de red inesperado")
        return DataError.Network.Unknown(e.message ?: "Error de red inesperado")
    }
}