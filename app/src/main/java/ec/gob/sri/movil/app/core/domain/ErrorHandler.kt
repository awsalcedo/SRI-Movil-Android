package ec.gob.sri.movil.app.core.domain

import android.content.Context
import ec.gob.sri.movil.app.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    
    fun getErrorMessage(context: Context, dataError: DataError): String {
        return when (dataError) {
            is DataError.Network.NoInternet -> context.getString(R.string.error_no_internet)
            is DataError.Network.Timeout -> context.getString(R.string.error_network)
            is DataError.Network.Unknown -> context.getString(R.string.error_unknown)
            is DataError.Local.Database -> context.getString(R.string.error_unknown)
            is DataError.Local.Unknown -> context.getString(R.string.error_unknown)
        }
    }
}
