package ec.gob.sri.movil.app.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.gob.sri.movil.app.config.DefaultApiConfig
import ec.gob.sri.movil.common.domain.config.ApiConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {
    @Provides
    @Singleton
    fun provideApiConfig(): ApiConfig = DefaultApiConfig()
}