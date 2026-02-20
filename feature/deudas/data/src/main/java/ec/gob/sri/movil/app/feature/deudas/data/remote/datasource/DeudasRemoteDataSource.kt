package ec.gob.sri.movil.app.feature.deudas.data.remote.datasource

import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudaPorDenominacionDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudasDto
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult


interface DeudasRemoteDataSource {
    suspend fun consultarDeudasPorRuc(ruc: String): DataResult<DeudasDto, AppError>
    suspend fun consultarPorNombre(
        nombre: String,
        tipoPersona: String,
        resultados: Int
    ): DataResult<List<DeudaPorDenominacionDto>, AppError>

    suspend fun consultarPorIdentificacion(
        identificacion: String,
        tipoPersona: String
    ): DataResult<DeudasDto, AppError>
}