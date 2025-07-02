package ec.gob.sri.movil.app.login.data.remote.datasource

import ec.gob.sri.movil.app.login.domain.models.LoginDomain

interface LoginRemoteDataSource {
    suspend fun login(authorization: String): LoginDomain
}