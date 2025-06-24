package ec.gob.sri.movil.app.consultas.domain.repository

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel
import kotlinx.coroutines.flow.Flow

interface ConsultasRepository {
    fun getConsultas(): Flow<List<ConsultasModel>>
}

