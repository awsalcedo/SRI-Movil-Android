package ec.gob.sri.movil.app.estadotributario.data.remote.datasource

import ec.gob.sri.movil.app.core.domain.Result
import ec.gob.sri.movil.app.estadotributario.data.mapper.toDomain
import ec.gob.sri.movil.app.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain
import javax.inject.Inject

class EstadoTributarioRemoteDataSourceImpl @Inject constructor(
    private val apiService: EstadoTributarioService
) : EstadoTributarioRemoteDataSource {
    
    override suspend fun consultarEstadoTributarioApi(ruc: String): Result<EstadoTributarioDomain> {
        return try {
            val response = apiService.consultarApi(ruc)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body.toDomain())
                } else {
                    Result.Error(Exception("Response body is null"))
                }
            } else {
                Result.Error(Exception("API returned error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}