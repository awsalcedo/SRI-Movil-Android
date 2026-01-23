package ec.gob.sri.movil.common.framework.ui.text

import androidx.annotation.StringRes

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
    /**
     * Texto ya calculado (por ejemplo, un mensaje retornado por backend o compuesto en runtime).
     */
    data class Dynamic(val value: String) : UiText

    /**
     * Texto localizable usando un recurso de string.
     *
     * @param id id del string resource.
     * @param args argumentos para formateo (string placeholders).
     */
    data class Res(@param:StringRes val id: Int, val args: List<Any> = emptyList()) : UiText
}