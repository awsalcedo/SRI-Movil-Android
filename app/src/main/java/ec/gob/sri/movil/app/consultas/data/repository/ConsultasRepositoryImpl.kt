package ec.gob.sri.movil.app.consultas.data.repository

import ec.gob.sri.movil.app.consultas.data.remote.datasource.ConsultasRemoteDataSource
import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import ec.gob.sri.movil.app.consultas.domain.repository.ConsultasRepository

class ConsultasRepositoryImpl(
    private val remoteDataSource: ConsultasRemoteDataSource
) : ConsultasRepository {
    override suspend fun getConsultas(): List<ConsultasModel> = remoteDataSource.getConsultasApi()
}