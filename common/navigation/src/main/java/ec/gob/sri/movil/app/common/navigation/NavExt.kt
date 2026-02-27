package ec.gob.sri.movil.app.common.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(screen: NavKey) {
    add(screen)
}

fun NavBackStack<NavKey>.back() {
    if (isEmpty()) return
    removeLastOrNull()
}

fun NavBackStack<NavKey>.backTo(targetScreen: NavKey) {
    if (isEmpty()) return
    if (targetScreen !in this) return

    while (isNotEmpty() && last() != targetScreen) {
        removeLastOrNull()
    }
}

/**
 * Reemplaza el top del back stack por otra key.
 * Útil para flujos "router" donde cambias el estado (query) sin apilar pantallas.
 */
fun NavBackStack<NavKey>.replaceTop(screen: NavKey) {
    if (isEmpty()) {
        add(screen)
        return
    }
    removeLastOrNull()
    add(screen)
}