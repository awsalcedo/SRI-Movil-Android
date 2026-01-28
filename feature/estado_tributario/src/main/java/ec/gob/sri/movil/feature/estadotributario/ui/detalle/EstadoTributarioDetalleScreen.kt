package ec.gob.sri.movil.app.estadotributario.ui.detalle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.models.ObligacionesPendientesDomain
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleAction
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleEvent
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadoTributarioDetalleScreen(
    ruc: String,
    viewModel: EstadoTributarioDetalleViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val initialRuc = remember { ruc }

    // Iniciar la carga de datos
    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(EstadoTributarioDetalleAction.OnLoad(initialRuc))
    }

    // Observar el `evenChannel` para reaccionar a eventos como mostrar un Snackbar.
    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is EstadoTributarioDetalleEvent.OnError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    state.obligacionSeleccionada?.let { obligacion ->
        ObligacionPeriodosSheet(
            sheetState = sheetState,
            obligacion = obligacion,
            onDismiss = viewModel::onDismissObligacion
        )

    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Detalle Estado Tributario",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface, // O .primary si se prefiere
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.estadoTributario != null) {
                EstadoTributarioDetalleContent(
                    contribuyente = state.estadoTributario!!,
                    onObligacionClick = viewModel::onObligacionClick
                )
            }
        }
    }
}

@Composable
fun EstadoTributarioDetalleContent(
    contribuyente: EstadoTributarioDomain,
    onObligacionClick: (ObligacionesPendientesDomain) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = contribuyente.razonSocial,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
        }

        item { GeneralInfoCard(contribuyente) }

        if (contribuyente.obligacionesPendientes.isNotEmpty()) {
            item {
                ObligationsCard(
                    contribuyente.obligacionesPendientes,
                    onObligacionClick
                )
            }
        }
    }
}

@Composable
fun GeneralInfoCard(info: EstadoTributarioDomain) {
    val isAlDia = info.descripcion.contains("AL DIA", ignoreCase = true)

    val statusIcon = if (isAlDia) Icons.Default.CheckCircle else Icons.Default.Warning
    val statusColor = if (isAlDia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Header: título + badge de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado Tributario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                // Badge minimalista
                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    contentColor = statusColor,
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAlDia) "AL DÍA" else "REVISAR",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = statusColor
                        )
                    }
                }
            }

            // Estado (descripción completa) con jerarquía fuerte
            Text(
                text = info.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )

            // Detalles
            InfoRow(label = "Plazo de vigencia", value = info.plazoVigenciaDoc)
            InfoRow(label = "Clase de contribuyente", value = info.claseContribuyente)
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ObligationsCard(
    obligaciones: List<ObligacionesPendientesDomain>,
    onObligacionClick: (ObligacionesPendientesDomain) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            ListItem(
                headlineContent = { Text("Obligaciones Pendientes", fontWeight = FontWeight.Bold) },
                leadingContent = {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
            HorizontalDivider()
            obligaciones.forEachIndexed { index, obligacion ->
                ListItem(
                    modifier = Modifier.clickable { onObligacionClick(obligacion) },
                    headlineContent = {
                        Text(
                            obligacion.descripcion,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    },
                    supportingContent = { Text("${obligacion.periodos.size} periodos pendientes") },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Ver detalle"
                        )
                    }
                )
                if (index < obligaciones.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ObligacionPeriodosSheet(
    sheetState: SheetState,
    obligacion: ObligacionesPendientesDomain,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            Text(
                text = obligacion.descripcion,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${obligacion.periodos.size} PERIODOS PENDIENTES",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
            )
            HorizontalDivider()
            LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
                // Ahora podemos iterar sobre `obligacion.periodos` de forma segura.
                items(obligacion.periodos) { periodo ->
                    ListItem(headlineContent = { Text(text = periodo) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleScreenPreview_ConObligaciones() {
    val contribuyenteConObligaciones = EstadoTributarioDomain(
        ruc = "1314411206001",
        razonSocial = "ESPINALES GARCIA MARCOS RENATO",
        descripcion = "OBLIGACIONES PENDIENTES",
        plazoVigenciaDoc = "3 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = listOf(
            ObligacionesPendientesDomain(
                "DECLARACIÓN DE IVA",
                listOf("AGOSTO 2023", "SEPTIEMBRE 2023")
            ),
            ObligacionesPendientesDomain("DECLARACIÓN DE IVA PAGO", listOf("OCTUBRE 2023"))
        )
    )
    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Detalle Estado Tributario") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                EstadoTributarioDetalleContent(
                    contribuyente = contribuyenteConObligaciones,
                    onObligacionClick = {} // En preview, la acción no hace nada
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleScreenPreview_SinObligaciones() {
    val contribuyenteConObligaciones = EstadoTributarioDomain(
        ruc = "1314411206001",
        razonSocial = "ESPINALES GARCIA MARCOS RENATO",
        descripcion = "OBLIGACIONES PENDIENTES",
        plazoVigenciaDoc = "3 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = emptyList()
    )
    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Detalle Estado Tributario") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                EstadoTributarioDetalleContent(
                    contribuyente = contribuyenteConObligaciones,
                    onObligacionClick = {}
                )
            }
        }
    }
}