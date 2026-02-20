package ec.gob.sri.movil.common.framework.ui.format

import java.text.NumberFormat
import java.util.Locale

/**
 * Extensiones de formateo de moneda para capa de UI.
 *
 * Ubicación: common:framework (presentation layer)
 * - No pertenece a domain.
 * - Reutilizable por múltiples features.
 * - No depende de Android.
 */

private val ecuadorLocale: Locale = Locale.Builder()
    .setLanguage("es")
    .setRegion("EC")
    .build()

private fun ecuadorNumberFormatter(): NumberFormat {
    return NumberFormat.getNumberInstance(ecuadorLocale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
}

/**
 * Formatea un Double como moneda estilo SRI:
 *
 * Ej:
 * 0.01 -> "0,01 USD"
 * 1500.5 -> "1.500,50 USD"
 */
fun Double.toUsdEc(): String {
    return try {
        "${ecuadorNumberFormatter().format(this)} USD"
    } catch (_: Exception) {
        "- USD"
    }
}

/**
 * Variante que permite controlar si mostrar USD o no.
 */
fun Double.toEcNumber(): String {
    return try {
        ecuadorNumberFormatter().format(this)
    } catch (_: Exception) {
        "-"
    }
}