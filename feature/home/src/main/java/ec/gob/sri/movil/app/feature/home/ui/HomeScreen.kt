package ec.gob.sri.movil.app.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.app.common.navigation.NavigationRoute
import ec.gob.sri.movil.common.framework.ui.theme.SRIAppTheme
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme
import kotlinx.coroutines.flow.collectLatest

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
            when (event) {
                is HomeEvent.NavigateTo -> onNavigate(event.route)
                is HomeEvent.OnError -> TODO()
                is HomeEvent.OpenUrl -> openUrl(event.url)
            }
        }
    }

    HomeContentScreen(
        state = state,
        onHomeItemClick = { id ->
            viewModel.onAction(HomeAction.OnHomeItemClick(id))
        },
        onSearchChanged = { query ->
            viewModel.onAction(HomeAction.OnSearchChanged(query))
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContentScreen(
    state: HomeState,
    onHomeItemClick: (id: String) -> Unit,
    onSearchChanged: (query: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    // Design System access
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    val normalizedQuery = remember(state.searchQuery) {
        state.searchQuery.trim().lowercase()
    }

    val filteredItems by remember(state.items, normalizedQuery) {
        derivedStateOf {
            if (normalizedQuery.isBlank()) state.items
            else state.items.filter { item ->
                item.title.lowercase().contains(normalizedQuery) ||
                        (item.subtitle?.lowercase()?.contains(normalizedQuery) == true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SRI Móvil", style = typography.titleLarge) }
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
                columns = GridCells.Adaptive(minSize = dimens.homeGridMinCellSize),
                contentPadding = PaddingValues(
                    start = dimens.screenPadding,
                    end = dimens.screenPadding,
                    top = dimens.screenPadding,
                    bottom = padding.calculateBottomPadding() + dimens.screenPadding
                ),
                verticalArrangement = Arrangement.spacedBy(dimens.homeGridSpacing),
                horizontalArrangement = Arrangement.spacedBy(dimens.homeGridSpacing),
            ) {
                // Search (full width)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeSearchPill(
                        query = state.searchQuery,
                        onQueryChange = onSearchChanged,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Banner (full width)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeHeroBanner(
                        label = "AVISO IMPORTANTE",
                        title = "Temporada de Declaraciones",
                        body = "Recuerde presentar su impuesto a la renta antes del 31 de marzo. Evite multas y recargos.",
                        ctaText = "Declarar Ahora",
                        onClick = { /* TODO: evento/navegación luego */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimens.spaceS)
                    )
                }

                // Header “Servicios Destacados / Ver todos”
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        title = "Servicios Destacados",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimens.spaceS)
                    )
                }

                // Cards
                items(
                    items = filteredItems,
                    key = { it.id }
                ) { item ->
                    HomeCardNew(
                        item = item,
                        onClick = { onHomeItemClick(item.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// -------------------- COMPONENTS --------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeCardNew(
    item: HomeItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = SRITheme.dimens
    val colors = SRITheme.colors
    val typography = SRITheme.typography
    val shapes = SRITheme.shapes

    val cardShape = shapes.medium
    val iconTileColor =
        if (item.enabled) colors.surfaceContainerHighest else colors.surfaceVariant

    ElevatedCard(
        onClick = onClick,
        enabled = item.enabled,
        shape = cardShape,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = dimens.homeCardMinHeight)
            .clip(cardShape),               // Garantiza ripple redondeado
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (item.enabled) colors.surfaceContainerLow
            else colors.surfaceVariant
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = dimens.cardElevationMed
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS)
        ) {
            // Icon tile
            Surface(
                shape = shapes.medium,
                color = iconTileColor,
            ) {
                Box(
                    modifier = Modifier.padding(
                        horizontal = dimens.homeIconTilePaddingH,
                        vertical = dimens.homeIconTilePaddingV
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    item.icon()
                }
            }

            Text(
                text = item.title,
                style = typography.titleMedium,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val supporting = when {
                item.enabled -> item.subtitle
                !item.enabled && !item.enabledMessage.isNullOrBlank() -> item.enabledMessage
                else -> null
            }

            if (!supporting.isNullOrBlank()) {
                Text(
                    text = supporting,
                    style = typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun HomeSearchPill(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = SRITheme.colors
    val shapes = SRITheme.shapes
    val dimens = SRITheme.dimens

    Surface(
        modifier = modifier,
        shape = shapes.extraLarge,
        color = colors.surfaceContainerLowest,
        tonalElevation = dimens.searchElevation,
        shadowElevation = dimens.searchElevation
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dimens.searchMinHeight),
            singleLine = true,
            shape = shapes.extraLarge,
            placeholder = { Text("Buscar trámites o servicios") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar")
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colors.surfaceContainerLowest,
                focusedContainerColor = colors.surfaceContainerLowest,
                unfocusedBorderColor = colors.surfaceContainerLowest,
                focusedBorderColor = colors.surfaceContainerLowest,
            )
        )
    }
}

@Composable
private fun HomeHeroBanner(
    label: String,
    title: String,
    body: String,
    ctaText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = SRITheme.colors
    val typography = SRITheme.typography
    val dimens = SRITheme.dimens
    val shapes = SRITheme.shapes

    val shape = shapes.large

    ElevatedCard(
        onClick = onClick,
        shape = shape,
        modifier = modifier
            .clip(shape)
            .heightIn(dimens.homeBannerMinHeight),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = dimens.bannerElevation
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colors.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS)
        ) {
            Text(
                text = label,
                style = typography.labelMedium,
                color = colors.onPrimary.copy(alpha = 0.85f)
            )
            Text(
                text = title,
                style = typography.headlineSmall,
                color = colors.onPrimary
            )
            Text(
                text = body,
                style = typography.bodyMedium,
                color = colors.onPrimary.copy(alpha = 0.92f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.padding(top = dimens.spaceS))

            // CTA visual (por ahora simple; luego lo volvemos Button)
            Button(
                onClick = onClick,
                shape = shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.surface,
                    contentColor = colors.primary
                ),
                contentPadding = PaddingValues(
                    horizontal = dimens.spaceL,
                    vertical = dimens.spaceS
                )
            ) {
                Text(text = ctaText, style = typography.labelLarge)
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    val typography = SRITheme.typography
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = typography.titleLarge)
    }
}

@Preview(name = "Home - Light", showBackground = true)
@Composable
private fun HomeContentScreenPreview_Light() {
    SRIAppTheme(darkTheme = false) {
        HomeContentScreen(
            state = HomeState(
                isLoading = false,
                searchQuery = "",
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
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "validez",
                        title = "Validez documentos físicos",
                        subtitle = "Verifica si el documento físico es válido para su emisión y/o recepción",
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
                        subtitle = "Conozca un detalle histórico de los valores de Impuesto a la Renta Causado, ISD y otros regímenes",
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
                        subtitle = "Validación de certificados y declaraciones emitidos en línea",
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
                        subtitle = "Información sobre proceso de trámites",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "qr",
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
                        subtitle = "Turnos en línea",
                        enabled = false,
                        enabledMessage = "Temporalmente no disponible",
                        icon = { Icon(Icons.Default.RequestPage, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "calculadoras",
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
                        subtitle = "Denuncias Adiminstrativas y Tributarias",
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
                        subtitle = "Su opinión nos interesa",
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
                        subtitle = "Comprobar la legalidad de las precintas fiscales del SRI",
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
                        subtitle = "Fácil, seguro y sin costo",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "configuracion",
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
            onHomeItemClick = {},
            onSearchChanged = {}
        )
    }
}

@Preview(name = "Home - Dark", showBackground = true)
@Composable
private fun HomeContentScreenPreview_Dark() {
    SRIAppTheme(darkTheme = true) {
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
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "validez",
                        title = "Validez documentos físicos",
                        subtitle = "Verifica si el documento físico es válido para su emisión y/o recepción",
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
                        subtitle = "Conozca un detalle histórico de los valores de Impuesto a la Renta Causado, ISD y otros regímenes",
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
                        subtitle = "Validación de certificados y declaraciones emitidos en línea",
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
                        subtitle = "Información sobre proceso de trámites",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "qr",
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
                        subtitle = "Turnos en línea",
                        enabled = false,
                        enabledMessage = "Temporalmente no disponible",
                        icon = { Icon(Icons.Default.RequestPage, contentDescription = null) }
                    ),
                    HomeItemUi(
                        id = "calculadoras",
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
                        subtitle = "Denuncias Adiminstrativas y Tributarias",
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
                        subtitle = "Su opinión nos interesa",
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
                        subtitle = "Comprobar la legalidad de las precintas fiscales del SRI",
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
                        subtitle = "Fácil, seguro y sin costo",
                        enabled = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null
                            )
                        }
                    ),
                    HomeItemUi(
                        id = "configuracion",
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
            onHomeItemClick = {},
            onSearchChanged = {}
        )
    }
}


