package ec.gob.sri.movil.app.navigation

import android.content.Intent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ec.gob.sri.movil.app.common.navigation.NavigationRoute
import ec.gob.sri.movil.app.common.navigation.back
import ec.gob.sri.movil.app.common.navigation.navigateTo
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasConsultaEvent
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasConsultaScreen
import ec.gob.sri.movil.app.feature.home.ui.HomeScreen
import ec.gob.sri.movil.feature.estadotributario.ui.consulta.EstadoTributarioScreen
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavigationRoute.HomeScreen)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.back() },
        entryProvider = entryProvider {
            routeHomeEntry(backStack)
            routeEstadoTributarioEntry(backStack)
            routeEstadoTributarioDetalleEntry(backStack)
            routeDeudasEntry(backStack)
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
private fun EntryProviderScope<NavKey>.routeHomeEntry(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    entry<NavigationRoute.HomeScreen> {
        HomeScreen(
            onNavigate = { route -> backStack.navigateTo(route) },
            openUrl = { url ->
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
        )
    }
}

@Composable
private fun EntryProviderScope<NavKey>.routeDeudasEntry(backStack: NavBackStack<NavKey>) {
    entry<NavigationRoute.DeudasScreen> {
        DeudasConsultaScreen(
            onEvent = { event ->
                when (event) {
                    DeudasConsultaEvent.NavigateBack -> {backStack.navigateTo(NavigationRoute.HomeScreen)}
                    is DeudasConsultaEvent.NavigateToResultados -> TODO()
                    is DeudasConsultaEvent.ShowSnackbar -> TODO()
                }

            }
        )
    }
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
            },
            onBack = {
                backStack.navigateTo(
                    NavigationRoute.HomeScreen
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
