package ec.gob.sri.movil.feature.estadotributario.data.remote.service

import ec.gob.sri.movil.feature.estadotributario.data.remote.dto.EstadoTributarioDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EstadoTributarioService {
    @GET("v1.0/estadoTributario/{ruc}")
    suspend fun consultarApi(@Path("ruc") ruc: String): Response<EstadoTributarioDto>
}