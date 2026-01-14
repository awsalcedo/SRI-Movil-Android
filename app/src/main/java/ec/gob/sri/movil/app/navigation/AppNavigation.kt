package ec.gob.sri.movil.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ec.gob.sri.movil.app.consultas.ui.ConsultasScreen
import ec.gob.sri.movil.app.login.ui.LoginScreen
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.ui.EstadoTributarioScreen
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavigationRoute.EstadoTributarioScreen)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.back() },
        entryProvider = entryProvider {
            routeEstadoTributarioEntry(backStack)
            routeEstadoTributarioDetalleEntry(backStack)
        }
    )
}

@Composable
private fun EntryProviderScope<NavKey>.routeEstadoTributarioEntry(backStack: NavBackStack<NavKey>) {
    entry<NavigationRoute.EstadoTributarioScreen> {
        EstadoTributarioScreen(
            onNavigateToDetail = {
                backStack.navigateTo(
                    NavigationRoute.EstadoTributarioDetalleScreen(
                        it
                    )
                )
            }
        )
    }
}
