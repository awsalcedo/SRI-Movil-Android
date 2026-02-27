package ec.gob.sri.movil.app.feature.deudas.ui.consulta

import kotlinx.serialization.Serializable

@Serializable
    sealed interface DeudasQuery {

    @Serializable
    data class  PorIdentificacion(
        val contribuyenteType: ContribuyenteType,
        val idType: IdType,
        val identificacion: String,
        val tipoPersona: String
    ): DeudasQuery

    @Serializable
    data class PorDenominacion(
        val contribuyenteType: ContribuyenteType,
        val apellidos: String,
        val nombres: String,
        val razonSocial: String,
        val tipoPersona: String,
        val resultados: Int
    ): DeudasQuery
}