package br.com.mantunes.sped.webclient

import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.webclient.services.ProdutoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUISICAO_NAO_SUCEDIDA = "Requisição não sucedida"
private const val TAG = "ProdutoWebClient"

class ProdutoWebClient(
    private val produtoService: ProdutoService = RetrofitInicializador()
        .produtoService
) {
    private fun <T> executaRequisicao(
        call: Call<T>,
        quandoSucesso: (produtosNovos: T?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    quandoSucesso(response.body())
                } else {
                    quandoFalha(REQUISICAO_NAO_SUCEDIDA)
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                quandoFalha(t.message)
            }
        })
    }

    fun buscaTodas(
        categoriaId: Long,
        quandoSucesso: (produtosNovos: List<Produto>?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            produtoService.buscaTodas(categoriaId),
            quandoSucesso,
            quandoFalha
        )
    }

    fun salva(
        produto: Produto,
        quandoSucesso: (produtosNovos: Produto?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            produtoService.salva(produto),
            quandoSucesso,
            quandoFalha
        )
    }
}