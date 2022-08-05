package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import br.com.mantunes.sped.database.dao.CategoriaDAO
import br.com.mantunes.sped.model.Categoria

class CategoriaRepository(private val daoCategoria: CategoriaDAO) {

    fun buscaTodos(): LiveData<List<Categoria>> = daoCategoria.buscaTodos()
}
