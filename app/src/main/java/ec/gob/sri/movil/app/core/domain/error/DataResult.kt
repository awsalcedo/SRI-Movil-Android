package ec.gob.sri.movil.app.core.domain.error

/**
 * Unified Result type for the entire application.
 * Represents either a successful operation with data or a failure with an error.
 * 
 * @param T The type of data returned on success
 * @param E The type of error returned on failure (must extend DataError)
 */
sealed interface DataResult<out T, out E : DataError> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>
    data class Error<out E : DataError>(val error: E) : DataResult<Nothing, E>
    
    /**
     * Returns true if this is a Success result
     */
    val isSuccess: Boolean
        get() = this is Success
    
    /**
     * Returns true if this is an Error result
     */
    val isError: Boolean
        get() = this is Error
    
    /**
     * Returns the data if this is a Success, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }
    
    /**
     * Returns the error if this is an Error, null otherwise
     */
    fun getErrorOrNull(): E? = when (this) {
        is Success -> null
        is Error -> error
    }
    
    /**
     * Transforms the success data using the given function
     */
    inline fun <R> map(transform: (T) -> R): DataResult<R, E> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }
    
    /**
     * Transforms the error using the given function
     */
    inline fun <R> mapError(transform: (E) -> R): DataResult<T, R> = when (this) {
        is Success -> this
        is Error -> Error(transform(error))
    }
    
    /**
     * Chains another operation if this is a Success
     */
    inline fun <R> flatMap(transform: (T) -> DataResult<R, E>): DataResult<R, E> = when (this) {
        is Success -> transform(data)
        is Error -> this
    }
    
    /**
     * Executes the appropriate lambda based on the result type
     */
    inline fun <R> fold(
        onSuccess: (T) -> R,
        onError: (E) -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError(error)
    }
}