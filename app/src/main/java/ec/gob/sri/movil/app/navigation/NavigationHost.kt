package ec.gob.sri.movil.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.gob.sri.movil.app.consultas.ui.ConsultasScreen
import ec.gob.sri.movil.app.estadotributario.ui.EstadoTributarioScreen
import ec.gob.sri.movil.app.login.ui.LoginScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: NavigationRoute = NavigationRoute.ConsultasScreen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        loginScreen(navController = navController)
        consultasScreen(navController = navController)
        estadoTributarioScreen(navController = navController)
    }
}

fun NavGraphBuilder.loginScreen(navController: NavHostController) {
    composable<NavigationRoute.LoginScreen> {
        LoginScreen()
    }
}

fun NavGraphBuilder.consultasScreen(navController: NavHostController) {
    composable<NavigationRoute.ConsultasScreen> {
        ConsultasScreen()
    }
}

fun NavGraphBuilder.estadoTributarioScreen(navController: NavHostController) {
    composable<NavigationRoute.EstadoTributarioScreen> {
        EstadoTributarioScreen()
    }
}