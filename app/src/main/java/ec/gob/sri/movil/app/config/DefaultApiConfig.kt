package ec.gob.sri.movil.app.config

import ec.gob.sri.movil.app.BuildConfig
import ec.gob.sri.movil.common.domain.config.ApiConfig

class DefaultApiConfig : ApiConfig {
    override val baseUrl: String = BuildConfig.BASE_URL
    override val contextApi: String = BuildConfig.CONTEXT_API
}