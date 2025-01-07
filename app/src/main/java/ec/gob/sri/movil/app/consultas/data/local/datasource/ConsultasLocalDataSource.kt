package ec.gob.sri.movil.app.consultas.data.local.datasource

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import kotlinx.coroutines.flow.Flow

interface ConsultasLocalDataSource {
    fun getConsultasLocal(): Flow<List<ConsultasModel>>
}