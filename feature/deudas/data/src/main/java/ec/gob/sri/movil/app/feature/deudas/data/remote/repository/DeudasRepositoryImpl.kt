package ec.gob.sri.movil.app.feature.deudas.data.remote.repository

import ec.gob.sri.movil.app.feature.deudas.data.mapper.toDomain
import ec.gob.sri.movil.app.feature.deudas.data.remote.datasource.DeudasRemoteDataSource
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.domain.error.map
import javax.inject.Inject

class DeudasRepositoryImpl @Inject constructor(private val remoteDataSource: DeudasRemoteDataSource) :
    DeudasRepository {
    override suspend fun consultarDeudas(ruc: String): DataResult<DeudasDomain, AppError> {
        return remoteDataSource.consultarDeudasApi(ruc = ruc)
            .map { dto -> dto.toDomain() }
    }
}