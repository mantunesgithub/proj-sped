package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.mantunes.sped.asynctask.BaseAsyncTask
import br.com.mantunes.sped.database.dao.CategoriaDAO
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.webclient.CategoriaWebClient

class CategoriaRepository(
    private val daoCategoria: CategoriaDAO,
    private val webClient: CategoriaWebClient
) {
    private val mediador = MediatorLiveData<Resource<List<Categoria>?>>()

    fun buscaTodos(): LiveData<Resource<List<Categoria>?>> {

        mediador.addSource(buscaInterno(), Observer {
            mediador.value = Resource(dado = it)
        })

        val falhasDaWebApiLiveData = MutableLiveData<Resource<List<Categoria>?>>()

        mediador.addSource(falhasDaWebApiLiveData) { resourceDeFalha ->
            val resourceAtual = mediador.value
            val resourceNovo: Resource<List<Categoria>?> =
                if (resourceAtual != null) {
                    Resource(dado = resourceAtual.dado, erro = resourceDeFalha.erro)
                } else {
                    resourceDeFalha
                }
            mediador.value = resourceNovo
        }

        buscaNaApi(
            quandoFalha = { erro ->
                falhasDaWebApiLiveData.value = Resource(dado = null, erro = erro)
            })

        return mediador
    }

    private fun buscaNaApi(
        quandoFalha: (erro: String?) -> Unit
    ) {
        webClient.buscaTodas(
            quandoSucesso = { categoriasNovas ->
                categoriasNovas?.let {
                    salvaInterno(categoriasNovas)
                }
            }, quandoFalha = quandoFalha
        )
    }

    private fun buscaInterno(): LiveData<List<Categoria>> {
        return daoCategoria.buscaTodos()
    }

    private fun salvaInterno(
        categorias: List<Categoria>
    ) {
        BaseAsyncTask(
            quandoExecuta = {
                daoCategoria.salva(categorias)
            }, quandoFinaliza = {}
        ).execute()
    }
    private fun salvaInterno(
        categoria: Categoria,
        quandoSucesso: () -> Unit
    ) {
        BaseAsyncTask(quandoExecuta = {
            daoCategoria.salva(categoria)
        }, quandoFinaliza = {
            quandoSucesso()
        }).execute()
    }
}
