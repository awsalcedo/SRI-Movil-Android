package ec.gob.sri.movil.app.feature.deudas.ui.coincidencias

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.common.framework.ui.text.UiText
import ec.gob.sri.movil.common.framework.ui.theme.SRIAppTheme
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeudasCoincidenciasScreen(
    state: DeudasCoincidenciasUiState,
    onBack: () -> Unit,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
                title = {
                    Text(
                        text = "Deudas",
                        color = colors.primary,
                        fontWeight = FontWeight.Bold,
                        style = typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        containerColor = colors.surfaceContainerLowest
    ) { padding ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            state.error != null -> {
                ErrorCard(
                    message = state.error,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = dimens.screenPadding, vertical = dimens.spaceM)
                )
            }

            state.items.isEmpty() -> {
                EmptyCard(
                    razonSocial = state.razonSocial,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = dimens.screenPadding, vertical = dimens.spaceM)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = dimens.screenPadding)
                        .padding(top = dimens.spaceM)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceM)
                ) {

                    // Header (como la foto izquierda: texto plano, minimal)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Coincidencias",
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                        Text(
                            text = "Selecciona un contribuyente para ver el detalle.",
                            style = typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(dimens.spaceS),
                        // ✅ evita card cortada al final
                        contentPadding = PaddingValues(bottom = dimens.spaceL)
                    ) {
                        items(
                            items = state.items,
                            key = { it.identificacion } // ✅ estable
                        ) { item ->
                            CoincidenciaRowCompact(
                                item = item,
                                onClick = { onSelected(item.identificacion) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CoincidenciaRowCompact(
    item: DeudasCoincidenciaItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SRITheme.colors
    val typography = SRITheme.typography
    val dimens = SRITheme.dimens
    val shapes = SRITheme.shapes
    val cardShape = shapes.medium


    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        shape = cardShape,
        colors = CardDefaults.elevatedCardColors(containerColor = colors.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevationMed)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.titulo.uppercase(),
                style = typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface
            )

            IdPillSmall(text = item.identificacion)
        }
    }
}

@Composable
private fun IdPillSmall(
    text: String,
    modifier: Modifier = Modifier
) {
    val typography = SRITheme.typography
    val colors = SRITheme.colors
    val shapes = SRITheme.shapes
    val cardShape = shapes.extraSmall


    Surface(
        modifier = modifier,
        shape = cardShape,
        color = colors.primaryContainer.copy(alpha = 0.85f),
        contentColor = androidx.compose.ui.graphics.Color.White,
        border = BorderStroke(
            1.dp,
            colors.primaryContainer.copy(alpha = 0.30f)

        )
    ) {
        Text(
            text = text,
            style = typography.labelSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun ErrorCard(message: UiText, modifier: Modifier = Modifier) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography
    val ctx = androidx.compose.ui.platform.LocalContext.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.cardRadius),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS)
        ) {
            Text(
                text = "Ocurrió un problema",
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.error
            )
            Text(
                text = message.asString(ctx),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyCard(razonSocial: String, modifier: Modifier = Modifier) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.cardRadius),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS)
        ) {
            Text(
                text = "Sin resultados",
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
            Text(
                text = if (razonSocial.isBlank()) {
                    "No se registró una razón social para buscar."
                } else {
                    "No se encontraron coincidencias para: $razonSocial"
                },
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

// ------------------------ PREVIEW ------------------------

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DeudasCoincidencias_Preview() {
    SRIAppTheme(darkTheme = false) {
        val sample = remember {
            DeudasCoincidenciasUiState(
                razonSocial = "SALCEDO SILVA ALEX",
                items = listOf(
                    DeudasCoincidenciaItemUi(
                        identificacion = "1717077505",
                        titulo = "SALCEDO SILVA LEONARDO FABIAN",
                        subtitulo = "PERSONA NATURAL · C"
                    ),
                    DeudasCoincidenciaItemUi(
                        identificacion = "1718124694001",
                        titulo = "SALCEDO SILVA MAURICIO FERNANDO",
                        subtitulo = "PERSONA NATURAL · R"
                    ),
                    DeudasCoincidenciaItemUi(
                        identificacion = "1755929005",
                        titulo = "SALCEDO SILVA RODRIGO JAVIER",
                        subtitulo = "PERSONA NATURAL · C"
                    ),
                    DeudasCoincidenciaItemUi(
                        identificacion = "1759166406",
                        titulo = "SALCEDO SILVA STEFANNY CAROLINA",
                        subtitulo = "PERSONA NATURAL · C"
                    ),
                    DeudasCoincidenciaItemUi(
                        identificacion = "1718651027",
                        titulo = "SALCEDO VASQUEZ NATHALY SILVANA",
                        subtitulo = "PERSONA NATURAL · C"
                    )
                )
            )
        }

        DeudasCoincidenciasScreen(
            state = sample,
            onBack = {},
            onSelected = {}
        )
    }
}