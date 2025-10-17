package ec.gob.sri.movil.app.core.domain

/**
 * Simple Result type for success/failure scenarios
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

/**
 * DataResult type like Philip Lackner's implementation
 * More specific than Result, allows typed errors
 */
sealed class DataResult<out T, out E> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>()
    data class Error<out E>(val error: E) : DataResult<Nothing, E>()
}
