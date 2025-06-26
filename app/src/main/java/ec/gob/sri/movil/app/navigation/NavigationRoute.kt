package ec.gob.sri.movil.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object ConsultasScreen

@Serializable
object EstadoTributarioScreen

@Serializable
sealed class NavigationRoute {
    @Serializable
    object LoginScreen : NavigationRoute()

    @Serializable
    object ConsultasScreen : NavigationRoute()

    @Serializable
    object EstadoTributarioScreen : NavigationRoute()
}