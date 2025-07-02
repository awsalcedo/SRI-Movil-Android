package ec.gob.sri.movil.app.login.data.remote.dto

import kotlinx.serialization.SerialName

data class LoginResponseDto(
    @SerialName("contenido")
    val contenido: String
)
