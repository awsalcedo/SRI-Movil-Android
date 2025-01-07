package ec.gob.sri.movil.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object ConsultasScreen

@Serializable
object EstadoTributarioScreen

@Serializable
sealed class NavigationRoute {
    @Serializable
    object ConsultasScreen : NavigationRoute()

    @Serializable
    object EstadoTributarioScreen : NavigationRoute()
}