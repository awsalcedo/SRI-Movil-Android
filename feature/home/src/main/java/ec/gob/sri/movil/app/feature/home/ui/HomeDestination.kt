package ec.gob.sri.movil.app.feature.home.ui

import ec.gob.sri.movil.app.common.navigation.NavigationRoute

sealed interface HomeDestination {
    data class Internal(val route: NavigationRoute) : HomeDestination
    data class External(val url: String) : HomeDestination
    data object None : HomeDestination
}