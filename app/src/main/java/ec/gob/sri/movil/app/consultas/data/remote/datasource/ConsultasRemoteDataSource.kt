package ec.gob.sri.movil.app.consultas.data.remote.datasource

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel

interface ConsultasRemoteDataSource {
    suspend fun getConsultasApi(): List<ConsultasModel>
}