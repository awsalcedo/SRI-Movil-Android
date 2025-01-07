package ec.gob.sri.movil.app.consultas.domain.repository

import ec.gob.sri.movil.app.consultas.domain.model.ConsultasModel

interface ConsultasRepository {
    suspend fun getConsultas(): List<ConsultasModel>
}

