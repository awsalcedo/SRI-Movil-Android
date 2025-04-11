package ec.gob.sri.movil.app.consultas.data.remote.datasource

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.error.DataResult
import kotlinx.coroutines.flow.Flow

interface ConsultasRemoteDataSource {
    fun getConsultasApi(): Flow<DataResult<List<ConsultasModel>, DataError>>
}