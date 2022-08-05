package br.com.mantunes.sped.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.mantunes.sped.model.Categoria

@Dao
interface CategoriaDAO {

    @Query("SELECT * FROM Categoria")
    fun buscaTodos(): LiveData<List<Categoria>>

    @Insert
    fun salva(vararg categoria: Categoria)

}
