package br.com.mantunes.sped.webclient

import android.util.Log
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.webclient.services.CategoriaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUISICAO_NAO_SUCEDIDA = "Requisição não sucedida"
private const val TAG = "CategoriaWebClient"

class CategoriaWebClient(
    private val categoriaService: CategoriaService = RetrofitInicializador()
        .categoriaService
) {
    private fun <T> executaRequisicao(
        call: Call<T>,
        quandoSucesso: (categoriasNovas: T?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        Log.i("PWC", "executaRequisicaoCategoria: antes call")
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                Log.i("PWC", "executaRequisicaoCategoria: onresponse")
                if (response.isSuccessful) {
                    Log.i("PWC", "executaRequisicaoCategoria: reqok")
                    quandoSucesso(response.body())
                } else {
                    Log.i("PWC", "executaRequisicaoCategoria: reqnaook")
                    quandoFalha(REQUISICAO_NAO_SUCEDIDA)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.i("PWC", "executaRequisicaoCategoria: onfalha")
                quandoFalha(t.message)
            }
        })
    }

    fun buscaTodas(
        quandoSucesso: (categoriaNovas: List<Categoria>?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            categoriaService.buscaTodas(),
            quandoSucesso,
            quandoFalha
        )
    }

    fun salva(
        categoria: Categoria,
        quandoSucesso: (categoriaNovas: Categoria?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            categoriaService.salva(categoria),
            quandoSucesso,
            quandoFalha
        )
    }
}