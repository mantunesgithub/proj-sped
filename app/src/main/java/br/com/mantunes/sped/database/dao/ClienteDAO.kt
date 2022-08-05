package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.mantunes.sped.model.Cliente

@Dao
interface ClienteDAO {
    @Insert
    fun salva(cliente: Cliente)

    @Query("SELECT * FROM Cliente WHERE email = :email AND senha = :senha")
    fun autentica(email: String, senha: String): LiveData<Cliente>

    @Query("SELECT * FROM Cliente WHERE id = :ClienteId")
    fun buscaPorId(ClienteId: Long): LiveData<Cliente>

}