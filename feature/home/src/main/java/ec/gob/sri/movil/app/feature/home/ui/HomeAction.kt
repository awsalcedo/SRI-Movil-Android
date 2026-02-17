package ec.gob.sri.movil.app.feature.home.ui

sealed interface HomeAction {
    data object OnLoad : HomeAction
    data class OnHomeItemClick(val id: String) : HomeAction
    data class OnSearchChanged(val query: String) : HomeAction
}
