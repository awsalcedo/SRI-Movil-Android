package ec.gob.sri.movil.app.feature.deudas.data.mapper

import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.ContribuyenteDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DetalleRubroDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudaDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudaPorDenominacionDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudasDto
import ec.gob.sri.movil.app.feature.deudas.domain.models.ContribuyenteDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DetalleRubroDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudaDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudaPorDenominacionDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain


fun DeudasDto.toDomain(): DeudasDomain {
    return DeudasDomain(
        contribuyente = this.contribuyente.toDomain(),
        deuda = this.deuda?.toDomain(),
        impugnacion = this.impugnacion,
        remision = this.remision
    )
}

fun ContribuyenteDto.toDomain(): ContribuyenteDomain {
    return ContribuyenteDomain(
        identificacion = this.identificacion,
        denominacion = this.denominacion,
        tipo = this.tipo,
        clase = this.clase,
        tipoIdentificacion = this.tipoIdentificacion,
        resolucion = this.resolucion,
        nombreComercial = this.nombreComercial,
        direccionMatriz = this.direccionMatriz,
        fechaInformacion = this.fechaInformacion,
        mensaje = this.mensaje,
        estado = this.estado
    )
}

fun DeudaDto.toDomain(): DeudaDomain {
    return DeudaDomain(
        descripcion = this.descripcion,
        valor = this.valor,
        periodoFiscal = this.periodoFiscal,
        beneficiario = this.beneficiario,
        detallesRubro = this.detallesRubro?.map { it.toDomain() }
    )
}

fun DetalleRubroDto.toDomain(): DetalleRubroDomain {
    return DetalleRubroDomain(
        descripcion = this.descripcion,
        anio = this.anio,
        valor = this.valor
    )
}

fun DeudaPorDenominacionDto.toDomain(): DeudaPorDenominacionDomain {
    return DeudaPorDenominacionDomain(
        identificacion = this.identificacion,
        denominacion = this.denominacion,
        tipo = this.tipo,
        clase = this.clase,
        tipoIdentificacion = this.tipoIdentificacion,
        resolucion = this.resolucion,
        nombreComercial = this.nombreComercial,
        direccionMatriz = this.direccionMatriz,
        fechaInformacion = this.fechaInformacion,
        mensaje = this.mensaje,
        estado = this.estado
    )
}