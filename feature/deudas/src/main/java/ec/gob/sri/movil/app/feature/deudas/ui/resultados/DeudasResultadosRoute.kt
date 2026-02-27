package ec.gob.sri.movil.app.feature.deudas.ui.resultados

import androidx.compose.runtime.Composable
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.DeudasQuery
import ec.gob.sri.movil.app.feature.deudas.ui.detalle.DeudasDetalleRoute
import ec.gob.sri.movil.app.feature.deudas.ui.coincidencias.DeudasCoincidenciasRoute
import ec.gob.sri.movil.app.feature.deudas.ui.consulta.IdType

@Composable
fun DeudasResultadosRoute(
    query: DeudasQuery,
    onBack: () -> Unit,
    onNavigate: (DeudasQuery) -> Unit
) {
    when (query) {
        is DeudasQuery.PorIdentificacion -> {
            DeudasDetalleRoute(
                query = query,
                onBack = onBack
            )
        }

        is DeudasQuery.PorDenominacion -> {
            DeudasCoincidenciasRoute(
                query = query,
                onBack = onBack,
                onSelectedIdentificacion = { identificacionSeleccionada ->

                    val derivedIdType = when (identificacionSeleccionada.length) {
                        10 -> IdType.CEDULA
                        13 -> IdType.RUC
                        else -> IdType.RUC // fallback defensivo
                    }

                    // Re-usa la misma pantalla router, pero ahora en modo detalle
                    onNavigate(
                        DeudasQuery.PorIdentificacion(
                            contribuyenteType = query.contribuyenteType,
                            idType = derivedIdType, // o el que aplique; el detalle se basa en identificacion
                            identificacion = identificacionSeleccionada,
                            tipoPersona = query.tipoPersona
                        )
                    )
                }
            )
        }
    }
}