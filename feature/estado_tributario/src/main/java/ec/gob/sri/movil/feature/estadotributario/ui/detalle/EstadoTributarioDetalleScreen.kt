package ec.gob.sri.movil.app.estadotributario.ui.detalle

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme
import ec.gob.sri.movil.common.framework.ui.theme.SriStatus
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.models.ObligacionesPendientesDomain
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleAction
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleEvent
import ec.gob.sri.movil.feature.estadotributario.ui.detalle.EstadoTributarioDetalleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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

    val sheetScope = rememberCoroutineScope()

    // Para animar el BottomSheet
    LaunchedEffect(state.obligacionSeleccionada) {
        runCatching {
            if (state.obligacionSeleccionada != null) sheetState.show()
            else sheetState.hide()
        }
    }

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
            onDismiss = {
                sheetScope.launch {
                    sheetState.hide()
                    viewModel.onDismissObligacion()
                }
            }
        )

    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Detalle Estado Tributario",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
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
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { EstadoTributarioFooterDisclaimer() },
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
private fun EstadoTributarioFooterDisclaimer() {
    Surface(
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Text(
            text = "El tiempo reflejado en el Plazo de Vigencia de los Documentos, " +
                    "corresponde al tiempo que tendrá vigencia los documentos impresos el día de hoy.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun EstadoTributarioDetalleContent(
    contribuyente: EstadoTributarioDomain,
    onObligacionClick: (ObligacionesPendientesDomain) -> Unit
) {
    val isNoActivo = contribuyente.descripcion.contains("NO ACTIVO", ignoreCase = true)
    val isAlDia = contribuyente.descripcion.contains("AL DIA", ignoreCase = true)
            || contribuyente.descripcion.contains("AL DÍA", ignoreCase = true)


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = contribuyente.razonSocial,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
        }

        item { GeneralInfoCard(contribuyente) }

        // NO ACTIVO: no mostrar segunda card
        if (!isNoActivo) {

            val obligaciones = contribuyente.obligacionesPendientes

            when {
                obligaciones == null -> {
                    // Backend no envió el bloque => NO mostrar card (como app anterior)
                }

                obligaciones.isEmpty() -> {
                    // Bloque existe pero viene vacío => mostrar empty state
                    item { EmptyObligationsCard(descripcion = contribuyente.descripcion) }
                }

                else -> {
                    item {
                        // AL DÍA: mostrar card pero DESHABILITADA
                        val enabled = !isAlDia

                        ObligationsCard(
                            obligaciones = obligaciones,
                            enabled = enabled,
                            onObligacionClick = { obligacion ->
                                if (enabled && obligacion.periodos.isNotEmpty()) {
                                    onObligacionClick(obligacion)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyObligationsCard(
    descripcion: String
) {
    val isAlDia = descripcion.contains("AL DIA", ignoreCase = true) ||
            descripcion.contains("AL DÍA", ignoreCase = true)

    val status = if (isAlDia) SriStatus.ok() else SriStatus.warn()
    val icon = if (isAlDia) Icons.Default.CheckCircle else Icons.Default.Warning


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = status.container,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = status.icon,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(18.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Obligaciones pendientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Sin obligaciones tributarias.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun GeneralInfoCard(info: EstadoTributarioDomain) {
    val isAlDia = info.descripcion.contains("AL DIA", ignoreCase = true) ||
            info.descripcion.contains("AL DÍA", ignoreCase = true)

    val status = if (isAlDia) SriStatus.ok() else SriStatus.warn()
    val statusIcon = if (isAlDia) Icons.Default.CheckCircle else Icons.Default.Warning


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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
                    color = status.container,
                    contentColor = status.icon,
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = status.icon
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAlDia) "AL DIA" else "REVISAR",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = status.text
                        )
                    }
                }
            }

            // Estado (descripción completa) con jerarquía fuerte
            Text(
                text = info.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = status.text
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
    enabled: Boolean,
    onObligacionClick: (ObligacionesPendientesDomain) -> Unit
) {
    val totalPeriodos = obligaciones.sumOf { it.periodos.size }

    // AL DÍA (enabled=false): icono check + color primary + título "Obligaciones"
    val status = if (enabled) SriStatus.warn() else SriStatus.ok()
    val headerIcon = if (enabled) Icons.Default.Warning else Icons.Default.CheckCircle
    val headerTitle = if (enabled) "Obligaciones pendientes" else "Obligaciones"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header tipo "section" (M3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = status.container,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = headerIcon,
                        contentDescription = null,
                        tint = status.icon,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(18.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = headerTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${obligaciones.size} obligaciones • $totalPeriodos periodos",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            obligaciones.forEachIndexed { index, obligacion ->
                ObligacionRow(
                    obligacion = obligacion,
                    enabled = enabled && obligacion.periodos.isNotEmpty(),
                    onClick = { onObligacionClick(obligacion) }
                )

                if (index < obligaciones.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ObligacionRow(
    obligacion: ObligacionesPendientesDomain,
    enabled: Boolean, // ✅ NUEVO
    onClick: () -> Unit
) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .then(
            if (enabled) {
                Modifier.clickable(
                    role = Role.Button,
                    onClick = onClick
                )
            } else {
                Modifier // ✅ sin clickable = sin ripple + no abre sheet
            }
        )
        .padding(horizontal = 4.dp, vertical = 10.dp)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ListAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(8.dp)
                    .size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = obligacion.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )

            // Si está deshabilitado, mejor no insinuar “pendientes”
            val supporting = if (enabled) {
                "${obligacion.periodos.size} periodos pendientes"
            } else {
                "Sin periodos pendientes"
            }

            Text(
                text = supporting,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Flecha solo si es interactivo
        if (enabled) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {
            Text(
                text = obligacion.descripcion,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "${obligacion.periodos.size} Periodos Pendientes",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
            )
            HorizontalDivider()

            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(obligacion.periodos) { index, periodo ->
                    PeriodoRowCompacto(text = periodo)

                    if (index < obligacion.periodos.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun PeriodoRowCompacto(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Controla el “alto” aquí
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleLightScreenPreview_ActivoConObligaciones() {
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
    SRITheme(darkTheme = false) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleDarkScreenPreview_ActivoConObligaciones() {
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
    SRITheme(darkTheme = true) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleLightScreenPreview_ActivoSinObligaciones() {
    val contribuyenteConObligaciones = EstadoTributarioDomain(
        ruc = "1314411206001",
        razonSocial = "SALCEDO SILVA ALEX WLADIMIR",
        descripcion = "AL DIA EN SUS OBLIGACIONES",
        plazoVigenciaDoc = "0 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = listOf(
            ObligacionesPendientesDomain(
                "SIN OBLIGACIONES TRIBUTARIAS",
                emptyList()
            ),
            ObligacionesPendientesDomain("CONTRIBUYENTE NO ACTIVO", emptyList())
        )
    )
    SRITheme(darkTheme = false) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle Activo sin Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleDarkScreenPreview_ActivoSinObligaciones() {
    val contribuyenteConObligaciones = EstadoTributarioDomain(
        ruc = "1712245974001",
        razonSocial = "SALCEDO SILVA ALEX WLADIMIR",
        descripcion = "AL DIA EN SUS OBLIGACIONES",
        plazoVigenciaDoc = "0 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = listOf(
            ObligacionesPendientesDomain(
                "SIN OBLIGACIONES TRIBUTARIAS",
                emptyList()
            ),
            ObligacionesPendientesDomain("CONTRIBUYENTE NO ACTIVO", emptyList())
        )
    )
    SRITheme(darkTheme = true) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleLigthScreenPreview_Iactivo() {
    val contribuyenteConObligaciones = EstadoTributarioDomain(
        ruc = "1314411206001",
        razonSocial = "MOREIRA TORRES YESSICA ELIZABETH",
        descripcion = "CONTRIBUYENTE NO ACTIVO",
        plazoVigenciaDoc = "0 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = emptyList()
    )
    SRITheme(darkTheme = false) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Pantalla Detalle con Obligaciones", widthDp = 360)
@Composable
fun EstadoTributarioDetalleDarkScreenPreview_Iactivo() {
    val contribuyenteConObligaciones = EstadoTributarioDomain(
        ruc = "1314411206001",
        razonSocial = "MOREIRA TORRES YESSICA ELIZABETH",
        descripcion = "CONTRIBUYENTE NO ACTIVO",
        plazoVigenciaDoc = "0 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = emptyList()
    )
    SRITheme(darkTheme = true) {
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