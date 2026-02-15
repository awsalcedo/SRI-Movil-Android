package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Punto único de acceso al Design System SRI.
 *
 * Permite acceder de forma limpia y desacoplada a:
 * - Colors
 * - Typography
 * - Shapes
 * - Dimens
 *
 * Evita depender directamente de MaterialTheme o LocalSriDimens
 * en las pantallas.
 *
 * Uso:
 *
 * val colors = SRITheme.colors
 * val dimens = SRITheme.dimens
 */
object SRITheme {

    val colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val dimens
        @Composable
        @ReadOnlyComposable
        get() = LocalSriDimens.current
}