package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Tokens semánticos (no “brand colors”) para estados.
 * Evita usar colorScheme.error para warnings.
 *
 * Uso:
 * val ok = SriStatus.ok()
 * val warn = SriStatus.warn()
 */
object SriStatus {

    // ✅ Constantes (no se recrean por recomposición)
    private val WarningAmberLight = Color(0xFFF59E0B)
    private val WarningAmberDark = Color(0xFFFFC107)

    @Composable
    fun ok(): StatusColors {
        // ✅ OK: usa primary (marca)
        val scheme = MaterialTheme.colorScheme
        return StatusColors(
            icon = scheme.primary,
            container = scheme.primary.copy(alpha = 0.12f),
            text = scheme.primary
        )
    }

    @Composable
    fun info(): StatusColors {
        // ℹ️ Info: usa secondary (o tertiary si prefieres)
        val scheme = MaterialTheme.colorScheme
        return StatusColors(
            icon = scheme.secondary,
            container = scheme.secondary.copy(alpha = 0.12f),
            text = scheme.secondary
        )
    }

    @Composable
    fun warn(): StatusColors {
        // ⚠️ Warning: NO usar error.
        // Usamos un ámbar estable (light/dark) y lo adaptamos según luminancia del background.
        val scheme = MaterialTheme.colorScheme

        val isDark = scheme.background.luminance() < 0.5f
        val base = if (isDark) WarningAmberDark else WarningAmberLight

        return StatusColors(
            icon = base,
            container = base.copy(alpha = 0.14f),
            text = base
        )
    }

    @Composable
    fun danger(): StatusColors {
        // ❌ Danger: aquí SÍ aplica error
        val scheme = MaterialTheme.colorScheme
        return StatusColors(
            icon = scheme.error,
            container = scheme.error.copy(alpha = 0.12f),
            text = scheme.error
        )
    }
}

/**
 * Colores para pintar icono / fondo (pill, chip, surface) / texto de estado.
 */
data class StatusColors(
    val icon: Color,
    val container: Color,
    val text: Color
)