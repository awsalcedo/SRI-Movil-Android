package ec.gob.sri.movil.common.framework.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observa un [Flow] de eventos de una sola vez (one-shot events) de forma segura con el lifecycle.
 *
 * Uso típico:
 * - Mostrar Snackbar/Toast
 * - Navegación
 * - Diálogos
 *
 * Características:
 * - Solo colecciona cuando el lifecycle está al menos en STARTED.
 * - Recolecta en Main.immediate (requerido para operaciones de UI).
 * - Permite reiniciar la colección cuando cambian [key1]/[key2] (útil si quieres "resetear" el collector).
 *
 * @param flow Flow de eventos (por ejemplo, `events` expuesto por un ViewModel).
 * @param key1 Clave opcional para controlar reinicios de la colección.
 * @param key2 Clave opcional para controlar reinicios de la colección.
 * @param onEvent Acción suspend que se ejecuta por cada evento emitido.
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = lifecycleOwner, key2 = key1, key3 = key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}