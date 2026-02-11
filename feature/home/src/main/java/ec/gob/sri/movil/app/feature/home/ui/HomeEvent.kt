package ec.gob.sri.movil.app.feature.home.ui

import ec.gob.sri.movil.app.common.navigation.NavigationRoute
import ec.gob.sri.movil.common.framework.ui.text.UiText

sealed interface HomeEvent {
    data class NavigateTo(val route: NavigationRoute) : HomeEvent
    data class OpenUrl(val url: String) : HomeEvent
    data class OnError(val message: UiText) : HomeEvent
}