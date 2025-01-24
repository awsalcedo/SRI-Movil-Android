package ec.gob.sri.movil.app.consultas.data.repository

import ec.gob.sri.movil.app.consultas.data.local.datasource.ConsultasLocalDataSource
import ec.gob.sri.movil.app.consultas.data.remote.datasource.ConsultasRemoteDataSource
import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import ec.gob.sri.movil.app.consultas.domain.repository.ConsultasRepository
import kotlinx.coroutines.flow.Flow

class ConsultasRepositoryImpl(
    private val remoteDataSource: ConsultasRemoteDataSource,
    private val localDataSource: ConsultasLocalDataSource
) : ConsultasRepository {

    override suspend fun getConsultas(): Flow<List<ConsultasModel>> {

        return try {
            if ( localDataSource.getCountConsultasLocal() == 0 ) {
                remoteDataSource.getConsultasApi()
            } else {
                localDataSource.getConsultasLocal()
            }
        } catch (e: Exception) {
            remoteDataSource.getConsultasApi()
        }





    }
}