package ec.gob.sri.movil.app.feature.deudas.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.feature.deudas.domain.usecase.ConsultarPorIdentificacionUseCase
import ec.gob.sri.movil.app.feature.deudas.domain.usecase.GetCachedDeudaPorIdentificacionUseCase
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.framework.ui.error.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeudasDetalleViewModel @Inject constructor(
    private val getCachedDeudaPorIdentificacionUseCase: GetCachedDeudaPorIdentificacionUseCase,
    private val consultarPorIdentificacionUseCase: ConsultarPorIdentificacionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DeudasDetalleState())
    val state = _state.asStateFlow()

    fun load(identificacion: String, tipoPersona: String) {
        // Cache
        getCachedDeudaPorIdentificacionUseCase(identificacion, tipoPersona)?.let { cached ->
            _state.update { it.copy(isLoading = false, data = cached) }
            return
        }

        // Remote
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = consultarPorIdentificacionUseCase(identificacion, tipoPersona)) {
                is DataResult.Error -> _state.update {
                    it.copy(
                        isLoading = false,
                        error = result.error.toUiText()
                    )
                }

                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false, data = result.data) }
                }
            }
        }

    }
}