package ec.gob.sri.movil.app.feature.deudas.data.remote.datasource

import DeudasDto
//import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudasDto
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult


interface DeudasRemoteDataSource {
    suspend fun consultarDeudasApi(ruc: String): DataResult<DeudasDto, AppError>
}