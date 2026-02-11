package ec.gob.sri.movil.app.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.app.feature.home.ui.BottomMenuItem
import ec.gob.sri.movil.app.feature.home.ui.iconVector
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme

@Composable
fun PillBottomNavBar(
    selected: BottomMenuItem,
    onSelected: (BottomMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            shadowElevation = 10.dp,
            color = containerColor
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomMenuItem.entries.forEach { item ->
                    PillNavItem(
                        selected = item == selected,
                        icon = item.iconVector(),
                        label = item.label,
                        showLabel = (item == selected),
                        onClick = { onSelected(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PillNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    showLabel: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent

    val iconColor = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
    else MaterialTheme.colorScheme.onSurfaceVariant

    val textColor = iconColor

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        color = bg
    ) {
        Column(
            modifier = Modifier
                .size(width = 64.dp, height = 52.dp) // ✅ suficiente para icon + label
                .padding(vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor
            )

            if (showLabel) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview(name = "PillBottomNavBar - Light", showBackground = true, backgroundColor = 0xFFF2F2F2)
@Composable
private fun PillBottomNavBarPreview_Light() {
    SRITheme(darkTheme = false) {
        PillBottomNavBar(
            selected = BottomMenuItem.Home,
            onSelected = {}
        )
    }
}

@Preview(name = "PillBottomNavBar - Dark", showBackground = true, backgroundColor = 0xFF111111)
@Composable
private fun PillBottomNavBarPreview_Dark() {
    SRITheme(darkTheme = true) {
        PillBottomNavBar(
            selected = BottomMenuItem.Noticias,
            onSelected = {}
        )
    }
}
