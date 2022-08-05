package br.com.mantunes.sped.ui.fragment
import android.os.Bundle
import androidx.lifecycle.Observer
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.CarrinhoOper
import br.com.mantunes.sped.ui.activity.ADICIONA_NO_CARRINHO
import br.com.mantunes.sped.ui.activity.DELETE_DO_CARRINHO
import br.com.mantunes.sped.ui.activity.REMOVE_DO_CARRINHO
import br.com.mantunes.sped.ui.viewmodel.CarrinhoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AtualizaMaisUmNoCarrinhoFragment : ClienteBaseLogadoFragment() {

    private val viewModel: CarrinhoViewModel by viewModel { parametersOf(clienteIdLogado) }
    var quandoAtualizaMaisUm: (idCarrinho: Long) -> Unit = {}
    var quandoDeletaCarrinho: () -> Unit = {}

    lateinit var carrinhoComTipoOperacao: CarrinhoOper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buscaClienteLogado()
        verificaTipoOperacao()
    }

    private fun verificaTipoOperacao() {
        chaveCarrinhoOper?.let {
            carrinhoComTipoOperacao = it
        } ?: throw IllegalArgumentException(CARRINHO_TIPO_OPERACAO_INVALIDA)

        var quantidade: Int = carrinhoComTipoOperacao.carrinhoOper.quantidade
        if (carrinhoComTipoOperacao.tipoOper == ADICIONA_NO_CARRINHO) {
            quantidade += 1
        }
        if (carrinhoComTipoOperacao.tipoOper == REMOVE_DO_CARRINHO) {
            quantidade -= 1
        }
        if (carrinhoComTipoOperacao.tipoOper == DELETE_DO_CARRINHO || quantidade == 0) {
            deletaCarrinho(carrinhoComTipoOperacao.carrinhoOper)
        } else {
            criaCarrinho(quantidade, carrinhoComTipoOperacao.carrinhoOper).
            let { carrinhoCriado ->
                atualiza(carrinhoCriado)
            }
        }
    }

    private fun criaCarrinho(quantidade: Int, carrinho: Carrinho): Carrinho {
        return Carrinho(
            carrinho.idCliente, carrinho.produtoId, carrinho.nome, carrinho.preco,
            quantidade
        )
    }

    private fun atualiza(carrinho: Carrinho) {
        viewModel.salva(carrinho)
            .observe(this, Observer {
                it?.dado?.let(quandoAtualizaMaisUm)
            })
    }

    private fun deletaCarrinho(carrinho: Carrinho) {
        viewModel.deleta(carrinho)
            .observe(this, Observer {idRetorno->
                idRetorno?.dado?.let{
                }
            })
        quandoDeletaCarrinho()
    }
}