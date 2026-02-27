package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.feature.deudas.domain.usecase.ConsultarDeudasPorNombreUseCase
import ec.gob.sri.movil.app.feature.deudas.domain.usecase.ConsultarPorIdentificacionUseCase
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.framework.ui.error.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeudasConsultaViewModel @Inject constructor(
    private val consultarPorIdentificacionUseCase: ConsultarPorIdentificacionUseCase,
    private val consultarDeudasPorNombreUseCase: ConsultarDeudasPorNombreUseCase
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

            is DeudasConsultaAction.RazonSocialChanged -> {
                _state.update { it.copy(razonSocialInput = action.value) }
            }

            is DeudasConsultaAction.TipoContribuyenteSelected -> {
                _state.update { s ->
                    val newTipo = action.value

                    val allowed = when (newTipo) {
                        ContribuyenteType.PERSONA_JURIDICA -> setOf(IdType.RUC, IdType.RAZON_SOCIAL)
                        ContribuyenteType.PERSONA_NATURAL -> setOf(
                            IdType.CEDULA,
                            IdType.RUC,
                            IdType.APELLIDOS_NOMBRES
                        )
                    }

                    val fixedIdType = if (s.idType in allowed) s.idType else allowed.first()

                    s.copy(
                        tipoContribuyente = newTipo,
                        idType = fixedIdType,

                        // limpiar lo que ya no aplica
                        ruc = if (fixedIdType == IdType.RUC) s.ruc else "",
                        cedula = if (fixedIdType == IdType.CEDULA) s.cedula else "",
                        apellidos = if (fixedIdType == IdType.APELLIDOS_NOMBRES) s.apellidos else "",
                        nombres = if (fixedIdType == IdType.APELLIDOS_NOMBRES) s.nombres else "",
                        razonSocialInput = if (fixedIdType == IdType.RAZON_SOCIAL) s.razonSocialInput else ""
                    )
                }
            }

            is DeudasConsultaAction.IdTypeSelected -> {
                _state.update {
                    it.copy(
                        idType = action.value,
                        ruc = if (action.value == IdType.RUC) it.ruc else "",
                        cedula = if (action.value == IdType.CEDULA) it.cedula else "",
                        apellidos = if (action.value == IdType.APELLIDOS_NOMBRES) it.apellidos else "",
                        nombres = if (action.value == IdType.APELLIDOS_NOMBRES) it.nombres else "",
                        razonSocialInput = if (action.value == IdType.RAZON_SOCIAL) it.razonSocialInput else ""
                    )
                }
            }

            DeudasConsultaAction.ConsultarClicked -> consulta()
        }
    }

    private fun consulta() {
        val current = _state.value

        if (current.isLoading || !current.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (current.idType) {
                IdType.RUC, IdType.CEDULA -> consultarPorIdentificacion(current)
                IdType.APELLIDOS_NOMBRES, IdType.RAZON_SOCIAL -> consultarPorDenominacion(current)
            }
        }
    }

    private suspend fun consultarPorIdentificacion(current: DeudasConsultaUiState) {
        val identificacion = if (current.idType == IdType.RUC) current.ruc else current.cedula
        val tipoPersona = current.tipoPersona

        when (val result = consultarPorIdentificacionUseCase(
            identificacion = identificacion,
            tipoPersona = tipoPersona
        )) {
            is DataResult.Error -> {
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(DeudasConsultaEvent.OnError(result.error.toUiText()))
            }

            is DataResult.Success -> {
                _state.update { it.copy(isLoading = false) }

                // Repository ya cachea el detalle (last + lastKey), así que detalle no reconsulta
                _eventChannel.trySend(
                    DeudasConsultaEvent.NavigateToResultados(
                        DeudasQuery.PorIdentificacion(
                            contribuyenteType = current.tipoContribuyente,
                            idType = current.idType,
                            identificacion = identificacion,
                            tipoPersona = tipoPersona
                        )
                    )
                )
            }
        }
    }

    private suspend fun consultarPorDenominacion(current: DeudasConsultaUiState) {
        val tipoPersona = current.tipoPersona
        val razonSocial = current.razonSocial
        val resultados = 30

        when (val result = consultarDeudasPorNombreUseCase(
            nombre = razonSocial,
            tipoPersona = tipoPersona,
            resultados = resultados
        )) {
            is DataResult.Error -> {
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(DeudasConsultaEvent.OnError(result.error.toUiText()))
            }

            is DataResult.Success -> {
                _state.update { it.copy(isLoading = false) }

                _eventChannel.trySend(
                    DeudasConsultaEvent.NavigateToResultados(
                        DeudasQuery.PorDenominacion(
                            contribuyenteType = current.tipoContribuyente,
                            apellidos = current.apellidos,
                            nombres = current.nombres,
                            razonSocial = razonSocial,
                            tipoPersona = tipoPersona,
                            resultados = resultados
                        )
                    )
                )
            }
        }
    }
}