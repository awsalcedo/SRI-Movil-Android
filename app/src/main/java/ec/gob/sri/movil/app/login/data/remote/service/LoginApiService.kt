package ec.gob.sri.movil.app.login.data.remote.service

import ec.gob.sri.movil.app.login.data.remote.dto.LoginResponseDto
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {

    @POST("v2.0/secured")
    suspend fun login(@Header ("Authorization") authorization: String): LoginResponseDto
}