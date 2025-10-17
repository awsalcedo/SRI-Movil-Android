package ec.gob.sri.movil.app.core.domain

/**
 * Simple Result type for success/failure scenarios
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
