package ec.gob.sri.movil.feature.estadotributario.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper
import ec.gob.sri.movil.feature.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.feature.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSourceImpl
import ec.gob.sri.movil.feature.estadotributario.data.remote.repository.EstadoTributarioRepositoryImpl
import ec.gob.sri.movil.feature.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.feature.estadotributario.domain.repository.EstadoTributarioRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EstadoTributarioModule {

    @Provides
    @Singleton
    fun provideEstadoTributarioService(retrofit: Retrofit): EstadoTributarioService =
        retrofit.create(
            EstadoTributarioService::class.java
        )


    @Provides
    @Singleton
    fun provideEstadoTributarioDataSource(
        service: EstadoTributarioService,
        httpErrorMapper: HttpErrorMapper
    ): EstadoTributarioRemoteDataSource {
        return EstadoTributarioRemoteDataSourceImpl(
            service = service,
            httpErrorMapper = httpErrorMapper
        )
    }

    @Provides
    @Singleton
    fun provideEstadoTributarioRepository(remoteDataSource: EstadoTributarioRemoteDataSource): EstadoTributarioRepository {
        return EstadoTributarioRepositoryImpl(remoteDataSource)
    }

}