package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.mantunes.sped.asynctask.BaseAsyncTask
import br.com.mantunes.sped.database.dao.ProdutoDAO
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.webclient.ProdutoWebClient
class ProdutoRepository(
    private val daoProduto: ProdutoDAO,
    private val webClient: ProdutoWebClient
) {
    private val mediador = MediatorLiveData<Resource<List<Produto>?>>()

    fun buscaTodos(categoriaId: Long): LiveData<Resource<List<Produto>?>> {
        mediador.addSource(buscaInterno(categoriaId), Observer {
            mediador.value = Resource(dado = it)
        })
        val falhasDaWebApiLiveData = MutableLiveData<Resource<List<Produto>?>>()

        mediador.addSource(falhasDaWebApiLiveData) { resourceDeFalha ->
            val resourceAtual = mediador.value
            val resourceNovo: Resource<List<Produto>?> =
                if (resourceAtual != null) {
                    Resource(dado = resourceAtual.dado, erro = resourceDeFalha.erro)
                } else {
                    resourceDeFalha
                }
            mediador.value = resourceNovo
        }

        buscaNaApi(categoriaId
        ) { erro ->
            falhasDaWebApiLiveData.value = Resource(dado = null, erro = erro)
        }
        return mediador
    }

    private fun buscaNaApi(
        categoriaId: Long,
        quandoFalha: (erro: String?) -> Unit
    ) {
        webClient.buscaTodas(categoriaId ,
            quandoSucesso = { produtosNovos ->
                produtosNovos?.let {
                    println("Lista produtos novos ==  $produtosNovos")
                    adicionaCategoria(produtosNovos, categoriaId)
                    salvaInterno(produtosNovos)
                }
            }, quandoFalha = quandoFalha
        )
    }

    private fun adicionaCategoria(
        produtosNovos: List<Produto>?,
        categoriaId: Long
    ) : List<Produto>? {
        produtosNovos?.forEach {
            it.categoriaId = categoriaId
        }
        return produtosNovos
    }

    private fun buscaInterno(categoriaId: Long): LiveData<List<Produto>> {
        return daoProduto.buscaTodos(categoriaId)
    }

    private fun salvaInterno(
        produtos: List<Produto>
    ) {
        BaseAsyncTask(
            quandoExecuta = {
                daoProduto.salva(produtos)
            }, quandoFinaliza = {}
        ).execute()
    }

    private fun salvaInterno(
        produto: Produto,
        quandoSucesso: () -> Unit
    ) {
        BaseAsyncTask(quandoExecuta = {
            daoProduto.salva(produto)
        }, quandoFinaliza = {
            quandoSucesso()
        }).execute()
    }

    fun buscaPorId(id: Long): LiveData<Produto> = daoProduto.buscaPorId(id)
}
