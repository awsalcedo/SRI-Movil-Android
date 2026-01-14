package ec.gob.sri.movil.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute: NavKey {
    @Serializable
    data object LoginScreen: NavigationRoute()

    @Serializable
    data object ConsultasScreen: NavigationRoute()

    @Serializable
    data object EstadoTributarioScreen: NavigationRoute()

    @Serializable
    data object EstadoTributarioDetalleScreen: NavigationRoute()

    @Serializable
    data object MatriculacionVehicularScreen: NavigationRoute()
}