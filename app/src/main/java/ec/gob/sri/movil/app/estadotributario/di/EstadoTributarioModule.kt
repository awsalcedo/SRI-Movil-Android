package ec.gob.sri.movil.app.estadotributario.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.app.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSource
import ec.gob.sri.movil.app.estadotributario.data.remote.datasource.EstadoTributarioRemoteDataSourceImpl
import ec.gob.sri.movil.app.estadotributario.data.remote.repository.EstadoTributarioRepositoryImpl
import ec.gob.sri.movil.app.estadotributario.data.remote.service.EstadoTributarioService
import ec.gob.sri.movil.app.estadotributario.domain.repository.EstadoTributarioRepository
import ec.gob.sri.movil.app.estadotributario.domain.usecase.EstadoTributarioUseCase
import ec.gob.sri.movil.app.estadotributario.domain.usecase.ObtenerEstadoTributarioUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EstadoTributarioModule {
    
    @Provides
    @Singleton
    fun provideEstadoTributarioUseCase(estadoTributarioRepository: EstadoTributarioRepository): EstadoTributarioUseCase {
        return EstadoTributarioUseCase(
            obtenerEstadoTributarioUseCase = ObtenerEstadoTributarioUseCase(
                repository = estadoTributarioRepository
            )
        )
    }

    @Provides
    @Singleton
    fun provideEstadoTributarioDataSource(apiService: EstadoTributarioService): EstadoTributarioRemoteDataSource {
        return EstadoTributarioRemoteDataSourceImpl(apiService = apiService)
    }

    @Provides
    @Singleton
    fun provideEstadoTributarioRepository(remoteDataSource: EstadoTributarioRemoteDataSource): EstadoTributarioRepository {
        return EstadoTributarioRepositoryImpl(remoteDataSource)
    }
}