package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.mantunes.sped.model.Cliente

@Dao
interface ClienteDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun salva (cliente: Cliente)

    @Update
    fun atualiza (cliente: Cliente)

    @Query("SELECT * FROM Cliente WHERE email = :email AND senha = :senha")
    fun autentica(email: String, senha: String): LiveData<Cliente>

    @Query("SELECT * FROM Cliente WHERE id = :ClienteId")
    fun buscaPorId(ClienteId: Long): LiveData<Cliente>

}