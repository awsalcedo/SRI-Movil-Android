package ec.gob.sri.movil.app.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.ui.CommonUiEvent
import ec.gob.sri.movil.app.core.domain.ui.UiEvent
import ec.gob.sri.movil.app.core.domain.ui.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base ViewModel that provides common functionality for all feature ViewModels.
 * Implements the UI State + Events pattern with proper error handling.
 * 
 * @param T The type of UI State
 * @param E The type of UI Events
 */
abstract class BaseViewModel<T : UiState<*>, E : UiEvent> : ViewModel() {
    
    private val _uiState = MutableStateFlow<T>(UiState.Idle as T)
    val uiState: StateFlow<T> = _uiState.asStateFlow()
    
    private val _uiEvents = MutableSharedFlow<E>()
    val uiEvents: SharedFlow<E> = _uiEvents.asSharedFlow()
    
    private val _commonEvents = MutableSharedFlow<CommonUiEvent>()
    val commonEvents: SharedFlow<CommonUiEvent> = _commonEvents.asSharedFlow()
    
    /**
     * Current UI state
     */
    protected val currentState: T
        get() = _uiState.value
    
    /**
     * Updates the UI state
     */
    protected fun updateState(newState: T) {
        _uiState.value = newState
        Timber.d("UI State updated: ${newState::class.simpleName}")
    }
    
    /**
     * Emits a UI event
     */
    protected fun emitEvent(event: E) {
        viewModelScope.launch {
            _uiEvents.emit(event)
            Timber.d("UI Event emitted: ${event::class.simpleName}")
        }
    }
    
    /**
     * Emits a common UI event
     */
    protected fun emitCommonEvent(event: CommonUiEvent) {
        viewModelScope.launch {
            _commonEvents.emit(event)
            Timber.d("Common Event emitted: ${event::class.simpleName}")
        }
    }
    
    /**
     * Handles incoming UI events
     */
    fun handleEvent(event: E) {
        when (event) {
            is CommonUiEvent -> handleCommonEvent(event)
            else -> handleFeatureEvent(event)
        }
    }
    
    /**
     * Handles common UI events that are shared across features
     */
    private fun handleCommonEvent(event: CommonUiEvent) {
        when (event) {
            is CommonUiEvent.DismissError -> dismissError()
            is CommonUiEvent.Retry -> retryLastOperation()
            is CommonUiEvent.NavigateBack -> navigateBack()
            is CommonUiEvent.ClearData -> clearData()
        }
    }
    
    /**
     * Handles feature-specific events - must be implemented by subclasses
     */
    protected abstract fun handleFeatureEvent(event: E)
    
    /**
     * Dismisses current error state
     */
    protected open fun dismissError() {
        // Default implementation - can be overridden by subclasses
        Timber.d("Dismissing error state")
    }
    
    /**
     * Retries the last operation - must be implemented by subclasses
     */
    protected abstract fun retryLastOperation()
    
    /**
     * Handles navigation back - can be overridden by subclasses
     */
    protected open fun navigateBack() {
        Timber.d("Navigate back requested")
    }
    
    /**
     * Clears all data and resets to initial state - can be overridden by subclasses
     */
    protected open fun clearData() {
        updateState(UiState.Idle as T)
        Timber.d("Data cleared, reset to initial state")
    }
    
    /**
     * Sets loading state
     */
    protected fun setLoading() {
        updateState(UiState.Loading as T)
    }
    
    /**
     * Sets error state with the given error
     */
    protected fun setError(error: DataError, message: String? = null) {
        updateState(UiState.Error(error, message) as T)
        Timber.e("Error state set: ${error::class.simpleName} - $message")
    }
    
    /**
     * Sets success state with data
     */
    protected fun <D> setSuccess(data: D) {
        updateState(UiState.Success(data) as T)
        Timber.d("Success state set with data: ${data::class.simpleName}")
    }
    
    /**
     * Executes an operation with automatic state management
     */
    protected fun <D> executeOperation(
        operation: suspend () -> D,
        onSuccess: (D) -> Unit = { setSuccess(it) },
        onError: (DataError, String?) -> Unit = { error, message -> setError(error, message) },
        showLoading: Boolean = true
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) setLoading()
                
                val result = operation()
                onSuccess(result)
                
            } catch (e: Exception) {
                Timber.e(e, "Operation failed")
                val error = mapExceptionToDataError(e)
                onError(error, e.message)
            }
        }
    }
    
    /**
     * Maps exceptions to DataError - can be overridden by subclasses for specific mapping
     */
    protected open fun mapExceptionToDataError(exception: Exception): DataError {
        return when (exception) {
            is IllegalArgumentException -> DataError.Business.InvalidInput
            is IllegalStateException -> DataError.Business.OperationNotAllowed
            else -> DataError.Network.Unknown(cause = exception, message = exception.message)
        }
    }
}
