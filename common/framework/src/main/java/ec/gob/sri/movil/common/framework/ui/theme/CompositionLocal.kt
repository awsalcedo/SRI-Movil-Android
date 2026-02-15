package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal que expone los tokens de dimensión (spacing, tamaños, alturas mínimas, etc.)
 * del Design System SRI a lo largo del árbol de composición.
 *
 * ¿Qué es?
 * ----------
 * `LocalSriDimens` es el mecanismo mediante el cual el Theme provee una instancia de [SriDimens]
 * a todos los composables hijos, permitiendo acceder a valores consistentes de spacing y layout
 * sin depender de constantes hardcodeadas (ej: 16.dp, 12.dp, etc.).
 *
 * ¿Por qué se usa?
 * ----------------
 * En aplicaciones profesionales con Design System, los valores de espaciado deben:
 *
 * - Ser centralizados.
 * - Ser consistentes en toda la aplicación.
 * - Poder variar según configuración (modo compacto, tablet, accesibilidad, etc.).
 * - Ser fácilmente testeables y reemplazables en previews.
 *
 * `CompositionLocal` permite que estos valores sean:
 *
 * - Inyectados por el Theme (SRIAppTheme).
 * - Sobrescritos en contextos específicos (por ejemplo, en previews o variantes compactas).
 * - Independientes de singletons globales.
 *
 * ¿Qué problema evita?
 * ---------------------
 * Evita el uso repetido de valores mágicos como:
 *
 *     padding(16.dp)
 *     Arrangement.spacedBy(12.dp)
 *
 * Reemplazándolos por:
 *
 *     val dimens = SRITheme.dimens
 *     padding(dimens.screenPadding)
 *
 * Lo cual mejora:
 * - Mantenibilidad.
 * - Escalabilidad.
 * - Coherencia visual.
 * - Evolución futura del Design System.
 *
 * Ejemplo de uso:
 * ----------------
 * Dentro de un Composable:
 *
 *     val dimens = SRITheme.dimens
 *
 *     Column(
 *         modifier = Modifier.padding(dimens.screenPadding),
 *         verticalArrangement = Arrangement.spacedBy(dimens.spaceM)
 *     ) { ... }
 *
 * Nota:
 * ------
 * Este CompositionLocal debe ser provisto por [SRIAppTheme] mediante
 * CompositionLocalProvider, garantizando que siempre exista una instancia válida.
 *
 * Forma parte del Design System del módulo :common:framework y no debe ser
 * accedido desde capas de dominio o data.
 */
val LocalSriDimens = staticCompositionLocalOf { SriDimens() }