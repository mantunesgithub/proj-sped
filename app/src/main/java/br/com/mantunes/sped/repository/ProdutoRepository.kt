package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import br.com.mantunes.sped.database.dao.ProdutoDAO
import br.com.mantunes.sped.model.Produto

class ProdutoRepository(private val dao: ProdutoDAO) {

    fun buscaTodos(categoriaId: Long): LiveData<List<Produto>> = dao.buscaTodos(categoriaId)

    fun buscaPorId(id: Long): LiveData<Produto> = dao.buscaPorId(id)

}
