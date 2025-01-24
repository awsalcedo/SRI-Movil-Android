package ec.gob.sri.movil.app.consultas.data.remote.datasource

import ec.gob.sri.movil.app.consultas.data.mappers.toDomain
import ec.gob.sri.movil.app.consultas.data.remote.dto.ConsultasService
import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ConsultasRemoteDataSourceImpl(
    private val api: ConsultasService,
    private val ioDispatcher: CoroutineDispatcher
) : ConsultasRemoteDataSource {
    override suspend fun getConsultasApi(): Flow<List<ConsultasModel>> =
        withContext(ioDispatcher) {
            flow { api.getConsultasApi().map { it.toDomain() } }
        }
}