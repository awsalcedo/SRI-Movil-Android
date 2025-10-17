package ec.gob.sri.movil.app.estadotributario.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.core.domain.DataResult
import ec.gob.sri.movil.app.core.domain.ErrorHandler
import ec.gob.sri.movil.app.core.presentation.BaseViewModel
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.app.estadotributario.domain.usecase.ObtenerEstadoTributarioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EstadoTributarioViewModel @Inject constructor(
    private val estadoTributarioUseCase: ObtenerEstadoTributarioUseCase,
    private val errorHandler: ErrorHandler
) : BaseViewModel() {
    
    private val _estadoTributario = MutableStateFlow<EstadoTributarioDomain?>(null)
    val estadoTributario: StateFlow<EstadoTributarioDomain?> = _estadoTributario.asStateFlow()
    
    fun consultarEstadoTributario(context: Context, ruc: String) {
        executeDataOperation(
            operation = { estadoTributarioUseCase(ruc) },
            onSuccess = { data ->
                _estadoTributario.value = data
            },
            onError = { dataError ->
                val errorMessage = errorHandler.getErrorMessage(context, dataError)
                setError(errorMessage)
            }
        )
    }
    
    fun clearError() {
        setError(null)
    }
}