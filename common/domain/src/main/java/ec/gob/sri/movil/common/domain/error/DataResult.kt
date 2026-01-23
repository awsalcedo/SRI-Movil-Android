package ec.gob.sri.movil.common.domain.error

/**
 * Representa el resultado de una operación que puede:
 *
 * - Completar con éxito devolviendo un dato [T]
 * - Fallar devolviendo un error tipado [E]
 *
 * Este tipo es útil para estandarizar la comunicación entre capas (Data → Domain → UI),
 * evitando excepciones como control de flujo y permitiendo manejo explícito de errores.
 *
 * @param T Tipo del dato en caso de éxito.
 * @param E Tipo del error (debe implementar [AppError]).
 */
sealed class DataResult<out T, out E : AppError> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>()
    data class Error<out E : AppError>(val error: E) : DataResult<Nothing, E>()
}