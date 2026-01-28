package ec.gob.sri.movil.feature.estadotributario.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.framework.ui.error.toUiText
import ec.gob.sri.movil.feature.estadotributario.domain.models.ObligacionesPendientesDomain
import ec.gob.sri.movil.feature.estadotributario.domain.usecase.GetCachedEstadoTributarioUseCase
import ec.gob.sri.movil.feature.estadotributario.domain.usecase.ObtenerEstadoTributarioUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadoTributarioDetalleViewModel @Inject constructor(
    private val getCachedEstadoTributarioUseCase: GetCachedEstadoTributarioUseCase,
    private val obtenerEstadoTributarioUseCase: ObtenerEstadoTributarioUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EstadoTributarioDetalleState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<EstadoTributarioDetalleEvent>(capacity = Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private var lastLoadedRuc: String? = null

    fun onAction(action: EstadoTributarioDetalleAction) {
        when (action) {
            is EstadoTributarioDetalleAction.OnLoad -> load(action.ruc)
        }
    }

    private fun load(ruc: String, forceRemote: Boolean = false) {
        // Evita repetir SOLO si es el mismo ruc y no se fuerza remoto
        if (!forceRemote && lastLoadedRuc == ruc && _state.value.estadoTributario != null) return

        if (_state.value.isLoading) return

        lastLoadedRuc = ruc

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            if (!forceRemote) {
                val cache = getCachedEstadoTributarioUseCase(ruc)
                if (cache != null) {
                    _state.update { it.copy(estadoTributario = cache, isLoading = false) }
                    return@launch
                }
            }

            // Si no existe cache consulta remoto
            when (val result = obtenerEstadoTributarioUseCase(ruc)) {
                is DataResult.Success -> {
                    _state.update {
                        it.copy(estadoTributario = result.data, isLoading = false)
                    }
                }

                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(EstadoTributarioDetalleEvent.OnError(result.error.toUiText()))
                }
            }
        }
    }

    /**
     * Se llama cuando el usuario hace clic en una obligación en la lista.
     * Actualiza el estado para mostrar el BottomSheet.
     */
    fun onObligacionClick(obligacion: ObligacionesPendientesDomain) {
        _state.update { it.copy(obligacionSeleccionada = obligacion) }
    }

    /**
     * Se llama cuando el usuario cierra el BottomSheet.
     * Limpia la selección para ocultarlo.
     */
    fun onDismissObligacion() {
        _state.update { it.copy(obligacionSeleccionada = null) }
    }
}