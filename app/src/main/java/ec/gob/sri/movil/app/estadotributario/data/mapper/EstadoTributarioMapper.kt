package ec.gob.sri.movil.app.estadotributario.data.mapper

import ec.gob.sri.movil.app.estadotributario.data.remote.dto.EstadoTributarioDto
import ec.gob.sri.movil.app.estadotributario.data.remote.dto.ObligacionesPendientesDto
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.app.estadotributario.domain.models.ObligacionesPendientesDomain

fun EstadoTributarioDto.toDomain(): EstadoTributarioDomain {
    return EstadoTributarioDomain(
        ruc = this.ruc,
        razonSocial = this.razonSocial,
        descripcion = this.descripcion,
        plazoVigenciaDoc = this.plazoVigenciaDoc,
        claseContribuyente = this.claseContribuyente,
        obligacionesPendientes = this.obligacionesPendientes.map { it.toDomain() }
    )
}

fun ObligacionesPendientesDto.toDomain(): ObligacionesPendientesDomain {
    return ObligacionesPendientesDomain(
        descripcion = this.descripcion,
        periodos = this.periodos
    )
}