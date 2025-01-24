package ec.gob.sri.movil.app.consultas.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import ec.gob.sri.movil.app.consultas.data.local.entity.ConsultasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultasDao {
    @Query("SELECT * FROM consultas")
    fun getConsultas(): Flow<List<ConsultasEntity>>

    @Query("SELECT COUNT(*) FROM consultas")
    fun getCountConsultas(): Int

    @Delete
    fun deleteConsultas()
}