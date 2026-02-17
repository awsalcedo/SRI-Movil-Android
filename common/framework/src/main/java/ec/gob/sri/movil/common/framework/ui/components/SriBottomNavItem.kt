package ec.gob.sri.movil.common.framework.ui.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class SriBottomNavItem(
    val id: String,                 // estable para key + analytics + tests
    val label: String,
    val icon: ImageVector,
    val contentDescription: String? = null,
)
