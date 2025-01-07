package ec.gob.sri.movil.app.consultas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultas")
data class ConsultasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idConsulta: String,
    val nombre: String,
    val posicion: Int,
    val habilitado: Boolean,
    val autenticada: Boolean
)
