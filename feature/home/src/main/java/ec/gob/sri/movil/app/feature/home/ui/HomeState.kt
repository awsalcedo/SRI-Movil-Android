package ec.gob.sri.movil.app.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class HomeItemUi(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val enabled: Boolean = true,
    val enabledMessage: String? = null, // mensaje secundario cuando NO está habilitada
    val icon: @Composable () -> Unit,
)

@Immutable
data class HomeState(
    val items: List<HomeItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val selectedBottomMenu: BottomMenuItem = BottomMenuItem.Home
)
