package ec.gob.sri.movil.app.feature.deudas.domain.usecase

import ec.gob.sri.movil.app.feature.deudas.domain.repository.DeudasRepository
import javax.inject.Inject

class ConsultarDeudasPorNombreUseCase @Inject constructor(private val repository: DeudasRepository) {
    suspend operator fun invoke(nombre: String, tipoPersona: String, resultados: Int) =
        repository.consultarPorNombre(
            nombre = nombre,
            tipoPersona = tipoPersona,
            resultados = resultados
        )
}