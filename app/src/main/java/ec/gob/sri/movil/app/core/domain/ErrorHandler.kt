package ec.gob.sri.movil.app.core.domain

import android.content.Context
import ec.gob.sri.movil.app.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    
    fun getErrorMessage(context: Context, dataError: DataError): String {
        return when (dataError) {
            // HTTP specific errors
            is DataError.Network.BadRequest -> context.getString(R.string.error_unknown)
            is DataError.Network.Unauthorized -> context.getString(R.string.error_unauthorized)
            is DataError.Network.Forbidden -> context.getString(R.string.error_forbidden)
            is DataError.Network.NotFound -> context.getString(R.string.error_not_found)
            is DataError.Network.Conflict -> context.getString(R.string.error_unknown)
            is DataError.Network.UnprocessableEntity -> context.getString(R.string.error_unknown)
            is DataError.Network.PayLoadTooLarge -> context.getString(R.string.error_unknown)
            is DataError.Network.RequestTimeout -> context.getString(R.string.error_network)
            is DataError.Network.TooManyRequests -> context.getString(R.string.error_unknown)
            is DataError.Network.ServerError -> context.getString(R.string.error_server)
            is DataError.Network.UnexpectedHttp -> context.getString(R.string.error_unknown)
            
            // Network connectivity errors
            is DataError.Network.NoInternet -> context.getString(R.string.error_no_internet)
            is DataError.Network.Timeout -> context.getString(R.string.error_network)
            is DataError.Network.Unknown -> context.getString(R.string.error_unknown)
            
            // Local errors
            is DataError.Local.Database -> context.getString(R.string.error_unknown)
            is DataError.Local.Unknown -> context.getString(R.string.error_unknown)
        }
    }
}
