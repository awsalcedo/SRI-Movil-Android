package ec.gob.sri.movil.app.estadotributario.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.gob.sri.movil.app.estadotributario.domain.usecase.ObtenerEstadoTributarioUseCase
import javax.inject.Inject

@HiltViewModel
class EstadoTributarioViewModel @Inject constructor(private val estadoTributarioUseCase: ObtenerEstadoTributarioUseCase) :
    ViewModel() {
}