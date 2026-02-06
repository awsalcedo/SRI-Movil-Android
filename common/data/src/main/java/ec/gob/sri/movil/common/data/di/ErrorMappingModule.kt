package ec.gob.sri.movil.common.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.common.data.error.DefaultHttpErrorMapper
import ec.gob.sri.movil.common.domain.error.HttpErrorMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorMappingModule {

    @Provides
    @Singleton
    fun provideHttpErrorMapper(): HttpErrorMapper = DefaultHttpErrorMapper()
}