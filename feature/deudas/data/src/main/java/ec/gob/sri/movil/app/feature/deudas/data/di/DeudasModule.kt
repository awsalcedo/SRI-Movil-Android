package ec.gob.sri.movil.app.feature.deudas.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.app.feature.deudas.data.remote.datasource.DeudasRemoteDataSource
import ec.gob.sri.movil.app.feature.deudas.data.remote.datasource.DeudasRemoteDataSourceImpl
import ec.gob.sri.movil.app.feature.deudas.data.remote.service.DeudasService
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeudasModule {

    @Provides
    @Singleton
    fun provideDeudasDataSource(
        service: DeudasService,
        httpErrorMapper: HttpErrorMapper
    ): DeudasRemoteDataSource {
        return DeudasRemoteDataSourceImpl(service = service, httpErrorMapper = httpErrorMapper)
    }

    @Provides
    @Singleton
    fun provideDeudasService(retrofit: Retrofit): DeudasService =
        retrofit.create(DeudasService::class.java)
}