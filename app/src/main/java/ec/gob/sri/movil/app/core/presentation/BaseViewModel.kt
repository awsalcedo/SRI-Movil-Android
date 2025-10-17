package ec.gob.sri.movil.app.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.gob.sri.movil.app.core.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Simple base ViewModel with loading and error states
 */
abstract class BaseViewModel : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
    
    protected fun setError(message: String?) {
        _errorMessage.value = message
    }
    
    protected fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * Execute a suspend operation with automatic loading and error handling
     */
    protected fun <T> executeOperation(
        operation: suspend () -> Result<T>,
        onSuccess: (T) -> Unit = {},
        onError: (String) -> Unit = { setError(it) }
    ) {
        viewModelScope.launch {
            try {
                setLoading(true)
                clearError()
                
                when (val result = operation()) {
                    is Result.Success -> onSuccess(result.data)
                    is Result.Error -> onError(result.exception.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            } finally {
                setLoading(false)
            }
        }
    }
}