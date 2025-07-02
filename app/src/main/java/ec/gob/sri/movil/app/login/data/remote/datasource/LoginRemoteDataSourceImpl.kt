package ec.gob.sri.movil.app.login.data.remote.datasource

import ec.gob.sri.movil.app.login.data.mapper.toDomain
import ec.gob.sri.movil.app.login.data.remote.service.LoginApiService
import ec.gob.sri.movil.app.login.domain.models.LoginDomain
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(private val api: LoginApiService): LoginRemoteDataSource {
    override suspend fun login(authorization: String): LoginDomain {
        return api.login(authorization).toDomain()
    }
}