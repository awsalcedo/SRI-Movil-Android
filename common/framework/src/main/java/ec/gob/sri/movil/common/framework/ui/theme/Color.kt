package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Marca (ajusta a tu guideline SRI)
private val SriBlue = Color(0xFF0401A9)
private val SriBlueDark = Color(0xFF7AA7FF)

// Neutrales pro (no muy contrastados)
private val Neutral10 = Color(0xFF0F172A)
private val Neutral20 = Color(0xFF1E293B)
private val Neutral90 = Color(0xFFF8FAFC)

// Estados
private val Error = Color(0xFFB3261E)

val SriLightColorScheme = lightColorScheme(
    primary = SriBlue,
    onPrimary = Color.White,
    secondary = Color(0xFF44546F),
    onSecondary = Color.White,

    background = Neutral90,
    onBackground = Neutral10,

    surface = Color.White,
    onSurface = Neutral10,

    surfaceVariant = Color(0xFFEFF3F9),
    onSurfaceVariant = Color(0xFF334155),

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),

    error = Error,
    onError = Color.White
)

val SriDarkColorScheme = darkColorScheme(
    primary = SriBlueDark,
    onPrimary = Color(0xFF0B1220),

    secondary = Color(0xFFB7C4D9),
    onSecondary = Color(0xFF0B1220),

    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE6EDF8),

    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFE6EDF8),

    surfaceVariant = Color(0xFF162033),
    onSurfaceVariant = Color(0xFFB8C7E1),

    outline = Color(0xFF3A4A66),
    outlineVariant = Color(0xFF22304A),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)