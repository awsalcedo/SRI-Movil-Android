package ec.gob.sri.movil.app.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper
import ec.gob.sri.movil.common.data.error.DefaultHttpErrorMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorMappingModule {

    @Provides
    @Singleton
    fun provideHttpErrorMapper(): HttpErrorMapper = DefaultHttpErrorMapper()
}