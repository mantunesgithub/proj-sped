package br.com.mantunes.sped.ui.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.mantunes.sped.model.*
import br.com.mantunes.sped.ui.activity.*
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

abstract class ClienteBaseLogadoFragment : Fragment() {

    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteIdLogado) }
    var clienteLogado: Cliente? = null
    var clienteIdLogado: Long = 0
    var produtoIdSelecionado: Long = 0
    var produtoSelecionado: Produto? = null
    var categoriaIdSelecionada: Long = 0
    var chaveCarrinhoSelecionado: Carrinho? = null
    var chaveCarrinhoOper: CarrinhoOper? = null
    var quandoClienteNaoLogado: () -> Unit = {}

    fun buscaClienteLogado(): Cliente? {
        verificaIdDoClienteLogado()

        viewModel.buscaPorId(clienteIdLogado).observe(this, Observer {clienteObserver->
            clienteObserver?.let { cliente ->
                clienteLogado = cliente
            }
        })
        return clienteLogado
    }

    private fun verificaIdDoClienteLogado() {

        clienteIdLogado = requireArguments().getLong(CHAVE_CLIENTE_ARGS)
        produtoIdSelecionado = requireArguments().getLong(CHAVE_PRODUTO_ID_ARGS)
        produtoSelecionado = requireArguments().getParcelable<Produto>(CHAVE_PRODUTO_ARGS)
        categoriaIdSelecionada = requireArguments().getLong(CHAVE_CATEGORIA_ID_ARGS)
        chaveCarrinhoSelecionado = requireArguments().getParcelable<Carrinho>(CHAVE_CARRINHO)
        chaveCarrinhoOper = requireArguments().getParcelable<CarrinhoOper>(CHAVE_CARRINHO_OPER)

        if (clienteIdLogado > 0) {
            Log.i("{ClienteBaseLogadoFrag}", "verificaIdDoClienteLogado: $clienteIdLogado id" )
        } else {
            quandoClienteNaoLogado()
        }
    }
}