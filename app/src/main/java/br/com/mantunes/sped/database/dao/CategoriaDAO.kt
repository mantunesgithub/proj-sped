package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.mantunes.sped.model.Categoria

@Dao
interface CategoriaDAO {

    @Query("SELECT * FROM Categoria")
    fun buscaTodos(): LiveData<List<Categoria>>

    @Insert(onConflict = REPLACE)
    fun salva(categoria: Categoria)

    @Insert(onConflict = REPLACE)
    fun salva(categorias: List<Categoria>)
}
