package ec.gob.sri.movil.app.consultas.data.remote.datasource

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import kotlinx.coroutines.flow.Flow

interface ConsultasRemoteDataSource {
    suspend fun getConsultasApi(): Flow<List<ConsultasModel>>
}