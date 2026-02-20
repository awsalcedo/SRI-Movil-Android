package ec.gob.sri.movil.app.feature.deudas.domain.repository

import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudaPorDenominacionDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult

interface DeudasRepository {
    suspend fun consultarDeudasPorRuc(ruc: String): DataResult<DeudasDomain, AppError>
    suspend fun consultarPorNombre(
        nombre: String,
        tipoPersona: String,
        resultados: Int
    ): DataResult<List<DeudaPorDenominacionDomain>, AppError>

    suspend fun consultarPorIdentificacion(
        identificacion: String,
        tipoPersona: String
    ): DataResult<DeudasDomain, AppError>

    fun getCacheConsultaPorIdentificacion(
        identificacion: String,
        tipoPersona: String
    ): DeudasDomain?

}