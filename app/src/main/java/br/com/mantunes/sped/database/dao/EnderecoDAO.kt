package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.mantunes.sped.model.Endereco

@Dao
interface EnderecoDAO {
    @Query("SELECT * FROM Endereco WHERE clienteId = :clienteId")
    fun buscaTodos(clienteId : Long): LiveData<List<Endereco>>

    @Query("SELECT * FROM Endereco WHERE id = :id")
    fun buscaPorId(id: Long): LiveData<Endereco>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(endereco: Endereco) : Long

    @Delete
    fun remove(endereco: Endereco)
}
