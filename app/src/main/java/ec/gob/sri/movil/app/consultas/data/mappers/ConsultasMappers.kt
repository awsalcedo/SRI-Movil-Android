package ec.gob.sri.movil.app.consultas.data.mappers

import ec.gob.sri.movil.app.consultas.data.remote.dto.ConsultasDto
import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel

fun ConsultasDto.toDomain() = ConsultasModel(
    id = id,
    nombre = nombre,
    posicion = posicion,
    habilitado = habilitado,
    autenticada = autenticada
)