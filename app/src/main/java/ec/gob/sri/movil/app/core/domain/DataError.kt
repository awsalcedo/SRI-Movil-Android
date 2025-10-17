package ec.gob.sri.movil.app.core.domain

/**
 * Simple DataError like Philip Lackner's implementation
 */
sealed interface DataError {
    sealed interface Network : DataError {
        data object NoInternet : Network
        data object Timeout : Network
        data object Unknown : Network
    }
    
    sealed interface Local : DataError {
        data object Database : Local
        data object Unknown : Local
    }
}
