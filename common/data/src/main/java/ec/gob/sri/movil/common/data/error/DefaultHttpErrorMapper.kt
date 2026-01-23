package ec.gob.sri.movil.common.data.error

import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper

/**
 * Implementación por defecto del mapeo HTTP hacia [AppError].
 *
 * Reglas:
 * - Maneja códigos frecuentes de manera explícita (401, 403, 404, 429).
 * - Agrupa 5xx como [AppError.Http.Server].
 * - Agrupa 4xx (no cubiertos) como [AppError.Http.Client].
 * - Usa [AppError.Unknown] como fallback defensivo.
 *
 * Nota:
 * - `errorBody` se conserva como `message` para logging/diagnóstico.
 * - Si se desea UX basada en códigos de negocio del backend, se recomienda parsear `errorBody`
 *   a un DTO en otra implementación del mapper.
 */
class DefaultHttpErrorMapper : HttpErrorMapper {
    override fun map(code: Int, errorBody: String?): AppError {
        return when (code) {
            401 -> AppError.Http.Unauthorized(errorBody)
            403 -> AppError.Http.Forbidden(errorBody)
            404 -> AppError.Http.NotFound(errorBody)
            429 -> AppError.Http.TooManyRequests(errorBody)
            in 500..599 -> AppError.Http.Server(code, errorBody)
            in 400..499 -> AppError.Http.Client(code, errorBody)
            else -> AppError.Unknown("Unexpected HTTP $code: ${errorBody ?: ""}".trim())
        }
    }
}