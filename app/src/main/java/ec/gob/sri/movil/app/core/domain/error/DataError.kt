package ec.gob.sri.movil.app.core.domain.error

/**
 * Unified error hierarchy for the entire application.
 * Provides comprehensive error classification for different layers and scenarios.
 */
sealed interface DataError : Error {
    
    /**
     * Network-related errors including HTTP errors, connectivity issues, and timeouts
     */
    sealed interface Network : DataError {
        
        /**
         * HTTP-specific errors with detailed status codes and response bodies
         */
        sealed interface Http : Network {
            val code: Int
            val body: String?
            val message: String?
            
            // 4xx Client Errors
            data class BadRequest(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 400
            }
            
            data class Unauthorized(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 401
            }
            
            data class Forbidden(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 403
            }
            
            data class NotFound(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 404
            }
            
            data class Conflict(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 409
            }
            
            data class UnprocessableEntity(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 422
            }
            
            data class TooManyRequests(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 429
            }
            
            // 5xx Server Errors
            data class InternalServerError(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 500
            }
            
            data class BadGateway(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 502
            }
            
            data class ServiceUnavailable(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 503
            }
            
            data class GatewayTimeout(
                override val body: String? = null,
                override val message: String? = null
            ) : Http {
                override val code = 504
            }
            
            // Generic HTTP error for unexpected codes
            data class UnexpectedHttp(
                override val code: Int,
                override val body: String? = null,
                override val message: String? = null
            ) : Http
        }
        
        // Network connectivity and transport errors
        data object NoInternet : Network
        data object Timeout : Network
        data class Serialization(
            val cause: Throwable,
            val jsonPath: String? = null,
            val message: String? = null
        ) : Network
        
        data class Unknown(
            val cause: Throwable? = null,
            val message: String? = null
        ) : Network
    }

    /**
     * Local data storage errors (Room, SharedPreferences, File System)
     */
    sealed interface Local : DataError {
        data object Database : Local
        data object DiskFull : Local
        data object CorruptedData : Local
        data object DataNotFound : Local
        data object WriteFailed : Local
        data object ReadFailed : Local
        
        data class Unknown(
            val cause: Throwable? = null,
            val message: String? = null
        ) : Local
    }

    /**
     * Authentication and authorization errors
     */
    sealed interface Auth : DataError {
        data object InvalidCredentials : Auth
        data object TokenExpired : Auth
        data object InvalidToken : Auth
        data object NoUserID : Auth
        data object InvalidSessionData : Auth
        data object AuthenticationFailed : Auth
        data object RefreshFailed : Auth
        data object UnauthorizedAccess : Auth
        
        data class Unknown(
            val cause: Throwable? = null,
            val message: String? = null
        ) : Auth
    }
    
    /**
     * Business logic and validation errors
     */
    sealed interface Business : DataError {
        data object InvalidInput : Business
        data object ValidationFailed : Business
        data object ResourceNotFound : Business
        data object ResourceAlreadyExists : Business
        data object OperationNotAllowed : Business
        data object InsufficientPermissions : Business
        
        data class Unknown(
            val cause: Throwable? = null,
            val message: String? = null
        ) : Business
    }
}