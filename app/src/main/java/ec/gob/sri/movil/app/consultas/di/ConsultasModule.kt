package ec.gob.sri.movil.app.consultas.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.app.consultas.data.remote.error.ConsultasErrorHandlerImpl
import ec.gob.sri.movil.app.core.domain.error.ErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConsultasModule {

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler {
        return ConsultasErrorHandlerImpl()
    }
}