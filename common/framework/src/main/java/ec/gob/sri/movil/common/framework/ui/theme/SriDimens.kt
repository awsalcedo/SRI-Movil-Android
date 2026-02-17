package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Representa los tokens de dimensión (spacing, tamaños, radios, alturas mínimas, etc.)
 * del Design System SRI.
 *
 * ¿Qué es?
 * ----------
 * `SriDimens` es una colección tipada y centralizada de valores de layout que
 * definen el sistema de espaciado y proporciones visuales de la aplicación.
 *
 * En lugar de usar valores mágicos como `16.dp` o `12.dp` directamente en los
 * composables, estos valores se definen aquí como tokens semánticos.
 *
 * ¿Por qué es importante?
 * ------------------------
 * En aplicaciones profesionales, los sistemas de diseño deben ser:
 *
 * - Consistentes.
 * - Escalables.
 * - Configurables.
 * - Evolucionables.
 *
 * `SriDimens` permite:
 *
 * - Cambiar el spacing global sin modificar cada pantalla.
 * - Definir variantes futuras (compact, tablet, accesibilidad).
 * - Mantener coherencia visual en toda la app.
 * - Evitar hardcoded dp repetidos.
 *
 * Ejemplo:
 * --------
 * En lugar de:
 *
 *     Modifier.padding(16.dp)
 *
 * Usamos:
 *
 *     val dimens = SRITheme.dimens
 *     Modifier.padding(dimens.screenPadding)
 *
 * Beneficios:
 * ------------
 * - Mejora mantenibilidad.
 * - Facilita refactors visuales globales.
 * - Reduce deuda técnica.
 * - Refuerza el Design System institucional.
 *
 * Nota:
 * ------
 * Esta clase es inmutable (@Immutable) para optimizar recomposición en Compose.
 * Se expone vía CompositionLocal a través de SRIAppTheme.
 *
 * Ubicación arquitectónica:
 * --------------------------
 * Pertenece al módulo :common:framework porque depende de Compose y
 * forma parte de la capa de presentación (UI Framework layer).
 */
@Immutable
data class SriDimens(
    val screenPadding: Dp = 16.dp,
    val cardRadius: Dp = 18.dp,
    val itemRadius: Dp = 10.dp,

    val spaceXS: Dp = 4.dp,
    val spaceS: Dp = 8.dp,
    val spaceM: Dp = 12.dp,
    val spaceL: Dp = 16.dp,

    val cardPadding: Dp = 14.dp,
    val homeCardMinHeight: Dp = 120.dp,

    val homeGridSpacing: Dp = 12.dp,
    val homeBottomBarClearance: Dp = 92.dp,
    val homeGridMinCellSize: Dp = 160.dp,

    // Elevations
    val cardElevationLow: Dp = 2.dp,
    val cardElevationMed: Dp = 6.dp,
    val bannerElevation: Dp = 8.dp,
    val searchElevation: Dp = 4.dp,

    // Home specific sizes
    val homeSearchMinHeight: Dp = 56.dp,
    val homeBannerMinHeight: Dp = 190.dp,

    // Icon tile paddings
    val homeIconTilePaddingH: Dp = 12.dp,
    val homeIconTilePaddingV: Dp = 10.dp,

    val searchMinHeight: Dp = 52.dp
)