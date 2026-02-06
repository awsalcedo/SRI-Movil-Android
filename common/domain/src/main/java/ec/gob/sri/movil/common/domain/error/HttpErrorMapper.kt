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