package ec.gob.sri.movil.common.framework.ui.format

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Extensiones de formateo de fechas para capa de UI.
 *
 * Ubicación: common:framework (porque es lógica de presentación)
 * - No pertenece a domain.
 * - No depende de Android.
 * - Reutilizable en múltiples features.
 */

private val ecuadorLocale: Locale = Locale.Builder()
    .setLanguage("es")
    .setRegion("EC")
    .build()

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter
        .ofPattern("dd/MM/yyyy", ecuadorLocale)

/**
 * Convierte epoch millis a formato dd/MM/yyyy.
 *
 * Ej: 1739913600000 -> "19/02/2026"
 */
fun Long.toFechaCorte(): String {
    return try {
        Instant
            .ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    } catch (_: Exception) {
        "-"
    }
}

/**
 * Convierte epoch millis a fecha con hora.
 *
 * Ej: 1739913600000 -> "19/02/2026 14:35"
 */
fun Long.toFechaHora(): String {
    return try {
        val formatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm", ecuadorLocale)

        Instant
            .ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    } catch (_: Exception) {
        "-"
    }
}