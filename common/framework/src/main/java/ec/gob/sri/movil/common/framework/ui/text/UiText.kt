package ec.gob.sri.movil.common.framework.ui.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Abstracción de texto para UI que soporta:
 *
 * - Texto dinámico (ya formateado) mediante [Dynamic]
 * - Texto basado en recursos (localizable) mediante [Res]
 *
 * Este enfoque permite:
 * - Mantener el dominio libre de dependencias de Android
 * - Devolver mensajes desde mappers/use cases sin acoplarse a Resources
 * - Renderizar el texto final en Compose con [asString]
 */

sealed interface UiText {
    data class DynamicString(val value: String): UiText
    class StringResource(
        @param:StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> stringResource(id = id, *args)
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}