package ec.gob.sri.movil.common.domain.error

/**
 * Modelo de errores de la aplicación.
 *
 * Define un contrato estable y tipado para representar fallos provenientes de:
 * - Red (conectividad/tiempo de espera/servicio no disponible)
 * - HTTP (códigos y respuestas de backend)
 * - Persistencia local (base de datos/almacenamiento)
 * - Sesión/autenticación (estado inválido o refresh rechazado)
 * - Serialización (parseo de JSON u otros formatos)
 * - Desconocidos (fallback)
 *
 * Este modelo permite:
 * - Centralizar el mapeo desde excepciones o códigos HTTP a un error de dominio
 * - Evitar acoplamiento de la UI a Retrofit/OkHttp/excepciones
 * - Definir una UX consistente (por ejemplo: convertir [AppError] a mensajes UI)
 */
sealed interface AppError {
    /* ========= NETWORK ========= */

    /**
     * Errores relacionados con conectividad o disponibilidad de red.
     */
    sealed interface Network : AppError {
        /** No hay conectividad (DNS/host no resuelto / sin acceso a internet). */
        data object NoInternet : Network
        /** La operación excedió el tiempo de espera. */
        data object Timeout : Network
        /** La red/servicio no está disponible o falló la conexión (IO genérico). */
        data object Unavailable : Network
    }

    /* ========= HTTP ========= */

    /**
     * Errores HTTP provenientes del backend.
     *
     * Se incluyen casos específicos frecuentes y categorías genéricas:
     * - [Server] para 5xx
     * - [Client] para 4xx no cubiertos por casos específicos
     *
     * [message] puede contener el cuerpo del error en bruto u otra descripción.
     */
    sealed interface Http : AppError {
        data class Unauthorized(val message: String? = null) : Http
        data class Forbidden(val message: String? = null) : Http
        data class NotFound(val message: String? = null) : Http
        data class TooManyRequests(val message: String? = null) : Http
        data class Server(val code: Int, val message: String? = null) : Http
        data class Client(val code: Int, val message: String? = null) : Http
    }

    /* ========= LOCAL ========= */

    /**
     * Errores originados en almacenamiento/persistencia local.
     */
    sealed interface Local : AppError {
        /**
         * Errores relacionados con base de datos.
         */
        sealed interface Database : Local {
            /** Violación de constraint: unique, foreign key, etc. */
            data object Constraint : Database
            /** No hay espacio disponible en disco. */
            data object DiskFull : Database
            /** Base corrupta o inconsistente. */
            data object Corruption : Database
            /** Base bloqueada por concurrencia/locking. */
            data object Locked : Database
            /** Error local no clasificado con detalle opcional. */
            data class Unknown(val message: String? = null) : Database
        }
    }

    /* ========= AUTH/SESSION ========= */

    /**
     * Errores relacionados con autenticación/sesión.
     *
     * Se usan cuando no existe una sesión válida, falta refresh token o el backend
     * rechaza el refresh (por ejemplo 401/invalid token).
     */
    sealed interface Auth : AppError {
        data object MissingSession : Auth      // no access/refresh, user no logueado o estado corrupto
        data object MissingRefreshToken : Auth // refresh vacío o null
        data object RefreshRejected : Auth     // backend rechazó refresh (401/invalid)
    }

    /* ========= SERIALIZATION ========= */

    /**
     * Fallo al parsear/serializar datos (por ejemplo JSON inválido o estructura inesperada).
     */
    data object Serialization : AppError

    /* ========= FALLBACK ========= */

    /**
     * Error no clasificado. Se usa como fallback defensivo.
     */
    data class Unknown(val message: String? = null) : AppError
}