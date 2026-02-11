package ec.gob.sri.movil.app.feature.home.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.app.feature.home.ui.BottomMenuItem
import ec.gob.sri.movil.app.feature.home.ui.iconVector
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme


@Composable
fun FloatingBottomMenuBar(
    selected: BottomMenuItem,
    onSelected: (BottomMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            BottomMenuItem.entries.forEach { item ->
                NavigationBarItem(
                    selected = item == selected,
                    onClick = { onSelected(item) },
                    icon = {
                        Icon(
                            imageVector = item.iconVector(),
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label) },
                    alwaysShowLabel = true
                )
            }
        }
    }
}

@Preview(name = "Light theme")
@Composable
fun FloatingBottomMenuBarPreview() {
    SRITheme(darkTheme = false) {
        FloatingBottomMenuBar(
            selected = BottomMenuItem.Home,
            onSelected = {}
        )
    }
}

@Preview(name = "Dark theme")
@Composable
fun FloatingBottomMenuBarDarkPreview() {
    SRITheme(darkTheme = true) {
        FloatingBottomMenuBar(
            selected = BottomMenuItem.Noticias,
            onSelected = {}
        )
    }
}
