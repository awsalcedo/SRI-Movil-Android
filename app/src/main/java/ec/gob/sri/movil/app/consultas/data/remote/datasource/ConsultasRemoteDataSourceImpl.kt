package ec.gob.sri.movil.app.consultas.data.remote.datasource

import ec.gob.sri.movil.app.consultas.data.mappers.toDomain
import ec.gob.sri.movil.app.consultas.data.remote.dto.ConsultasService
import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import ec.gob.sri.movil.app.core.domain.error.DataError
import ec.gob.sri.movil.app.core.domain.error.DataResult
import ec.gob.sri.movil.app.core.domain.error.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConsultasRemoteDataSourceImpl @Inject constructor(
    private val api: ConsultasService,
    private val errorHandler: ErrorHandler,
    private val ioDispatcher: CoroutineDispatcher
) : ConsultasRemoteDataSource {
    override fun getConsultasApi(): Flow<DataResult<List<ConsultasModel>, DataError>> =
        api.getConsultasApi().map { listDto ->
            DataResult.Success(listDto.map { dto ->
                dto.toDomain()
            })
        }.catch { e ->
            DataResult.Error(errorHandler.handleNetWorkError(e))

        }.flowOn(ioDispatcher)
}