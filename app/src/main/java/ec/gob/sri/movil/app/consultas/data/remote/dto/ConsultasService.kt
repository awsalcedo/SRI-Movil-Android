package ec.gob.sri.movil.app.consultas.data.remote.dto

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ConsultasService {
    @GET("/api/consultas")
    fun getConsultasApi(): Flow<List<ConsultasDto>>
}