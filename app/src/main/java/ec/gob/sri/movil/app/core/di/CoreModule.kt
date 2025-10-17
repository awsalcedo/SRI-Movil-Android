package ec.gob.sri.movil.app.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.app.core.domain.error.ErrorHandler
import ec.gob.sri.movil.app.core.domain.error.ErrorHandlerImpl
import javax.inject.Singleton

/**
 * Core dependency injection module providing shared components
 * used across the entire application.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    
    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandlerImpl()
    }
}
