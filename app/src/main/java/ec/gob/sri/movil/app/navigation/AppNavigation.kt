package ec.gob.sri.movil.app.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleScreen
import ec.gob.sri.movil.feature.estadotributario.ui.consulta.EstadoTributarioScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavigationRoute.EstadoTributarioScreen)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.back() },
        entryProvider = entryProvider {
            routeEstadoTributarioEntry(backStack)
            routeEstadoTributarioDetalleEntry(backStack)
        },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it }
            ) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it }
            ) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it }
            ) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
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

@Composable
private fun EntryProviderScope<NavKey>.routeEstadoTributarioDetalleEntry(backStack: NavBackStack<NavKey>) {
    entry<NavigationRoute.EstadoTributarioDetalleScreen> {
        EstadoTributarioDetalleScreen(
            ruc = it.ruc,
            onBack = {
                backStack.navigateTo(
                    NavigationRoute.EstadoTributarioScreen
                )
            }
        )
    }
}
