package ec.gob.sri.movil.common.domain.error

/**
 * Contrato para mapear respuestas HTTP no exitosas a un [AppError] de dominio.
 *
 * Motivación:
 * - Aislar el mapeo HTTP del resto de la lógica de networking.
 * - Permitir extender/ajustar reglas (por ejemplo, manejar 422, 409, 400) sin modificar `safeCall`.
 * - Mantener una salida estable para capas superiores (Domain/UI) sin acoplarse a Retrofit.
 *
 * Implementaciones típicas:
 * - Un mapper por módulo/feature si los endpoints devuelven formatos de error distintos.
 * - Un mapper por entorno si existen reglas diferentes.
 */
interface HttpErrorMapper {
    /**
     * Convierte un código HTTP y el cuerpo de error (si existe) a un [AppError].
     *
     * @param code Código HTTP de la respuesta (por ejemplo 401, 404, 500).
     * @param errorBody Cuerpo de error en bruto (puede ser null o JSON sin parsear).
     * @return Un [AppError] tipado para el dominio.
     */
    fun map(code: Int, errorBody: String?): AppError
}

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
/*object DefaultHttpErrorMapper : HttpErrorMapper {
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
}*/