package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mantunes.sped.database.dao.CategoriaDAO
import br.com.mantunes.sped.model.CEP
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.webclient.CategoriaWebClient
import br.com.mantunes.sped.webclient.EnderecoWebViacep

class EnderecoViacepRepository(
    private val enderecoWebViacep: EnderecoWebViacep
) {
    private val enderecoViacepCash = MutableLiveData<Resource<CEP>?>()

    fun buscaPorCEP(cepEndereco: String): LiveData<Resource<CEP>?> {
        enderecoWebViacep.buscaEnderecoViacep(cepEndereco,
            quandoSucesso = {
                enderecoViacepCash.value = Resource(dado = it)
            }, quandoFalha = {
                val resourceAtual = enderecoViacepCash.value
                val resourceCriado: Resource<CEP>? =
                    if (resourceAtual != null) {
                        Resource(dado = resourceAtual.dado, erro = it)
                    } else {
                        Resource(dado = null, erro = it)
                    }
                enderecoViacepCash.value = resourceCriado
            })
        return enderecoViacepCash
    }
}

