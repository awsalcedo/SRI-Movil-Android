package ec.gob.sri.movil.app.feature.deudas.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.app.feature.deudas.domain.models.ContribuyenteDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DetalleRubroDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudaDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.common.framework.ui.format.toFechaCorte
import ec.gob.sri.movil.common.framework.ui.format.toUsdEc
import ec.gob.sri.movil.common.framework.ui.text.UiText
import ec.gob.sri.movil.common.framework.ui.theme.SRIAppTheme
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeudasDetalleScreen(
    state: DeudasDetalleState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SRITheme.colors
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
        containerColor = colors.background
    ) { padding ->

        when {
            state.isLoading -> {
                LoadingContent(modifier = Modifier.padding(padding))
            }

            state.error != null -> {
                ErrorContent(
                    message = state.error,
                    modifier = Modifier.padding(padding)
                )
            }

            state.data != null -> {
                DeudasDetalleContent(
                    data = state.data,
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                // Estado vacío defensivo (no debería pasar si tu VM está bien)
                ErrorContent(
                    message = UiText.DynamicString("No hay información para mostrar."),
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimens.screenPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = colors.primary,
                strokeWidth = 3.dp
            )
            Text(
                text = "Cargando…",
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: UiText,
    modifier: Modifier = Modifier
) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimens.screenPadding),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(dimens.cardRadius),
            colors = CardDefaults.elevatedCardColors(containerColor = colors.surface)
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
                    text = message.asString(), // <- si tu UiText no tiene asString() sin contexto, cambia por el que uses en tu proyecto
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DeudasDetalleContent(
    data: DeudasDomain,
    modifier: Modifier = Modifier
) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimens.screenPadding, vertical = dimens.spaceM),
        verticalArrangement = Arrangement.spacedBy(dimens.spaceM)
    ) {

        ElevatedCard(
            shape = RoundedCornerShape(dimens.cardRadius),
            colors = CardDefaults.elevatedCardColors(containerColor = colors.surface)
        ) {
            Column(modifier = Modifier.padding(dimens.cardPadding)) {

                Text(
                    text = data.contribuyente.nombreComercial,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )

                Spacer(Modifier.height(dimens.spaceS))

                Text(
                    text = data.contribuyente.identificacion,
                    style = typography.titleMedium,
                    color = colors.onSurface
                )
            }
        }

        SriDetalleSection(
            title = "FECHA CORTE",
            value = data.contribuyente.fechaInformacion.toFechaCorte()
        )

        SriDetalleSection(
            title = "DEUDAS FIRMES",
            value = data.deuda?.valor?.toUsdEc() ?: "No registra deudas firmes"
        )

        SriDetalleSection(
            title = "IMPUGNACIONES",
            value = data.impugnacion ?: "No se registra impugnaciones"
        )

        SriNotasDeuda()
    }
}

@Composable
private fun SriDetalleSection(
    title: String,
    value: String
) {
    val colors = SRITheme.colors
    val dimens = SRITheme.dimens
    val typography = SRITheme.typography

    ElevatedCard(
        shape = RoundedCornerShape(dimens.cardRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = colors.surface)
    ) {
        Column {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.primary)
                    .padding(dimens.cardPadding),
                color = colors.onPrimary,
                style = typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                modifier = Modifier.padding(dimens.cardPadding),
                style = typography.bodyLarge,
                color = colors.onSurface
            )
        }
    }
}

@Composable
private fun SriNotasDeuda() {
    val colors = SRITheme.colors
    val typography = SRITheme.typography
    val dimens = SRITheme.dimens

    ElevatedCard(
        shape = RoundedCornerShape(dimens.cardRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = colors.surface)
    ) {
        Column(
            modifier = Modifier.padding(dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS)
        ) {
            Text(
                "1. La información registrada puede variar de existir el pago o justificación de las deudas pendientes en los últimos días.",
                style = typography.bodySmall,
                color = colors.onSurfaceVariant
            )
            Text(
                "2. En caso de existir inconsistencias en la información de su deuda por favor acérquese a la oficina del SRI más cercana.",
                style = typography.bodySmall,
                color = colors.onSurfaceVariant
            )
            Text(
                "* Impugnaciones son los actos que pretenden obtener la modificación, revocatoria o invalidación de un acto administrativo. Se tramitan ante la propia Administración Tributaria o los Tribunales de la República.",
                style = typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DeudasDetalle_Preview() {
    SRIAppTheme(darkTheme = false) {
        DeudasDetalleScreen(
            state = DeudasDetalleState(
                isLoading = false,
                data = fakeDeudasDomainForPreview(),
                error = null
            ),
            onBack = {}
        )
    }
}

private fun fakeDeudasDomainForPreview(): DeudasDomain {
    return DeudasDomain(
        contribuyente = ContribuyenteDomain(
            identificacion = "2350294936001",
            denominacion = null,
            tipo = null,
            clase = "PERSONA NATURAL",
            tipoIdentificacion = "R",
            resolucion = null,
            nombreComercial = "VEINTIMILLA ARGUELLO GABRIELA KATHERINE",
            direccionMatriz = null,
            fechaInformacion = System.currentTimeMillis(),
            mensaje = null,
            estado = null
        ),
        deuda = DeudaDomain(
            descripcion = "DEUDA TRIBUTARIA",
            valor = 0.01,
            periodoFiscal = "2024",
            beneficiario = "SRI",
            detallesRubro = listOf(
                DetalleRubroDomain(
                    descripcion = "IVA",
                    anio = 2024,
                    valor = 0.01
                )
            )
        ),
        impugnacion = null,
        remision = null
    )
}


