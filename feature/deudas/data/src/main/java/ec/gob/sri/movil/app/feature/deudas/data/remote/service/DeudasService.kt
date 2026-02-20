package ec.gob.sri.movil.app.feature.deudas.data.remote.service

import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudaPorDenominacionDto
import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudasDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeudasService {
    @GET("v1.0/deudas/porIdentificacion/{ruc}")
    suspend fun consultarDeudasPorRucApi(@Path("ruc") ruc: String): Response<DeudasDto>

    @GET("v1.0/deudas/porDenominacion/{nombre}/")
    suspend fun consultarPorNombreApi(
        @Path("nombre") nombre: String,
        @Query("tipoPersona") tipoPersona: String,
        @Query("resultados") resultados: Int
    ): Response<List<DeudaPorDenominacionDto>>

    @GET("v1.0/deudas/porIdentificacion/{identificacion}")
    suspend fun consultarPorIdentificacionApi(
        @Path("identificacion") identificacion: String,
        @Query("tipoPersona") tipoPersona: String
    ): Response<DeudasDto>
}