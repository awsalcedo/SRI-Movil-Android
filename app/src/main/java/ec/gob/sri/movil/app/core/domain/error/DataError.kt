package ec.gob.sri.movil.app.core.domain.error

sealed interface DataError : Error {
    sealed interface Network : DataError {
        data class NoInternet(val message: String?) : Network
        data class ClientError(val code: Int, val message: String?) : Network // 4xx
        data class ServerError(val code: Int, val message: String?) : Network // 5xx
        data class Unknown(val message: String?) : Network
    }

    sealed interface Local : DataError {
        data object Database : Local
        data object DiskFull : Local
        data object Unknown : Local
    }

    sealed interface Auth: DataError {
        data object InvalidCredentials : Auth
        data object ServerError : Auth
        data object Timeout : Auth
        data object NoUserID : Auth
        data object Unknown : Auth
        data object InvalidSessionData : Auth
        data object AuthenticationFailed : Auth
        data object RefreshFailed : Auth
    }
}