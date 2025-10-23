package ec.gob.sri.movil.app.estadotributario.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import ec.gob.sri.movil.app.estadotributario.domain.usecase.ObtenerEstadoTributarioUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadoTributarioViewModel @Inject constructor(private val estadoTributarioUseCase: ObtenerEstadoTributarioUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(EstadoTributarioState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<EstadoTributarioEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: EstadoTributarioAction) {
        when (action) {
            is EstadoTributarioAction.onConsultaEstadoTributarioClick -> obtenerEstadoTributario(
                action.ruc
            )
        }
    }

    private fun obtenerEstadoTributario(ruc: String) {
        viewModelScope.launch {
            when (val result = estadoTributarioUseCase(ruc)) {
                is DataResult.Error -> {
                    eventChannel.send(EstadoTributarioEvent.OnError(result.error.toString()))
                }

                is DataResult.Success -> {
                    _state.update {
                        it.copy(estadoTributario = result.data)
                    }
                }
            }
        }
    }


}