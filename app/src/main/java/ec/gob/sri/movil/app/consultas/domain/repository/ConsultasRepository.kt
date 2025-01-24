package ec.gob.sri.movil.app.consultas.domain.repository

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import kotlinx.coroutines.flow.Flow

interface ConsultasRepository {
    suspend fun getConsultas(): Flow<List<ConsultasModel>>
}

