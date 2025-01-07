package ec.gob.sri.movil.app.consultas.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConsultasViewModel: ViewModel() {
    var state by mutableStateOf(ConsultasState())
        private set

    init {
        getConsultas()
    }

    private fun getConsultas() {
        TODO("Not yet implemented")
    }
}