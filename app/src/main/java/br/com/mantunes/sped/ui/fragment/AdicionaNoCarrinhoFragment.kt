package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.repository.Resource
import br.com.mantunes.sped.ui.viewmodel.CarrinhoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AdicionaNoCarrinhoFragment : ClienteBaseLogadoFragment() {

    private val viewModel: CarrinhoViewModel by viewModel { parametersOf(clienteIdCarrinho) }
    var quandoSalvaCarrinho: (idCarrinho: Long) -> Unit = {}
    private var clienteIdCarrinho: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaClienteLogado()
        clienteIdCarrinho = clienteIdLogado
        produtoSelecionado?.let { produto ->
            adicionaProdutoNoCarrinho(produto)
        } ?: throw IllegalArgumentException(PRODUTO_INVALIDO)
    }

    fun adicionaProdutoNoCarrinho(produto: Produto) {
        geraCarrinho(produto)?.let { carrinho ->
            this.salva(carrinho)
        } ?: Toast.makeText(
            context, FALHA_AO_CRIAR_CARRINHO, Toast.LENGTH_LONG
        ).show()
    }

    private fun geraCarrinho(produto: Produto): Carrinho? {
        try {
            return Carrinho(
                idCliente = clienteIdCarrinho,
                produtoId = produto.id,
                nome = produto.nome,
                preco = produto.preco,
                quantidade = 1
            )
        } catch (e: NumberFormatException) {
            return null
        }
    }

    private fun salva(carrinho: Carrinho) {
        viewModel.inclui(carrinho)
            .observe(this, Observer {
                it?.dado?.let {
                     quandoSalvaCarrinho(it)
                }
            })
        quandoSalvaCarrinho(carrinho.idCliente)
    }

    private fun carrinhoDuplicado(it: Resource<Long>) {
        (quandoSalvaCarrinho)
    }
}