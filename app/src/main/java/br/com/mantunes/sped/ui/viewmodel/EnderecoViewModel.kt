package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.repository.EnderecoRepository

class EnderecoViewModel(
    clienteId: Long,
    private val repository: EnderecoRepository
) : ViewModel() {

    fun buscaTodos(clienteId : Long): LiveData<List<Endereco>> = repository.buscaTodos(clienteId)

    fun buscaPorId(enderecoId: Long): LiveData<Endereco> = repository.buscaPorId(enderecoId)

    fun salva(endereco: Endereco) = repository.salva(endereco)

    fun remove(endereco: Endereco) = repository.remove(endereco)

}
