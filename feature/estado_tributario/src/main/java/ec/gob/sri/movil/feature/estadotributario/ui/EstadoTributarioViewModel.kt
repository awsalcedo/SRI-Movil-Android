package ec.gob.sri.movil.feature.estadotributario.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataError
import ec.gob.sri.movil.app.estadotributario.data.remote.util.DataResult
import ec.gob.sri.movil.app.estadotributario.domain.usecase.EstadoTributarioUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadoTributarioViewModel @Inject constructor(private val estadoTributarioUseCase: EstadoTributarioUseCase) :
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
            _state.update { it.copy(isLoading = true) }
            when (val result = estadoTributarioUseCase.obtenerEstadoTributarioUseCase(ruc)) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(EstadoTributarioEvent.OnError(result.error.toString()))
                }

                is DataResult.Success -> {
                    _state.update {
                        it.copy(estadoTributario = result.data, isLoading = false)
                    }
                    eventChannel.send(EstadoTributarioEvent.OnNavigateDetail(estadoTributario = result.data))
                }
            }
        }
    }

    private fun getErrorMessage(error: DataError.Network): String = when (error) {
        is DataError.Network.BadRequest -> TODO()
        is DataError.Network.Conflict -> TODO()
        is DataError.Network.Forbidden -> TODO()
        is DataError.Network.NotFound -> TODO()
        is DataError.Network.PayLoadTooLarge -> TODO()
        is DataError.Network.RequestTimeout -> TODO()
        is DataError.Network.ServerError -> TODO()
        is DataError.Network.TooManyRequests -> TODO()
        is DataError.Network.Unauthorized -> TODO()
        is DataError.Network.UnexpectedHttp -> TODO()
        is DataError.Network.UnprocessableEntity -> TODO()
        DataError.Network.NoInternet -> TODO()
        is DataError.Network.Serialization -> TODO()
        DataError.Network.Unknown -> TODO()
    }

}