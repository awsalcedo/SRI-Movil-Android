package ec.gob.sri.movil.common.data.networking

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val jsonLenient = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

/**
 * Intenta extraer un mensaje "user-friendly" del errorBody (JSON) del backend.
 * Si no puede, devuelve el raw original.
 */
fun normalizeErrorBody(raw: String?): String? {
    if (raw.isNullOrBlank()) return null

    // Si el backend ya devolvió texto plano, no intentes parsear.
    val trimmed = raw.trim()
    val looksLikeJson = trimmed.startsWith("{") && trimmed.endsWith("}")
    if (!looksLikeJson) return trimmed

    val extracted = runCatching {
        val obj = jsonLenient.parseToJsonElement(trimmed).jsonObject

        // Prioridad: el contrato que viste en SRI
        obj["mensaje"]?.jsonPrimitive?.contentOrNull
        // Fallbacks comunes por si otros endpoints usan otro nombre
            ?: obj["message"]?.jsonPrimitive?.contentOrNull
            ?: obj["detail"]?.jsonPrimitive?.contentOrNull
            ?: obj["error"]?.jsonPrimitive?.contentOrNull
    }.getOrNull()

    return extracted?.takeIf { it.isNotBlank() } ?: trimmed
}