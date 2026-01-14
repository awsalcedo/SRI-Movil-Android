package ec.gob.sri.movil.feature.estadotributario.data.remote.util

sealed interface DataError : Error {
    sealed interface Network : DataError {
        // HTTP específicos
        sealed interface Http : Network {
            val code: Int
            val body: String?
        }

        data class BadRequest(override val body: String?) : Http {
            override val code = 400
        }

        data class Unauthorized(override val body: String?) : Http {
            override val code = 401
        }

        data class Forbidden(override val body: String?) : Http {
            override val code = 403
        }

        data class NotFound(override val body: String?) : Http {
            override val code = 404
        }

        data class Conflict(override val body: String?) : Http {
            override val code = 409
        }

        data class UnprocessableEntity(override val body: String?) : Http {
            override val code = 422
        }

        data class PayLoadTooLarge(override val body: String?) : Http {
            override val code = 413
        }

        data class RequestTimeout(override val body: String?) : Http {
            override val code = 408
        }

        data class TooManyRequests(override val body: String?) : Http {
            override val code = 429
        }

        data class ServerError(override val code: Int, override val body: String?) : Http // 5xx

        data class UnexpectedHttp(override val code: Int, override val body: String?) : Http

        // Red / otras condiciones

        data object NoInternet : Network

        data class Serialization(
            val cause: Throwable,
            val jsonPath: String? = null
        ) : Network

        data object Unknown : Network
    }

    sealed interface Local : DataError {
        data object Database : Local
        data object DiskFull : Local
        data object Unknown : Local
    }
}