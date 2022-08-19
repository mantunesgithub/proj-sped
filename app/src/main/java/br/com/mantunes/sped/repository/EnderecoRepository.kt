package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import br.com.mantunes.sped.database.dao.EnderecoDAO
import br.com.mantunes.sped.database.dao.ProdutoDAO
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.model.Produto

class EnderecoRepository(private val dao: EnderecoDAO) {
    fun buscaTodos(clienteId: Long): LiveData<List<Endereco>> = dao.buscaTodos(clienteId)

    fun buscaPorId(id: Long): LiveData<Endereco> = dao.buscaPorId(id)

}
