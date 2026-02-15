package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DeudasConsultaViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(DeudasConsultaUiState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<DeudasConsultaEvent>(capacity = Channel.BUFFERED)
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: DeudasConsultaAction) {
        when (action) {
            is DeudasConsultaAction.ApellidosChanged -> {
                _state.update { it.copy(apellidos = action.value) }
            }

            is DeudasConsultaAction.CedulaChanged -> {
                _state.update { it.copy(cedula = action.value) }
            }

            is DeudasConsultaAction.NombresChanged -> {
                _state.update { it.copy(nombres = action.value) }
            }

            is DeudasConsultaAction.RucChanged -> {
                _state.update { it.copy(ruc = action.value) }
            }
            is DeudasConsultaAction.TipoContribuyenteSelected -> {
                _state.update { it.copy(tipoContribuyente = action.value) }
            }

            DeudasConsultaAction.BackClicked -> {
                _eventChannel.trySend(DeudasConsultaEvent.NavigateBack)
            }

            is DeudasConsultaAction.IdTypeSelected -> {
                _state.update {
                    it.copy(
                        idType = action.value,
                        ruc = if (action.value == IdType.RUC) it.ruc else "",
                        cedula = if (action.value == IdType.CEDULA) it.cedula else "",
                        apellidos = if (action.value == IdType.APELLIDOS_NOMBRES) it.apellidos else "",
                        nombres = if (action.value == IdType.APELLIDOS_NOMBRES) it.nombres else ""
                    )
                }
            }

            DeudasConsultaAction.ConsultarClicked -> consulta()
        }
    }

    private fun consulta() {

    }
}