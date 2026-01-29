package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SriTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)