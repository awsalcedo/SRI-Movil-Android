package ec.gob.sri.movil.app.navigation

import android.content.Intent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import ec.gob.sri.movil.app.common.navigation.replaceTop
import ec.gob.sri.movil.app.feature.deudas.navigation.DeudasResultadosScreen
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasConsultaScreen
import ec.gob.sri.movil.app.feature.deudas.ui.resultados.DeudasResultadosRoute
import ec.gob.sri.movil.app.feature.home.ui.HomeScreen
import ec.gob.sri.movil.common.framework.ui.components.SriBottomNavBar
import ec.gob.sri.movil.common.framework.ui.navigation.SriTopLevelNav
import ec.gob.sri.movil.feature.estadotributario.ui.consulta.EstadoTributarioScreen
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavigationRoute.HomeScreen)
    val current = backStack.lastOrNull()

    val showBottomBar = current.isTopLevelDestination()

    // Mientras solo exista Home como top-level, quedará seleccionado Home.
    // Cuando tengas Noticias/Agencias/Login como routes reales, cambias el mapper.
    val selectedBottomId = current.toTopLevelIdOrNull() ?: TopLevelId.HOME

    // Fuente única de verdad para los items top-level (sin Icons aquí).
    val bottomItems = SriTopLevelNav.items



    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                SriBottomNavBar(
                    items = bottomItems,
                    selectedId = selectedBottomId,
                    onItemSelected = { item ->
                        backStack.navigateToTopLevel(item.id)
                    }
                )
            }
        }
    ) { padding ->

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.back() },
            entryProvider = entryProvider {
                routeHomeEntry(backStack)
                routeEstadoTributarioEntry(backStack)
                routeEstadoTributarioDetalleEntry(backStack)
                routeDeudasEntry(backStack)
                routeDeudasResultadosEntry(backStack)

                // routeNoticiasEntry(backStack)
                // routeAgenciasEntry(backStack)
                // routeLoginEntry(backStack)
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
            },
            modifier = Modifier.padding(padding)
        )
    }
}

private fun NavBackStack<NavKey>.navigateToTopLevel(id: String) {
    when (id) {
        TopLevelId.HOME -> navigateTo(NavigationRoute.HomeScreen)

        // Cuando existan:
        // TopLevelId.NOTICIAS -> navigateTo(NavigationRoute.NoticiasScreen)
        // TopLevelId.AGENCIAS -> navigateTo(NavigationRoute.AgenciasScreen)
        // TopLevelId.LOGIN -> navigateTo(NavigationRoute.LoginScreen)

        else -> Unit
    }
}

/**
 * Top-level = donde el bottom bar se mantiene visible.
 * Por ahora solo Home. Luego agregas Noticias/Agencias/Login.
 */
private fun NavKey?.isTopLevelDestination(): Boolean = toTopLevelIdOrNull() != null

/**
 * Mapper de NavKey -> id del bottom bar.
 * Por ahora sólo Home existe como top-level real.
 */
private fun NavKey?.toTopLevelIdOrNull(): String? = when (this) {
    is NavigationRoute.HomeScreen -> TopLevelId.HOME
    // is NavigationRoute.NoticiasScreen -> TopLevelId.NOTICIAS
    // is NavigationRoute.AgenciasScreen -> TopLevelId.AGENCIAS
    // is NavigationRoute.LoginScreen -> TopLevelId.LOGIN
    else -> null
}

private object TopLevelId {
    const val HOME = "home"
    const val NOTICIAS = "noticias"
    const val AGENCIAS = "agencias"
    const val LOGIN = "login"
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
            onNavigateToResultados = { query ->
                backStack.navigateTo(
                    DeudasResultadosScreen(
                        query
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
private fun EntryProviderScope<NavKey>.routeDeudasResultadosEntry(backStack: NavBackStack<NavKey>) {
    entry<DeudasResultadosScreen> { navKey ->
        DeudasResultadosRoute(
            query = navKey.query,
            onBack = {
                backStack.back()
            },
            onNavigate = { nextQuery ->
                backStack.navigateTo(DeudasResultadosScreen(nextQuery))
            }
        )
    }
}

@Composable
private fun EntryProviderScope<NavKey>.routeEstadoTributarioEntry(backStack: NavBackStack<NavKey>) {
    entry<NavigationRoute.EstadoTributarioScreen> {
        EstadoTributarioScreen(
            onNavigateToDetail = { ruc ->
                backStack.navigateTo(
                    NavigationRoute.EstadoTributarioDetalleScreen(
                        ruc
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
