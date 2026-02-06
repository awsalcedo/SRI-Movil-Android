package ec.gob.sri.movil.app.feature.deudas.data.remote.service

import ec.gob.sri.movil.app.feature.deudas.data.remote.dto.DeudasDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DeudasService {
    @GET("v1.0/deudas/porIdentificacion/{ruc}")
    suspend fun consultarDeudasApi(@Path("ruc") ruc: String): Response<DeudasDto>
}