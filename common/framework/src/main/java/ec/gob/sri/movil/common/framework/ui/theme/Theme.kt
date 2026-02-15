package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

private val SriLightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,

    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,

    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,

    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,

    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,

    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,

    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,

    // ✅ claves para Cards/Sheets/Scaffold modernos
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val SriDarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,

    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,

    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,

    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,

    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,

    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,

    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,

    // ✅ claves para Cards/Sheets/Scaffold modernos
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

/**
 * Theme principal de la aplicación SRI Móvil.
 *
 * ¿Qué es?
 * ----------
 * `SRIAppTheme` es el punto central de configuración visual de la aplicación.
 * Define y provee:
 *
 * - ColorScheme (Light / Dark).
 * - Typography.
 * - Shapes.
 * - Tokens de dimensión (SriDimens).
 *
 * Funciona como el "contenedor raíz" del Design System.
 *
 * ¿Qué hace internamente?
 * ------------------------
 * 1. Determina si el sistema está en modo oscuro.
 * 2. Selecciona el ColorScheme correspondiente.
 * 3. Crea una instancia de SriDimens.
 * 4. Provee SriDimens al árbol de composición mediante CompositionLocal.
 * 5. Configura MaterialTheme con colores, tipografía y shapes.
 *
 * ¿Por qué usar CompositionLocal para SriDimens?
 * -----------------------------------------------
 * Permite:
 *
 * - Cambiar dinámicamente las dimensiones (compact / tablet / accesibilidad).
 * - Sobrescribir dimensiones en previews o tests.
 * - Mantener separación entre tokens y lógica UI.
 *
 * Ejemplo de uso:
 * ---------------
 * En la raíz de la app:
 *
 *     SRIAppTheme {
 *         AppNavigation()
 *     }
 *
 * En cualquier composable hijo:
 *
 *     val dimens = SRITheme.dimens
 *     Modifier.padding(dimens.screenPadding)
 *
 * Ventajas arquitectónicas:
 * ---------------------------
 * - Centraliza la configuración visual.
 * - Alineado con Material 3.
 * - Compatible con Clean Architecture.
 * - Facilita evolución del Design System institucional.
 *
 * Ubicación:
 * ----------
 * Debe vivir en :common:framework, ya que depende de Compose y Material3.
 */
@Composable
fun SRIAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) SriDarkColorScheme else SriLightColorScheme

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // Aquí luego puedo escoger variantes: compact / regular / tablet
    val dimens = remember(screenWidth) {
        when {
            screenWidth >= 840 -> SriTabletDimens   // Tablet breakpoint
            screenWidth <= 360 -> SriCompactDimens  // Compact phones
            else -> SriDimens()                     // Default
        }
    }

    // Con esto, todas las pantallas bajo SRIAppTheme {} ya “ven” LocalSriDimens.
    CompositionLocalProvider(
        LocalSriDimens provides dimens
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = SriTypography,
            shapes = SriShapes,
            content = content
        )
    }
}