package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

private val SriFont = FontFamily.SansSerif
private val baseline = Typography()

val SriTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = SriFont),
    displayMedium = baseline.displayMedium.copy(fontFamily = SriFont),
    displaySmall = baseline.displaySmall.copy(fontFamily = SriFont),

    headlineLarge = baseline.headlineLarge.copy(fontFamily = SriFont),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = SriFont),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = SriFont),

    titleLarge = baseline.titleLarge.copy(
        fontFamily = SriFont,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = SriFont,
        fontWeight = FontWeight.SemiBold
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = SriFont,
        fontWeight = FontWeight.SemiBold
    ),

    bodyLarge = baseline.bodyLarge.copy(fontFamily = SriFont),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = SriFont),
    bodySmall = baseline.bodySmall.copy(fontFamily = SriFont),

    labelLarge = baseline.labelLarge.copy(
        fontFamily = SriFont,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = baseline.labelMedium.copy(fontFamily = SriFont),
    labelSmall = baseline.labelSmall.copy(fontFamily = SriFont),
)