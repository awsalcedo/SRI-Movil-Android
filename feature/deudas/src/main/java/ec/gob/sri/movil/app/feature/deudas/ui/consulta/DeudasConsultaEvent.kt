package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import ec.gob.sri.movil.common.framework.ui.text.UiText

sealed interface DeudasConsultaEvent {
    data class OnError(val message: UiText) : DeudasConsultaEvent
    data class NavigateToResultados(val query: DeudasQuery) : DeudasConsultaEvent
}