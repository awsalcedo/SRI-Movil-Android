package ec.gob.sri.movil.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object ConsultasScreen

@Serializable
object EstadoTributarioScreen

@Serializable
object MatriculacionVehicularScreen


sealed class NavigationRoute {
    @Serializable
    data object LoginScreen: NavigationRoute()

    @Serializable
    data object ConsultasScreen: NavigationRoute()

    @Serializable
    data object EstadoTributarioScreen: NavigationRoute()

    @Serializable
    data object MatriculacionVehicularScreen: NavigationRoute()
}