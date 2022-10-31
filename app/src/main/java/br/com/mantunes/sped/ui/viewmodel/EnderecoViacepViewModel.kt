package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.CEP
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.repository.EnderecoViacepRepository
import br.com.mantunes.sped.repository.ProdutoRepository
import br.com.mantunes.sped.repository.Resource

class EnderecoViacepViewModel(private val repository: EnderecoViacepRepository): ViewModel() {

    fun buscaEnderecoPorCep(cepEndereco: String): LiveData<Resource<CEP>?> {
        return repository.buscaPorCEP(cepEndereco)
    }
}