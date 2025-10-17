package ec.gob.sri.movil.app.core.domain.error

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException

/**
 * Unified error handler for the entire application.
 * Provides consistent error mapping from exceptions to domain errors.
 */
interface ErrorHandler {
    
    /**
     * Handles network-related errors and maps them to appropriate DataError.Network types
     */
    fun handleNetworkError(throwable: Throwable): DataError.Network
    
    /**
     * Handles local storage errors and maps them to appropriate DataError.Local types
     */
    fun handleLocalError(throwable: Throwable): DataError.Local
    
    /**
     * Handles authentication errors and maps them to appropriate DataError.Auth types
     */
    fun handleAuthError(throwable: Throwable): DataError.Auth
    
    /**
     * Handles business logic errors and maps them to appropriate DataError.Business types
     */
    fun handleBusinessError(throwable: Throwable): DataError.Business
    
    /**
     * Parses HTTP error response body for detailed error messages
     */
    fun parseHttpError(exception: HttpException): String
    
    /**
     * Maps HTTP status codes to specific DataError.Network.Http types
     */
    fun mapHttpError(code: Int, body: String? = null, message: String? = null): DataError.Network.Http
    
    /**
     * Generic error handler that attempts to classify any throwable into appropriate DataError type
     */
    fun handleGenericError(throwable: Throwable): DataError
}