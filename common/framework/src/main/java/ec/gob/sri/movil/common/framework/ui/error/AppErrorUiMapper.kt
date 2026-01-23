package ec.gob.sri.movil.common.framework.ui.error

import ec.gob.sri.movil.common.framework.R
import ec.gob.sri.movil.common.domain.error.AppError
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
        UiText.Res(R.string.error_no_internet)

    AppError.Network.Timeout ->
        UiText.Res(R.string.error_timeout)

    AppError.Network.Unavailable ->
        UiText.Res(R.string.error_unavailable)

    /* ========= HTTP ========= */
    is AppError.Http.Unauthorized ->
        UiText.Res(R.string.error_unauthorized)

    is AppError.Http.Forbidden ->
        UiText.Res(R.string.error_forbidden)

    is AppError.Http.NotFound ->
        UiText.Res(R.string.error_not_found)

    is AppError.Http.TooManyRequests ->
        UiText.Res(R.string.error_too_many_requests)

    is AppError.Http.Server ->
        UiText.Res(R.string.error_server)

    is AppError.Http.Client ->
        UiText.Res(R.string.error_client)

    /* ========= AUTH ========= */
    is AppError.Auth ->
        UiText.Res(R.string.error_unauthorized)

    /* ========= LOCAL ========= */
    is AppError.Local ->
        UiText.Res(R.string.error_local)

    /* ========= SERIALIZATION ========= */
    AppError.Serialization ->
        UiText.Res(R.string.error_serialization)

    /* ========= FALLBACK ========= */
    is AppError.Unknown ->
        UiText.Res(R.string.error_unknown)

}