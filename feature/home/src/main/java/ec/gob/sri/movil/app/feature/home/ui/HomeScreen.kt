package ec.gob.sri.movil.app.feature.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.app.common.navigation.NavigationRoute
import ec.gob.sri.movil.app.feature.home.ui.components.PillBottomNavBar
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme
import kotlinx.coroutines.flow.collectLatest

enum class BottomMenuItem(val label: String) {
    Home("Home"),
    Noticias("Noticias"),
    Agencias("Agencias"),
    Login("Login"),
}

fun BottomMenuItem.iconVector() = when (this) {
    BottomMenuItem.Home -> Icons.Default.Home
    BottomMenuItem.Noticias -> Icons.Default.Article
    BottomMenuItem.Agencias -> Icons.Default.LocationOn
    BottomMenuItem.Login -> Icons.Default.AccountCircle
}


// -------------------- SCREEN --------------------
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (route: NavigationRoute) -> Unit,
    openUrl: (url: String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(HomeAction.OnLoad)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when(event) {
                is HomeEvent.NavigateTo -> onNavigate(event.route)
                is HomeEvent.OnError -> TODO()
                is HomeEvent.OpenUrl -> openUrl(event.url)
            }
        }
    }

    HomeContentScreen(
        state = state,
        selectedBottomMenu = state.selectedBottomMenu,
        onBottomMenuClick = { item ->
            viewModel.onAction(HomeAction.OnBottomMenuClick(item))
        },
        onHomeItemClick = { id ->
            viewModel.onAction(HomeAction.OnHomeItemClick(id))
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContentScreen(
    state: HomeState,
    selectedBottomMenu: BottomMenuItem,
    onBottomMenuClick: (BottomMenuItem) -> Unit,
    onHomeItemClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SRI Móvil") }
            )
        },
        bottomBar = {
            /*FloatingBottomMenuBar(
                selected = selectedBottomMenu,
                onSelected = onBottomMenuClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )*/
            PillBottomNavBar(
                selected = selectedBottomMenu,
                onSelected = onBottomMenuClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
            )
        },
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                return@Box
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 92.dp // deja aire para la barra flotante
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.items,
                    key = { it.id }
                ) { item ->
                    HomeCard(
                        item = item,
                        onClick = { onHomeItemClick(item.id) }
                    )
                }
            }
        }
    }
}

// -------------------- COMPONENTS --------------------

@Composable
private fun HomeCard(
    item: HomeItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickModifier = if (item.enabled) {
        Modifier.clickable(role = Role.Button, onClick = onClick)
    } else {
        Modifier
    }

    ElevatedCard(
        modifier = modifier.then(clickModifier),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (item.enabled) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item.icon()

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val supporting = when {
                item.enabled -> item.subtitle
                !item.enabled && !item.enabledMessage.isNullOrBlank() -> item.enabledMessage
                else -> "No disponible"
            }

            if (!supporting.isNullOrBlank()) {
                Text(
                    text = supporting,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview(name = "Home - Light", showBackground = true)
@Composable
private fun HomeContentScreenPreview_Light() {
    SRITheme(darkTheme = false) {
        HomeContentScreen(
            state = HomeState(
                items = listOf(
                    HomeItemUi(
                        id = "comprobantes",
                        title = "Comprobantes electrónicos",
                        subtitle = "Consulta y descarga",
                        enabled = true,
                        icon = { Icon(Icons.Default.Description, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "estado_tributario",
                        title = "Estado tributario",
                        subtitle = "Consulta tu estado",
                        enabled = true,
                        icon = { Icon(Icons.Default.RequestPage, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "valores_pagar",
                        title = "Valores a pagar",
                        subtitle = "Obligaciones pendientes",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "deudas",
                        title = "Deudas",
                        subtitle = "Firmes y en gestión",
                        enabled = true,
                        icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "validez",
                        title = "Validez documentos físicos",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "renta",
                        title = "Impuesto a la Renta Causado",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "certificados",
                        title = "Certificados",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "tramites",
                        title = "Seguimiento de trámites",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "qr4",
                        title = "Validación códigos QR",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "cita_previa",
                        title = "Cita previa",
                        subtitle = "Agenda tu atención",
                        enabled = false,
                        enabledMessage = "Temporalmente no disponible",
                        icon = { Icon(Icons.Default.RequestPage, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "cal",
                        title = "Calculadoras",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "denuncia",
                        title = "Denuncias",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "contactos",
                        title = "Contáctenos",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "simar",
                        title = "SIMAR",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "facturador",
                        title = "Facturador SRI",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "config",
                        title = "Configuración",
                        subtitle = "Preferencias",
                        enabled = false,
                        enabledMessage = "Temporalmente no disponible",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null
                            )
                        }
                    )
                )
            ),
            selectedBottomMenu = BottomMenuItem.Home,
            onBottomMenuClick = {},
            onHomeItemClick = {}
        )
    }
}

@Preview(name = "Home - Dark", showBackground = true)
@Composable
private fun HomeContentScreenPreview_Dark() {
    SRITheme(darkTheme = true) {
        HomeContentScreen(
            state = HomeState(
                items = listOf(
                    HomeItemUi(
                        id = "comprobantes",
                        title = "Comprobantes electrónicos",
                        subtitle = "Consulta y descarga",
                        enabled = true,
                        icon = { Icon(Icons.Default.Description, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "estado_tributario",
                        title = "Estado tributario",
                        subtitle = "Consulta tu estado",
                        enabled = true,
                        icon = { Icon(Icons.Default.RequestPage, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "valores_pagar",
                        title = "Valores a pagar",
                        subtitle = "Obligaciones pendientes",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "deudas",
                        title = "Deudas",
                        subtitle = "Firmes y en gestión",
                        enabled = true,
                        icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "validez",
                        title = "Validez documentos físicos",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "renta",
                        title = "Impuesto a la Renta Causado",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "certificados",
                        title = "Certificados",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "tramites",
                        title = "Seguimiento de trámites",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "qr4",
                        title = "Validación códigos QR",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "cita_previa",
                        title = "Cita previa",
                        subtitle = "Agenda tu atención",
                        enabled = false,
                        enabledMessage = "Temporalmente no disponible",
                        icon = { Icon(Icons.Default.RequestPage, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "cal",
                        title = "Calculadoras",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "denuncia",
                        title = "Denuncias",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "contactos",
                        title = "Contáctenos",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "simar",
                        title = "SIMAR",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "facturador",
                        title = "Facturador SRI",
                        subtitle = "Escanea y valida",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "config",
                        title = "Configuración",
                        subtitle = "Preferencias",
                        enabled = false,
                        enabledMessage = "Temporalmente no disponible",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null
                            )
                        }
                    )
                )
            ),
            selectedBottomMenu = BottomMenuItem.Home,
            onBottomMenuClick = {},
            onHomeItemClick = {}
        )
    }
}


