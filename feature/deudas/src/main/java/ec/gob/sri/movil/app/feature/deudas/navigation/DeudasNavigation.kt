package ec.gob.sri.movil.app.feature.deudas.navigation

import androidx.navigation3.runtime.NavKey
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasQuery
import kotlinx.serialization.Serializable

@Serializable
data class DeudasResultadosScreen(
    val query: DeudasQuery
) : NavKey