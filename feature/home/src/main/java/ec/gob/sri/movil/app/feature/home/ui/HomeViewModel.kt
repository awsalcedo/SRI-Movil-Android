package ec.gob.sri.movil.app.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.common.navigation.NavigationRoute
import ec.gob.sri.movil.common.framework.ui.text.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnBottomMenuClick -> TODO()
            is HomeAction.OnHomeItemClick -> handleHomeItemClick(action.id)
            HomeAction.OnLoad -> load()
        }
    }

    private fun load() {
        if (_state.value.items.isNotEmpty()) return
        _state.update { it.copy(isLoading = false, items = HomeMenuDefaults.build()) }
    }

    private fun handleHomeItemClick(id: String) {
        if (_state.value.isLoading) return

        val item = _state.value.items.firstOrNull { it.id == id }
        if (item == null) {
            viewModelScope.launch {
                eventChannel.send(HomeEvent.OnError(UiText.DynamicString("Opción no encontrada")))
            }
            return
        }

        if (!item.enabled) {
            viewModelScope.launch {
                eventChannel.send(
                    HomeEvent.OnError(
                        UiText.DynamicString(
                            item.enabledMessage ?: "Opción no disponible"
                        )
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            when (id) {
                "estado_tributario" -> eventChannel.send(HomeEvent.NavigateTo(NavigationRoute.EstadoTributarioScreen))
                "cita_previa" -> eventChannel.send(HomeEvent.OpenUrl("https://srienlinea.sri.gob.ec/turnos-internet-web/publico/menu.jsf"))
            }
        }
    }
}