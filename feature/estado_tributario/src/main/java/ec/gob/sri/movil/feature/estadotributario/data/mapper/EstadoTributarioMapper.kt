package ec.gob.sri.movil.feature.estadotributario.data.mapper

import ec.gob.sri.movil.feature.estadotributario.data.remote.dto.EstadoTributarioDto
import ec.gob.sri.movil.feature.estadotributario.data.remote.dto.ObligacionesPendientesDto
import ec.gob.sri.movil.feature.estadotributario.domain.models.EstadoTributarioDomain
import ec.gob.sri.movil.feature.estadotributario.domain.models.ObligacionesPendientesDomain

fun EstadoTributarioDto.toDomain(): EstadoTributarioDomain {
    return EstadoTributarioDomain(
        ruc = this.ruc,
        razonSocial = this.razonSocial,
        descripcion = this.descripcion,
        plazoVigenciaDoc = this.plazoVigenciaDoc,
        claseContribuyente = this.claseContribuyente,
        obligacionesPendientes = this.obligacionesPendientes.orEmpty().map { it.toDomain() }
    )
}

fun ObligacionesPendientesDto.toDomain(): ObligacionesPendientesDomain {
    return ObligacionesPendientesDomain(
        descripcion = this.descripcion,
        periodos = this.periodos
    )
}