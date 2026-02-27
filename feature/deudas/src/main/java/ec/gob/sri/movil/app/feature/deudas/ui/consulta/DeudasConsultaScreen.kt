package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.gob.sri.movil.common.framework.ui.components.SriButton
import ec.gob.sri.movil.common.framework.ui.theme.SRIAppTheme
import ec.gob.sri.movil.common.framework.ui.theme.SRITheme
import ec.gob.sri.movil.common.framework.ui.util.ObserveAsEvents

@Composable
fun DeudasConsultaScreen(
    viewModel: DeudasConsultaViewModel = hiltViewModel(),
    onNavigateToResultados: (DeudasQuery) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackbarState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is DeudasConsultaEvent.OnError -> {
                snackbarState.showSnackbar(event.message.asString(context))
            }

            is DeudasConsultaEvent.NavigateToResultados -> {
                onNavigateToResultados(event.query)
            }
        }
    }

    DeudasConsultaContentScreen(
        state = state,
        snackbarState = snackbarState,
        onAction = viewModel::onAction,
        onBack = onBack
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeudasConsultaContentScreen(
    state: DeudasConsultaUiState,
    snackbarState: SnackbarHostState,
    onAction: (DeudasConsultaAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Design System access
    val dimens = SRITheme.dimens
    val colors = SRITheme.colors
    val typography = SRITheme.typography

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
                title = {
                    Text(
                        "Deudas",
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarState)
        },
        containerColor = colors.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.screenPadding)
                .padding(top = dimens.spaceM),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceL)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(dimens.cardRadius),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = colors.surfaceContainerLow
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = dimens.cardElevationLow
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(dimens.cardPadding),
                    verticalArrangement = Arrangement.spacedBy(dimens.cardPadding)
                ) {
                    // ---------- Contribuyente ----------
                    SectionLabel(text = "Tipo de Contributente")

                    ContribuyenteSegmented(
                        selected = state.tipoContribuyente,
                        onSelected = { onAction(DeudasConsultaAction.TipoContribuyenteSelected(it)) }
                    )

                    HorizontalDivider(color = colors.outlineVariant)

                    // ---------- Tipo Identificación ----------
                    SectionLabel(text = "Tipo de Identificación")

                    val idTypeOptions = remember(state.tipoContribuyente) {
                        when (state.tipoContribuyente) {
                            ContribuyenteType.PERSONA_JURIDICA -> listOf(IdType.RUC, IdType.RAZON_SOCIAL)
                            ContribuyenteType.PERSONA_NATURAL -> listOf(IdType.CEDULA, IdType.RUC, IdType.APELLIDOS_NOMBRES)
                        }
                    }

                    IdTypeDropdown(
                        value = state.idType,
                        options = idTypeOptions,
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

                            // Nombres (opcionales)
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

                        IdType.RAZON_SOCIAL -> {
                            SriOutlinedField(
                                label = "Razón Social",
                                value = state.razonSocialInput,
                                placeholder = "Ej. MI EMPRESA S.A.",
                                supporting = "Ingresa la razón social del contribuyente.",
                                isError = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                                onValueChange = {
                                    onAction(DeudasConsultaAction.RazonSocialChanged(it.normalizeName()))
                                },
                                onImeDone = {
                                    focusManager.clearFocus()
                                    if (!state.isLoading && state.isValid) onAction(DeudasConsultaAction.ConsultarClicked)
                                }
                            )
                        }
                    }
                }
            }

            // ---------- CTA ----------
            SriButton(
                text = "Consultar",
                onClick = {
                    focusManager.clearFocus()
                    onAction(DeudasConsultaAction.ConsultarClicked)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isValid && !state.isLoading,
                isLoading = state.isLoading,
            )

            Spacer(Modifier.height(dimens.spaceS))
        }
    }
}

@Composable
fun SectionLabel(text: String) {

    val colors = SRITheme.colors
    val typography = SRITheme.typography

    Text(
        text = text,
        style = typography.labelLarge,
        color = colors.onSurfaceVariant
    )
}

@Composable
fun ContribuyenteSegmented(
    selected: ContribuyenteType,
    onSelected: (ContribuyenteType) -> Unit,
    modifier: Modifier = Modifier
) {

    val colors = SRITheme.colors
    val dimens = SRITheme.dimens

    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        val items = ContribuyenteType.entries
        items.forEachIndexed { index, item ->

            val shape = when (index) {
                0 -> RoundedCornerShape(
                    topStart = dimens.itemRadius,
                    bottomStart = dimens.itemRadius
                )

                items.lastIndex -> RoundedCornerShape(
                    topEnd = dimens.itemRadius,
                    bottomEnd = dimens.itemRadius
                )

                else -> RoundedCornerShape(0.dp)
            }

            SegmentedButton(
                selected = selected == item,
                onClick = { onSelected(item) },
                shape = shape,
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = colors.primary,
                    activeContentColor = colors.onPrimary,
                    inactiveContainerColor = colors.surfaceContainerLow,
                    inactiveContentColor = colors.onSurface
                ),
                label = { Text(text = item.label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdTypeDropdown(
    value: IdType,
    options: List<IdType>,
    onChange: (IdType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
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
            options.forEach { option ->
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

private fun String.onlyDigitsMax(max: Int): String =
    filter(Char::isDigit).take(max)

private fun String.normalizeName(): String =
    replace(Regex("\\s+"), " ")
        .trimStart()     // NO borra el espacio final mientras el usuario tipea
        .uppercase()

// ------------------------ PREVIEWS ------------------------

@Preview(showBackground = true)
@Composable
fun DeudasConsulta_Light_Ruc_Preview() {
    SRIAppTheme(darkTheme = false) {
        val snackbarState = remember {
            SnackbarHostState()
        }
        DeudasConsultaContentScreen(
            state = DeudasConsultaUiState(
                tipoContribuyente = ContribuyenteType.PERSONA_NATURAL,
                idType = IdType.RUC,
                ruc = "1712245974001"
            ),
            onAction = {},
            snackbarState = snackbarState,
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeudasConsulta_Light_Cedula_Preview() {
    SRIAppTheme(darkTheme = false) {
        val snackbarState = remember {
            SnackbarHostState()
        }
        DeudasConsultaContentScreen(
            state = DeudasConsultaUiState(
                tipoContribuyente = ContribuyenteType.PERSONA_NATURAL,
                idType = IdType.CEDULA,
                cedula = "1712245974"
            ),
            onAction = {},
            snackbarState = snackbarState,
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeudasConsulta_Dark_ApellidosNombres_Preview() {
    SRIAppTheme(darkTheme = true) {
        val snackbarState = remember {
            SnackbarHostState()
        }
        DeudasConsultaContentScreen(
            state = DeudasConsultaUiState(
                tipoContribuyente = ContribuyenteType.PERSONA_JURIDICA,
                idType = IdType.APELLIDOS_NOMBRES,
                apellidos = "SALCEDO SILVA",
                nombres = "ALEX"
            ),
            onAction = {},
            snackbarState = snackbarState,
            onBack = {}
        )
    }
}