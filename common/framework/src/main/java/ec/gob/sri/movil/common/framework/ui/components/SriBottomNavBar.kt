package ec.gob.sri.movil.common.framework.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import ec.gob.sri.movil.common.framework.ui.navigation.SriTopLevelNav
import ec.gob.sri.movil.common.framework.ui.theme.SRIAppTheme
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme

/**
 * Bottom navigation Material 3.
 *
 * - Stateless (state hoisted)
 * - Reusable en varias pantallas
 * - Testable (items por id, sin depender de feature enums)
 */
@Composable
fun SriBottomNavBar(
    items: List<SriBottomNavItem>,
    selectedId: String,
    onItemSelected: (SriBottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    // Design System access
    val colors = SRITheme.colors
    val typography = SRITheme.typography

    NavigationBar(
        modifier = modifier.semantics(mergeDescendants = true) {},
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = colors.surfaceContainerLow
    ) {
        items.forEach { item ->
            val selected = item.id == selectedId

            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = { Text(text = item.label, style = typography.labelMedium) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    // ✅ Indicador/píldora azul (selección)
                    indicatorColor = colors.primary,

                    // ✅ Icono azul/white según indicador
                    selectedIconColor = colors.onPrimary,
                    unselectedIconColor = colors.onSurfaceVariant,

                    // ✅ Texto azul cuando está seleccionado
                    selectedTextColor = colors.primary,
                    unselectedTextColor = colors.onSurfaceVariant
                ),
            )
        }
    }
}

// -------------------- PREVIEWS --------------------

@Preview(name = "SriBottomNavBar - Light (Home selected)", showBackground = true)
@Composable
private fun SriBottomNavBarPreview_Light_Home() {
    SRIAppTheme(darkTheme = false) {
        SriBottomNavBar(
            items = SriTopLevelNav.items,
            selectedId = "home",
            onItemSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "SriBottomNavBar - Light (Noticias selected)", showBackground = true)
@Composable
private fun SriBottomNavBarPreview_Light_Noticias() {
    SRIAppTheme(darkTheme = false) {
        SriBottomNavBar(
            items = SriTopLevelNav.items,
            selectedId = "noticias",
            onItemSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "SriBottomNavBar - Dark (Home selected)", showBackground = true)
@Composable
private fun SriBottomNavBarPreview_Dark_Home() {
    SRIAppTheme(darkTheme = true) {
        SriBottomNavBar(
            items = SriTopLevelNav.items,
            selectedId = "home",
            onItemSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
