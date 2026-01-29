package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SRITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) SriDarkColorScheme else SriLightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = SriTypography,
        shapes = SriShapes,
        content = content
    )
}