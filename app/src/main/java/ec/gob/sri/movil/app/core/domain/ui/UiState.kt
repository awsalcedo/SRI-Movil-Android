package ec.gob.sri.movil.app.core.domain.ui

import ec.gob.sri.movil.app.core.domain.error.DataError

/**
 * Base UI State interface that all feature UI states should implement.
 * Provides common loading, error, and success states.
 */
sealed interface UiState<out T> {
    
    /**
     * Initial state when the UI is first created
     */
    data object Idle : UiState<Nothing>
    
    /**
     * Loading state while an operation is in progress
     */
    data object Loading : UiState<Nothing>
    
    /**
     * Success state with data
     */
    data class Success<out T>(val data: T) : UiState<T>
    
    /**
     * Error state with error information
     */
    data class Error(val error: DataError, val message: String? = null) : UiState<Nothing>
}

/**
 * Extension to check if the state is in a loading condition
 */
val <T> UiState<T>.isLoading: Boolean
    get() = this is UiState.Loading

/**
 * Extension to check if the state has data (Success)
 */
val <T> UiState<T>.hasData: Boolean
    get() = this is UiState.Success

/**
 * Extension to check if the state has an error
 */
val <T> UiState<T>.hasError: Boolean
    get() = this is UiState.Error

/**
 * Extension to get the data if available, null otherwise
 */
fun <T> UiState<T>.getDataOrNull(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}

/**
 * Extension to get the error if available, null otherwise
 */
fun <T> UiState<T>.getErrorOrNull(): UiState.Error? = when (this) {
    is UiState.Error -> this
    else -> null
}

/**
 * Extension to transform the data in a Success state
 */
fun <T, R> UiState<T>.mapData(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Success -> UiState.Success(transform(data))
    is UiState.Idle -> UiState.Idle
    is UiState.Loading -> UiState.Loading
    is UiState.Error -> this
}

/**
 * Extension to transform the error in an Error state
 */
fun <T> UiState<T>.mapError(transform: (DataError) -> DataError): UiState<T> = when (this) {
    is UiState.Success -> this
    is UiState.Idle -> this
    is UiState.Loading -> this
    is UiState.Error -> UiState.Error(transform(error), message)
}
