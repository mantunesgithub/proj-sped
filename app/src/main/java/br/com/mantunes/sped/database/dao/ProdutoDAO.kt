package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.Produto

@Dao
interface ProdutoDAO {
    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long): LiveData<Produto>

    @Query("SELECT * FROM Produto WHERE categoriaId = :categoriaId")
    fun buscaTodos(categoriaId : Long): LiveData<List<Produto>>

    @Insert(onConflict = REPLACE)
    fun salva(produto: Produto)

    @Insert(onConflict = REPLACE)
    fun salva(produtos: List<Produto>)

}
