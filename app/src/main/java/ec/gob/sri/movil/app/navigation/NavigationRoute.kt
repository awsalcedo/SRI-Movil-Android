package ec.gob.sri.movil.app.navigation

import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object ConsultasScreen

@Serializable
object EstadoTributarioScreen

@Serializable
object EstadoTributarioDetalleScreen

@Serializable
object MatriculacionVehicularScreen


sealed class NavigationRoute {
    @Serializable
    data object LoginScreen : NavigationRoute()

    @Serializable
    data object ConsultasScreen : NavigationRoute()

    @Serializable
    data object EstadoTributarioScreen : NavigationRoute()

    @Serializable
    data class EstadoTributarioDetalleScreen(val estadoTributario: String) : NavigationRoute()

    @Serializable
    data object MatriculacionVehicularScreen : NavigationRoute()
}