package ec.gob.sri.movil.app.core.domain

/**
 * DataError with specific HTTP error types for better error handling
 */
sealed interface DataError {
    sealed interface Network : DataError {
        // HTTP specific errors
        data class BadRequest(val body: String?) : Network
        data class Unauthorized(val body: String?) : Network
        data class Forbidden(val body: String?) : Network
        data class NotFound(val body: String?) : Network
        data class Conflict(val body: String?) : Network
        data class UnprocessableEntity(val body: String?) : Network
        data class PayLoadTooLarge(val body: String?) : Network
        data class RequestTimeout(val body: String?) : Network
        data class TooManyRequests(val body: String?) : Network
        data class ServerError(val code: Int, val body: String?) : Network
        data class UnexpectedHttp(val code: Int, val body: String?) : Network
        
        // Network connectivity errors
        data object NoInternet : Network
        data object Timeout : Network
        data object Unknown : Network
    }
    
    sealed interface Local : DataError {
        data object Database : Local
        data object Unknown : Local
    }
}
