package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme

@Composable
fun DeudasConsultaScreen(
    viewModel: DeudasConsultaViewModel = hiltViewModel(),
    onEvent: (DeudasConsultaEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event -> onEvent(event) }
    }

    DeudasConsultaContentScreen(
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeudasConsultaContentScreen(
    state: DeudasConsultaUiState,
    onAction: (DeudasConsultaAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.safeDrawing),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Deudas",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(DeudasConsultaAction.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ---------- Contribuyente ----------
                    SectionLabel(text = "Tipo de Contributente")

                    ContribuyenteSegmented(
                        selected = state.tipoContribuyente,
                        onSelected = { onAction(DeudasConsultaAction.TipoContribuyenteSelected(it)) }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    // ---------- Tipo Identificación ----------
                    SectionLabel(text = "Tipo de Identificación")

                    IdTypeDropdown(
                        value = state.idType,
                        onChange = { onAction(DeudasConsultaAction.IdTypeSelected(it)) }
                    )

                    // ---------- Inputs dinámicos ----------
                    when (state.idType) {
                        IdType.RUC -> {
                            SriOutlinedField(
                                label = "RUC",
                                value = state.ruc,
                                placeholder = "13 dígitos",
                                supporting = "Ingresa 13 dígitos (sin guiones).",
                                isError = state.ruc.isNotEmpty() && state.ruc.length != 13,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                                onValueChange = {
                                    onAction(DeudasConsultaAction.RucChanged(it.onlyDigitsMax(13)))
                                },
                                onImeDone = {
                                    focusManager.clearFocus()
                                    if (!state.isLoading && state.isValid) {
                                        onAction(DeudasConsultaAction.ConsultarClicked)
                                    }
                                }
                            )
                        }

                        IdType.CEDULA -> {
                            SriOutlinedField(
                                label = "Cédula",
                                value = state.cedula,
                                placeholder = "10 dígitos",
                                supporting = "Ingresa 10 dígitos (sin guiones).",
                                isError = state.cedula.isNotEmpty() && state.cedula.length != 10,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                                onValueChange = {
                                    onAction(DeudasConsultaAction.CedulaChanged(it.onlyDigitsMax(10)))
                                },
                                onImeDone = {
                                    focusManager.clearFocus()
                                    if (!state.isLoading && state.isValid) {
                                        onAction(DeudasConsultaAction.ConsultarClicked)
                                    }
                                }
                            )
                        }

                        IdType.APELLIDOS_NOMBRES -> {
                            SriOutlinedField(
                                label = "Apellidos",
                                value = state.apellidos,
                                placeholder = "Ej. Pérez González",
                                supporting = "Tal como constan en el documento.",
                                isError = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                onValueChange = {
                                    onAction(DeudasConsultaAction.ApellidosChanged(it.normalizeName()))
                                },
                                onImeDone = { /* no-op: Next */ }
                            )

                            SriOutlinedField(
                                label = "Nombres",
                                value = state.nombres,
                                placeholder = "Ej. Juan Carlos",
                                supporting = "Tal como constan en el documento.",
                                isError = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                                onValueChange = {
                                    onAction(DeudasConsultaAction.NombresChanged(it.normalizeName()))
                                },
                                onImeDone = {
                                    focusManager.clearFocus()
                                    if (!state.isLoading && state.isValid) {
                                        onAction(DeudasConsultaAction.ConsultarClicked)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // ---------- CTA ----------
            PrimaryLoadingButton(
                text = "Consultar",
                loading = state.isLoading,
                enabled = state.isValid && !state.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    onAction(DeudasConsultaAction.ConsultarClicked)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ContribuyenteSegmented(
    selected: ContribuyenteType,
    onSelected: (ContribuyenteType) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        val items = ContribuyenteType.entries
        items.forEachIndexed { index, item ->
            SegmentedButton(
                selected = selected == item,
                onClick = { onSelected(item) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = items.size),
                label = { Text(text = item.label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdTypeDropdown(
    value: IdType,
    onChange: (IdType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { !expanded },
        modifier = Modifier
    ) {
        OutlinedTextField(
            value = value.label,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Selecciona") },
            trailingIcon = {
                TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            IdType.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        expanded = false
                        onChange(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun SriOutlinedField(
    label: String,
    value: String,
    placeholder: String,
    supporting: String,
    isError: Boolean,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit,
    onImeDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        supportingText = { Text(supporting) },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeDone() }
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun PrimaryLoadingButton(
    text: String,
    loading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(52.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        // ✅ Sin layout jump: reservamos siempre el espacio del loader
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Spacer(Modifier.size(18.dp))
                }

                Spacer(Modifier.width(10.dp))

                Text(text)
            }

            if (!loading) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

private fun String.onlyDigitsMax(max: Int): String =
    filter(Char::isDigit).take(max)

private fun String.normalizeName(): String =
    trim()
        .replace(Regex("\\s+"), " ")
        .uppercase()

// ------------------------ PREVIEWS ------------------------

@Preview(showBackground = true)
@Composable
fun DeudasConsulta_Light_Ruc_Preview() {
    SRITheme(darkTheme = false) {
        DeudasConsultaContentScreen(
            state = DeudasConsultaUiState(
                tipoContribuyente = ContribuyenteType.PERSONA_NATURAL,
                idType = IdType.RUC,
                ruc = "1712245974001"
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeudasConsulta_Light_Cedula_Preview() {
    SRITheme(darkTheme = false) {
        DeudasConsultaContentScreen(
            state = DeudasConsultaUiState(
                tipoContribuyente = ContribuyenteType.PERSONA_NATURAL,
                idType = IdType.CEDULA,
                cedula = "1712245974"
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeudasConsulta_Dark_ApellidosNombres_Preview() {
    SRITheme(darkTheme = true) {
        DeudasConsultaContentScreen(
            state = DeudasConsultaUiState(
                tipoContribuyente = ContribuyenteType.PERSONA_JURIDICA,
                idType = IdType.APELLIDOS_NOMBRES,
                apellidos = "SALCEDO SILVA",
                nombres = "ALEX"
            ),
            onAction = {}
        )
    }
}