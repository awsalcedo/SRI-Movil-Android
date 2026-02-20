package ec.gob.sri.movil.app.feature.deudas.data.remote.repository

import ec.gob.sri.movil.app.feature.deudas.data.mapper.toDomain
import ec.gob.sri.movil.app.feature.deudas.data.remote.datasource.DeudasRemoteDataSource
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudaPorDenominacionDomain
import ec.gob.sri.movil.app.feature.deudas.domain.models.DeudasDomain
import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import ec.gob.sri.movil.common.domain.error.AppError
import ec.gob.sri.movil.common.domain.error.DataResult
import ec.gob.sri.movil.common.domain.error.map
import javax.inject.Inject

class DeudasRepositoryImpl @Inject constructor(private val remoteDataSource: DeudasRemoteDataSource) :
    DeudasRepository {
    private data class CacheKey(val identificacion: String, val tipoPersona: String)

    private var lastKey: CacheKey? = null
    private var last: DeudasDomain? = null
    override suspend fun consultarDeudasPorRuc(ruc: String): DataResult<DeudasDomain, AppError> {
        return remoteDataSource.consultarDeudasPorRuc(ruc = ruc)
            .map { dto -> dto.toDomain() }
    }

    override suspend fun consultarPorNombre(
        nombre: String,
        tipoPersona: String,
        resultados: Int
    ): DataResult<List<DeudaPorDenominacionDomain>, AppError> {
        return remoteDataSource.consultarPorNombre(
            nombre = nombre,
            tipoPersona = tipoPersona,
            resultados = resultados
        ).map { dtos -> dtos.map { dto -> dto.toDomain() } }
    }

    override suspend fun consultarPorIdentificacion(
        identificacion: String,
        tipoPersona: String
    ): DataResult<DeudasDomain, AppError> {
        return remoteDataSource.consultarPorIdentificacion(
            identificacion = identificacion,
            tipoPersona = tipoPersona
        ).map { dto ->
            dto.toDomain().also { domain ->
                last = domain
                lastKey = CacheKey(identificacion = identificacion, tipoPersona = tipoPersona)
            }
        }
    }

    override fun getCacheConsultaPorIdentificacion(
        identificacion: String,
        tipoPersona: String
    ): DeudasDomain? =
        last?.takeIf { lastKey == CacheKey(identificacion, tipoPersona) }

}