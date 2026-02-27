package ec.gob.sri.movil.app.feature.deudas.ui.coincidencias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.feature.deudas.domain.usecase.ConsultarDeudasPorNombreUseCase
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.framework.ui.error.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeudasCoincidenciasViewModel @Inject constructor(
    private val consultarPorNombreUseCase: ConsultarDeudasPorNombreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DeudasCoincidenciasUiState())
    val state = _state.asStateFlow()

    fun load(razonSocial: String, tipoPersona: String, resultados: Int) {
        val current = _state.value
        if (current.isLoading) return

        val normalized = razonSocial.trim().replace(Regex("\\s+"), " ")
        if (normalized.isBlank()) {
            _state.update { it.copy(error = null, items = emptyList(), razonSocial = "") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, razonSocial = normalized) }

            when (val result = consultarPorNombreUseCase(
                nombre = normalized,
                tipoPersona = tipoPersona,
                resultados = resultados
            )) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error.toUiText()) }
                }

                is DataResult.Success -> {
                    val uiItems = result.data.map { domain ->
                        // ✅ Ajusta aquí si tu domain tiene otros nombres de campos
                        DeudasCoincidenciaItemUi(
                            identificacion = domain.identificacion,
                            titulo = domain.nombreComercial.ifBlank { "-" },
                            subtitulo = buildString {
                                append(domain.clase)
                                append(" · ")
                                append(domain.tipoIdentificacion)
                            }
                        )
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            items = uiItems
                        )
                    }
                }
            }
        }
    }
}