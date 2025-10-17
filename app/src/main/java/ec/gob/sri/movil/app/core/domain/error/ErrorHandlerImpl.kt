package ec.gob.sri.movil.app.core.domain.error

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Unified implementation of ErrorHandler that provides consistent error mapping
 * across the entire application.
 */
@Singleton
class ErrorHandlerImpl @Inject constructor() : ErrorHandler {
    
    override fun handleNetworkError(throwable: Throwable): DataError.Network {
        return when (throwable) {
            is CancellationException -> throw throwable
            
            is HttpException -> {
                val errorMessage = parseHttpError(throwable)
                mapHttpError(throwable.code(), null, errorMessage)
            }
            
            is SocketTimeoutException -> {
                Timber.w(throwable, "Network timeout")
                DataError.Network.Timeout
            }
            
            is UnknownHostException -> {
                Timber.w(throwable, "No internet connection")
                DataError.Network.NoInternet
            }
            
            is IOException -> {
                Timber.w(throwable, "Network IO error")
                DataError.Network.NoInternet
            }
            
            is kotlinx.serialization.SerializationException -> {
                Timber.w(throwable, "Serialization error")
                DataError.Network.Serialization(
                    cause = throwable,
                    message = throwable.message
                )
            }
            
            else -> {
                Timber.e(throwable, "Unknown network error")
                DataError.Network.Unknown(
                    cause = throwable,
                    message = throwable.message
                )
            }
        }
    }
    
    override fun handleLocalError(throwable: Throwable): DataError.Local {
        return when (throwable) {
            is SQLiteException -> {
                Timber.e(throwable, "Database error")
                DataError.Local.Database
            }
            
            is IOException -> {
                when {
                    throwable.message?.contains("No space left", ignoreCase = true) == true -> {
                        Timber.e(throwable, "Disk full")
                        DataError.Local.DiskFull
                    }
                    else -> {
                        Timber.e(throwable, "Local storage IO error")
                        DataError.Local.Unknown(
                            cause = throwable,
                            message = throwable.message
                        )
                    }
                }
            }
            
            is kotlinx.serialization.SerializationException -> {
                Timber.e(throwable, "Data corruption detected")
                DataError.Local.CorruptedData
            }
            
            is SecurityException -> {
                Timber.e(throwable, "Storage access denied")
                DataError.Local.Unknown(
                    cause = throwable,
                    message = "Storage access denied"
                )
            }
            
            else -> {
                Timber.e(throwable, "Unknown local error")
                DataError.Local.Unknown(
                    cause = throwable,
                    message = throwable.message
                )
            }
        }
    }
    
    override fun handleAuthError(throwable: Throwable): DataError.Auth {
        return when (throwable) {
            is HttpException -> when (throwable.code()) {
                401 -> DataError.Auth.InvalidCredentials
                403 -> DataError.Auth.UnauthorizedAccess
                else -> DataError.Auth.AuthenticationFailed
            }
            
            is SocketTimeoutException -> DataError.Auth.AuthenticationFailed
            
            else -> {
                Timber.e(throwable, "Unknown auth error")
                DataError.Auth.Unknown(
                    cause = throwable,
                    message = throwable.message
                )
            }
        }
    }
    
    override fun handleBusinessError(throwable: Throwable): DataError.Business {
        return when (throwable) {
            is IllegalArgumentException -> DataError.Business.InvalidInput
            is IllegalStateException -> DataError.Business.OperationNotAllowed
            
            else -> {
                Timber.e(throwable, "Unknown business error")
                DataError.Business.Unknown(
                    cause = throwable,
                    message = throwable.message
                )
            }
        }
    }
    
    override fun parseHttpError(exception: HttpException): String {
        return try {
            exception.response()?.errorBody()?.string()
                ?: exception.message
                ?: "HTTP error ${exception.code()}"
        } catch (e: Exception) {
            Timber.w(e, "Error parsing HTTP error response")
            e.message ?: "Error desconocido"
        }
    }
    
    override fun mapHttpError(code: Int, body: String?, message: String?): DataError.Network.Http {
        return when (code) {
            400 -> DataError.Network.Http.BadRequest(body, message)
            401 -> DataError.Network.Http.Unauthorized(body, message)
            403 -> DataError.Network.Http.Forbidden(body, message)
            404 -> DataError.Network.Http.NotFound(body, message)
            409 -> DataError.Network.Http.Conflict(body, message)
            422 -> DataError.Network.Http.UnprocessableEntity(body, message)
            429 -> DataError.Network.Http.TooManyRequests(body, message)
            500 -> DataError.Network.Http.InternalServerError(body, message)
            502 -> DataError.Network.Http.BadGateway(body, message)
            503 -> DataError.Network.Http.ServiceUnavailable(body, message)
            504 -> DataError.Network.Http.GatewayTimeout(body, message)
            else -> DataError.Network.Http.UnexpectedHttp(code, body, message)
        }
    }
    
    override fun handleGenericError(throwable: Throwable): DataError {
        return when (throwable) {
            is HttpException -> handleNetworkError(throwable)
            is IOException -> handleNetworkError(throwable)
            is SQLiteException -> handleLocalError(throwable)
            is IllegalArgumentException -> handleBusinessError(throwable)
            is IllegalStateException -> handleBusinessError(throwable)
            else -> {
                Timber.e(throwable, "Unclassified error")
                DataError.Network.Unknown(
                    cause = throwable,
                    message = throwable.message
                )
            }
        }
    }
}
