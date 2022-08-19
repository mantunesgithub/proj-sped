package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.repository.Resource

@Dao
interface CarrinhoDAO {

    @Query("SELECT * FROM Carrinho WHERE idCliente = :clienteId")
    fun buscaTodos(clienteId : Long): LiveData<List<Carrinho>>

    @Insert
    fun inclui(vararg carrinho: Carrinho)

    @Insert (onConflict = REPLACE)
    fun salva(vararg carrinho: Carrinho)

    @Delete
    fun delete(carrinho: Carrinho)

    @Query("DELETE FROM Carrinho WHERE idCliente = :clienteId")
    fun exclui(clienteId : Long)

}