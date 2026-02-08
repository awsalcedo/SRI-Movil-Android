package ec.gob.sri.movil.app.feature.deudas.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.app.feature.deudas.data.remote.datasource.DeudasRemoteDataSource
import ec.gob.sri.movil.app.feature.deudas.data.remote.repository.DeudasRepositoryImpl
import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeudasModule {

    @Provides
    @Singleton
    fun provideDeudasRepository(
        remoteDataSource: DeudasRemoteDataSource
    ): DeudasRepository {
        return DeudasRepositoryImpl(remoteDataSource)
    }
}