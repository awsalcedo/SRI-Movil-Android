package ec.gob.sri.movil.common.framework.ui.error

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.framework.R
import ec.gob.sri.movil.common.framework.ui.text.UiText

/**
 * Convierte un [ec.gob.sri.movil.common.domain.error.AppError] a un [ec.gob.sri.movil.common.framework.ui.text.UiText] (texto UI localizable).
 *
 * Objetivos:
 * - Evitar que la UI dependa de detalles técnicos (HTTP/JSON/stacktraces)
 * - Proveer mensajes consistentes y traducibles mediante recursos
 * - Mantener el mapeo centralizado y fácil de evolucionar
 *
 * Nota:
 * - Algunos grupos se mapean a un mismo mensaje por UX (por ejemplo, cualquier [ec.gob.sri.movil.common.domain.error.AppError.Auth]
 *   se trata como "sesión expirada").
 * - Los errores locales se agrupan en un mensaje genérico para el usuario final.
 */
fun AppError.toUiText(): UiText = when (this) {

    /* ========= NETWORK ========= */
    AppError.Network.NoInternet ->
        UiText.StringResource(R.string.error_no_internet)

    AppError.Network.Timeout ->
        UiText.StringResource(R.string.error_timeout)

    AppError.Network.Unavailable ->
        UiText.StringResource(R.string.error_unavailable)

    /* ========= HTTP ========= */
    is AppError.Http.Unauthorized ->
        UiText.StringResource(R.string.error_unauthorized)

    is AppError.Http.Forbidden ->
        UiText.StringResource(R.string.error_forbidden)

    is AppError.Http.NotFound ->
        this.message?.takeIf { it.isNotBlank() }?.let { UiText.DynamicString(it) }
            ?:UiText.StringResource(R.string.error_not_found)

    is AppError.Http.TooManyRequests ->
        UiText.StringResource(R.string.error_too_many_requests)

    is AppError.Http.Server ->
        UiText.StringResource(R.string.error_server)

    is AppError.Http.Client ->
        this.message?.takeIf { it.isNotBlank() }?.let { UiText.DynamicString(it) }
            ?:UiText.StringResource(R.string.error_client)

    /* ========= AUTH ========= */
    is AppError.Auth ->
        UiText.StringResource(R.string.error_unauthorized)

    /* ========= LOCAL ========= */
    is AppError.Local ->
        UiText.StringResource(R.string.error_local)

    /* ========= SERIALIZATION ========= */
    AppError.Serialization ->
        UiText.StringResource(R.string.error_serialization)

    /* ========= FALLBACK ========= */
    is AppError.Unknown ->
        UiText.StringResource(R.string.error_unknown)

}