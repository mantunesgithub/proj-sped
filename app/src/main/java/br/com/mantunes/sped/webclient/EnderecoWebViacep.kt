package br.com.mantunes.sped.webclient

import br.com.mantunes.sped.model.CEP
import br.com.mantunes.sped.webclient.services.EnderecoViacepService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUISICAO_NAO_SUCEDIDA = "Requisição não sucedida"
private const val TAG = "EnderecoWebViacep"

class EnderecoWebViacep(
    private val enderecoViacepService: EnderecoViacepService = RetrofitInicializador()
        .enderecoViacepService
) {
    private fun <T> executaRequisicao(
        call: Call<T>,
        quandoSucesso: (enderecoEncontrado: T?) -> Unit,
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

    fun buscaEnderecoViacep(cep : String,
        quandoSucesso: (enderecoViacep: CEP?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            enderecoViacepService.getEnderecoByCEP(cep),
            quandoSucesso,
            quandoFalha
        )
    }
}