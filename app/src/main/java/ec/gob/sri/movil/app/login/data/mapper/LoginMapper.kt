package ec.gob.sri.movil.app.login.data.mapper

import ec.gob.sri.movil.app.login.data.remote.dto.LoginResponseDto
import ec.gob.sri.movil.app.login.domain.models.LoginDomain

fun LoginResponseDto.toDomain(): LoginDomain {
    return LoginDomain(
        token = contenido
    )
}