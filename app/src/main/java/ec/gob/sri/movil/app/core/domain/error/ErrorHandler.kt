package ec.gob.sri.movil.app.core.domain.error

import retrofit2.HttpException

interface ErrorHandler {
    fun handleNetWorkError(throwable: Throwable): DataError.Network
    fun parseHttpError(exception: HttpException): String
}