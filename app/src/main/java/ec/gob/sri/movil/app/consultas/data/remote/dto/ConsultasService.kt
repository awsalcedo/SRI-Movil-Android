package ec.gob.sri.movil.app.consultas.data.remote.dto

import retrofit2.http.GET

interface ConsultasService {
    @GET("/api/consultas")
    suspend fun getConsultasApi(): List<ConsultasDto>
}