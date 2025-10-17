package ec.gob.sri.movil.app.core.domain.error

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Extension functions for DataResult to provide convenient operations
 * and integration with Flow and coroutines.
 */

/**
 * Maps a Flow of DataResult to extract only the success data, filtering out errors
 */
fun <T, E : DataError> Flow<DataResult<T, E>>.successData(): Flow<T> =
    map { result -> result.getOrNull() }.filterNotNull()

/**
 * Maps a Flow of DataResult to extract only the errors, filtering out successes
 */
fun <T, E : DataError> Flow<DataResult<T, E>>.errors(): Flow<E> =
    map { result -> result.getErrorOrNull() }.filterNotNull()

/**
 * Converts a Flow of DataResult to a Flow that emits the data on success
 * or completes with an exception on error
 */
fun <T, E : DataError> Flow<DataResult<T, E>>.toDataFlow(): Flow<T> =
    map { result ->
        when (result) {
            is DataResult.Success -> result.data
            is DataResult.Error -> throw DataResultException(result.error)
        }
    }

/**
 * Wraps a DataResult.Error in an exception for throwing
 */
class DataResultException(val error: DataError) : Exception("DataResult.Error: $error")

/**
 * Creates a success DataResult with the given data
 */
fun <T> T.toSuccessResult(): DataResult<T, Nothing> = DataResult.Success(this)

/**
 * Creates an error DataResult with the given error
 */
fun <E : DataError> E.toErrorResult(): DataResult<Nothing, E> = DataResult.Error(this)

/**
 * Creates a DataResult from a nullable value, returning an error if null
 */
fun <T, E : DataError> T?.toResultOrError(error: E): DataResult<T, E> =
    if (this != null) DataResult.Success(this) else DataResult.Error(error)

/**
 * Extension to safely execute a block and wrap any exceptions in a DataResult.Error
 */
inline fun <T, E : DataError> Result<T>.toDataResult(
    errorMapper: (Throwable) -> E
): DataResult<T, E> = fold(
    onSuccess = { DataResult.Success(it) },
    onFailure = { DataResult.Error(errorMapper(it)) }
)

/**
 * Extension to safely execute a suspend block and wrap any exceptions in a DataResult.Error
 */
suspend inline fun <T, E : DataError> safeDataResult(
    errorMapper: (Throwable) -> E,
    crossinline block: suspend () -> T
): DataResult<T, E> = try {
    DataResult.Success(block())
} catch (e: Exception) {
    DataResult.Error(errorMapper(e))
}

/**
 * Extension to combine multiple DataResults into a single one
 */
fun <T1, T2, E : DataError> DataResult<T1, E>.combine(
    other: DataResult<T2, E>,
    combine: (T1, T2) -> Pair<T1, T2>
): DataResult<Pair<T1, T2>, E> = when {
    this is DataResult.Success && other is DataResult.Success -> 
        DataResult.Success(combine(this.data, other.data))
    this is DataResult.Error -> this
    else -> other
}

/**
 * Extension to transform a list of DataResults into a single DataResult with a list
 */
fun <T, E : DataError> List<DataResult<T, E>>.combineAll(): DataResult<List<T>, E> {
    val successes = mutableListOf<T>()
    val errors = mutableListOf<E>()
    
    forEach { result ->
        when (result) {
            is DataResult.Success -> successes.add(result.data)
            is DataResult.Error -> errors.add(result.error)
        }
    }
    
    return if (errors.isEmpty()) {
        DataResult.Success(successes)
    } else {
        DataResult.Error(errors.first()) // Return first error
    }
}

/**
 * Extension to filter null values from a Flow
 */
private fun <T> Flow<T?>.filterNotNull(): Flow<T> = 
    map { it }.filter { it != null }.map { it!! }
