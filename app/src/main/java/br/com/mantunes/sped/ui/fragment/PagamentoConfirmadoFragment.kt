package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import br.com.mantunes.sped.extensions.getCurrentDateTime
import br.com.mantunes.sped.extensions.toString
import br.com.mantunes.sped.model.*
import br.com.mantunes.sped.model.enum.ESTADO_PAGAMENTO
import br.com.mantunes.sped.ui.viewmodel.ItensDoPedidoViewModel
import br.com.mantunes.sped.ui.viewmodel.PagamentoViewModel
import br.com.mantunes.sped.ui.viewmodel.PedidoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class PagamentoConfirmadoFragment : ClienteBaseLogadoFragment() {
    private val viewModelPedido: PedidoViewModel by viewModel()
    private val viewModelItensDoPedido: ItensDoPedidoViewModel by viewModel()
    private val viewModelPagamento: PagamentoViewModel by viewModel()

    var quandoPedidoSalvo: (idPedido: Long) -> Unit = {}
    var quandoPagamentoSalvo: (idPedido: Long) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaClienteLogado()

        pedidoDTO?.let { pedidoDTO ->

            geraPedidoDoCliente(pedidoDTO)
            quandoPedidoSalvo = { idPedidoGerado ->
                idPedidoGerado.let { idPedido ->
                    pedidoDTO.listaCarrinhoDoCliente.forEach { carrinho ->
                        geraItensDoPedidoDoCliente(idPedido, carrinho)
                    }
                    geraPagamentoDoPedido(idPedido)
                    quandoPagamentoSalvo(idPedido)
                }
            }
        } ?: throw IllegalArgumentException(PEDIDO_INVALIDO)
    }

    fun geraPedidoDoCliente(pedidoDTO: PedidoDTO) {
        geraPedido(pedidoDTO)?.let { pedido ->
            this.salvaPedido(pedido)
        } ?: Toast.makeText(
            context, FALHA_AO_CRIAR_PEDIDO, Toast.LENGTH_LONG
        ).show()
    }

    private fun geraPedido(pedidoDTO: PedidoDTO): Pedido? {
        val dataHoje = getCurrentDateTime()
        val dataPedidoConvertida = dataHoje.toString("yyyy/MM/dd")
        try {
            return Pedido(
                id = 0L,
                idCliente = pedidoDTO.clienteLogado.id,
                dataPedido = dataPedidoConvertida,
                idEnderecoEntrega = pedidoDTO.enderecoEscolhido.id
            )
        } catch (e: NumberFormatException) {
            return null
        }
    }

    private fun salvaPedido(pedido: Pedido) {
        viewModelPedido.salva(pedido)
            .observe(this, Observer {
                it?.dado?.let {
                    quandoPedidoSalvo(it)
                }
            })
    }

    /*
        Gera Itens do Pedido do Cliente
     */
    fun geraItensDoPedidoDoCliente(idPedido: Long, carrinho: Carrinho) {
        geraItensDoPedido(idPedido, carrinho)?.let { itensDoPedido ->
            this.salvaItensDoPedido(itensDoPedido)
        } ?: Toast.makeText(
            context, FALHA_AO_CRIAR_ITENS_PEDIDO, Toast.LENGTH_LONG
        ).show()
    }

    private fun geraItensDoPedido(idPedido: Long, carrinho: Carrinho): ItensDoPedido? {
        try {
            return ItensDoPedido(
                id = 0L,
                idPedido = idPedido,
                idProduto = carrinho.produtoId,
                quantidade = carrinho.quantidade,
                desconto = BigDecimal.ZERO,
                preco = carrinho.preco,
                imagem = carrinho.imagem
            )
        } catch (e: NumberFormatException) {
            return null
        }
    }

    private fun salvaItensDoPedido(itensDoPedido: ItensDoPedido) {
        viewModelItensDoPedido.salva(itensDoPedido)
            .observe(this, Observer {
                it?.dado?.let {
                }
            })
    }
    /*
        Gera Pagamento do Pedido do Cliente
     */
    fun geraPagamentoDoPedido(idPedido: Long) {
        geraPagamento(idPedido)?.let { pagamento ->
            this.salvaPagamento(pagamento)
        } ?: Toast.makeText(
            context, FALHA_AO_CRIAR_PAGAMENTO, Toast.LENGTH_LONG
        ).show()
    }

    private fun geraPagamento(idPedido: Long): Pagamento? {
        lateinit var pagamento: Pagamento
        try {
            pedidoDTO?.let { pedidoDTO ->

                pagamento = Pagamento(
                    id = 0L,
                    idPedido = idPedido,
                    tipoPagamento = pedidoDTO.tipoPagamento,
                    chavePix = pedidoDTO.chavePix,
                    cvcCartao = pedidoDTO.cvcCartao,
                    dataValidadeCartao = pedidoDTO.dataValidadeCartao,
                    dataVencimentoBoleto = pedidoDTO.dataVencimentoBoleto,
                    estadoPagamento = ESTADO_PAGAMENTO.PENDENTE,
                    numeroCartao = pedidoDTO.numeroCartao,
                    numeroParcelasCartao = pedidoDTO.numeroParcelasCartao
                )
            }
        } catch (e: NumberFormatException) {
            return null
        }
        return pagamento
    }

    private fun salvaPagamento(pagamento: Pagamento) {
        viewModelPagamento.salva(pagamento)
            .observe(this, Observer {
                it?.dado?.let {
                }
            })
    }
}