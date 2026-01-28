package ec.gob.sri.movil.feature.estadotributario.ui.consulta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.framework.ui.error.toUiText
import ec.gob.sri.movil.common.framework.ui.text.UiText
import ec.gob.sri.movil.feature.estadotributario.R
import ec.gob.sri.movil.feature.estadotributario.domain.usecase.ObtenerEstadoTributarioUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadoTributarioViewModel @Inject constructor(
    private val obtenerEstadoTributarioUseCase: ObtenerEstadoTributarioUseCase
) :
    ViewModel() {

    companion object {
        private const val MAX_LENGTH_RUC = 13
    }

    private val _state = MutableStateFlow(EstadoTributarioState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<EstadoTributarioEvent>(capacity = Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: EstadoTributarioAction) {
        when (action) {
            is EstadoTributarioAction.onRucChanged -> onRucChanged(action.ruc)
            is EstadoTributarioAction.OnConsultarClick -> onConsultarClick()
        }
    }

    private fun onRucChanged(ruc: String) {
        val normalizado = ruc.onlyDigits().take(MAX_LENGTH_RUC)
        val longitudRucvalida = isLongitudRucValido(normalizado)

        _state.update { it.copy(ruc = normalizado, isRucValid = longitudRucvalida) }
    }

    private fun onConsultarClick() {
        val currentState = _state.value

        if (currentState.isLoading) return

        if (!currentState.isRucValid) {
            viewModelScope.launch {
                eventChannel.send(
                    EstadoTributarioEvent.OnError(UiText.StringResource(R.string.ruc_invalido))
                )
            }
            return
        }

        obtenerEstadoTributario(currentState.ruc)
    }

    private fun obtenerEstadoTributario(ruc: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = obtenerEstadoTributarioUseCase(ruc)) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(EstadoTributarioEvent.OnError(result.error.toUiText()))
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

    private fun String.onlyDigits(): String = filter {
        it.isDigit()
    }

    private fun isLongitudRucValido(ruc: String): Boolean {
        return ruc.length == 13
    }
}