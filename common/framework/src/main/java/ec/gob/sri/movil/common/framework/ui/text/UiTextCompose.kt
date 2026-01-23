package ec.gob.sri.movil.common.framework.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Resuelve un [UiText] a un [String] usando el sistema de recursos de Compose.
 *
 * Debe usarse únicamente en capa UI (@Composable), ya que depende de [androidx.compose.ui.res.stringResource].
 */
@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Res -> stringResource(id, *args.toTypedArray())
}