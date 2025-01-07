package ec.gob.sri.movil.app.consultas.data.remote.datasource

import ec.gob.sri.movil.app.consultas.data.mappers.toDomain
import ec.gob.sri.movil.app.consultas.data.remote.dto.ConsultasService
import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ConsultasRemoteDataSourceImpl(
    private val api: ConsultasService,
    private val ioDispatcher: CoroutineDispatcher
) : ConsultasRemoteDataSource {
    override suspend fun getConsultasApi(): List<ConsultasModel> =
        withContext(ioDispatcher) {
            api.getConsultasApi().map { it.toDomain() }
        }
}