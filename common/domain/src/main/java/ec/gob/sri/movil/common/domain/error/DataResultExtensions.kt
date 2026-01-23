package ec.gob.sri.movil.common.domain.error

/**
 * Transforma el dato de un [DataResult.Success] manteniendo el error intacto en caso de [DataResult.Error].
 *
 * Útil para mapear DTO → Domain, Entity → Domain, o cualquier transformación del "data"
 * sin duplicar lógica de manejo de errores.
 *
 * Ejemplo:
 * val domainResult = dtoResult.map { it.toDomain() }
 *
 * @param transform Función que transforma el dato [T] a [R].
 * @return Un [DataResult] con el dato transformado o el mismo error original.
 */
inline fun <T, E : AppError, R> DataResult<T, E>.map(transform: (T) -> R): DataResult<R, E> =
    when (this) {
        is DataResult.Success -> DataResult.Success(transform(data))
        is DataResult.Error -> this
    }

/**
 * Ejecuta [action] únicamente cuando el resultado es [DataResult.Success], y retorna el mismo resultado.
 *
 * Ideal para ejecutar efectos colaterales (side-effects) de manera declarativa:
 * cacheo en memoria, trazas, métricas, etc., sin modificar el valor.
 *
 * Ejemplo:
 * repo.getX().onSuccess { cache.save(it) }
 *
 * @param action Acción a ejecutar con el dato exitoso.
 * @return El mismo [DataResult] original.
 */
inline fun <T, E : AppError> DataResult<T, E>.onSuccess(action: (T) -> Unit): DataResult<T, E> =
    when (this) {
        is DataResult.Success -> {
            action(data); this
        }

        is DataResult.Error -> this
    }

/**
 * Ejecuta [action] únicamente cuando el resultado es [DataResult.Error], y retorna el mismo resultado.
 *
 * Útil para log, métricas o para disparar decisiones sin romper el flujo funcional.
 *
 * Ejemplo:
 * repo.getX().onError { logger.e(it) }
 *
 * @param action Acción a ejecutar con el error.
 * @return El mismo [DataResult] original.
 */
inline fun <T, E : AppError> DataResult<T, E>.onError(action: (E) -> Unit): DataResult<T, E> =
    when (this) {
        is DataResult.Error -> {
            action(error); this
        }

        is DataResult.Success -> this
    }