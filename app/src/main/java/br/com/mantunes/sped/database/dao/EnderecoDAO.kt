package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.model.Produto

@Dao
interface EnderecoDAO {
    @Query("SELECT * FROM Endereco WHERE clienteId = :clienteId")
    fun buscaTodos(clienteId : Long): LiveData<List<Endereco>>

    @Insert
    fun salva(vararg endereco: Endereco)

    @Query("SELECT * FROM Endereco WHERE id = :id")
    fun buscaPorId(id: Long): LiveData<Endereco>
}
