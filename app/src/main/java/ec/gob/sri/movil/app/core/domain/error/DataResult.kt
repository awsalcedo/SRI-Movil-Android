package ec.gob.sri.movil.app.core.domain.error

sealed class DataResult<out T, out E: DataError> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>()
    data class Error<out E: DataError>(val error: E) : DataResult<Nothing, E>()
}