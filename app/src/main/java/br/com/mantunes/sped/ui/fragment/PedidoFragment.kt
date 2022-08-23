package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.formatParaMoedaBrasileira
import br.com.mantunes.sped.extensions.stringToDate
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import br.com.mantunes.sped.ui.recyclerview.adapter.ItensDoPedidoAdapter
import br.com.mantunes.sped.ui.viewmodel.CarrinhoViewModel
import kotlinx.android.synthetic.main.lista_pedido.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal
import java.text.SimpleDateFormat

class PedidoFragment : ClienteBaseLogadoFragment() {

    private var totalCarrinho: BigDecimal = BigDecimal.ZERO
    private val viewModel: CarrinhoViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val adapter: ItensDoPedidoAdapter by inject()
    var quandoConfirmaPedido: (pedidoDTO: PedidoDTO?) -> Unit = {}
    var quandoBotaoVoltar: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.lista_pedido,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = TITULO_APPBAR_CONFIRA_PEDIDO
        buscaClienteLogado()
        configuraRecyclerView()
        buscaCarrinhoComItensPedido()
        configuraBotaoFinalizarPedido()
        configuraBotaoVoltar()
    }
    private fun configuraBotaoFinalizarPedido() {
        lista_pedido_botao_confirmar_pedido.setOnClickListener { confirmaPedido ->
            confirmaPedido?.let { quandoConfirmaPedido(pedidoDTO) }
        }
    }
    private fun configuraBotaoVoltar() {
        lista_pedido_botao_voltar.setOnClickListener { voltar ->
            voltar?.let {
                quandoBotaoVoltar() }
        }
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, VERTICAL)
        lista_pedido_recyclerView.addItemDecoration(divisor)
        lista_pedido_recyclerView.adapter = adapter
    }
    private fun buscaCarrinhoComItensPedido() {
        viewModel.buscaTodos(clienteIdLogado).observe(this,
            Observer { carrinhoEncontrado ->
                carrinhoEncontrado?.let { listaCarrinhoDoCliente ->
                    adapter.atualiza(listaCarrinhoDoCliente)
                    imprimeTotalCarrinho(listaCarrinhoDoCliente)
                    formataPedidoDTOcomCarrinhoCliente(listaCarrinhoDoCliente)
                    formataDadosdoPedido()
                }
            })
    }

    private fun formataPedidoDTOcomCarrinhoCliente(listaCarrinhoDoCliente: List<Carrinho>) {
        pedidoDTO?.listaCarrinhoDoCliente?.addAll(listaCarrinhoDoCliente)
    }

    private fun formataDadosdoPedido() {
        lista_pedido_logradouro.text = pedidoDTO?.enderecoEscolhido?.logradouro + ", " +
                pedidoDTO?.enderecoEscolhido?.numero
        lista_pedido_compl_bairro_cep.text =
            pedidoDTO?.enderecoEscolhido?.complemento + " " + pedidoDTO?.enderecoEscolhido?.bairro +
                    " , cep " + pedidoDTO?.enderecoEscolhido?.cep
        lista_pedido_cidade_estado.text = pedidoDTO?.enderecoEscolhido?.cidade + ", " +
                pedidoDTO?.enderecoEscolhido?.estado + ", " +pedidoDTO?.enderecoEscolhido?.pais

        lista_pedido_nome.text = pedidoDTO?.clienteLogado?.nome
        lista_pedido_email.text = pedidoDTO?.clienteLogado?.email

        if (pedidoDTO?.tipoPagamento == TIPO_PAGAMENTO.CARTAO) {
            lista_pedido_tipo_pagamento.text = "Pagamento com Cartão Crédito"
            lista_pedido_forma_pagamento.text = "Quantidade de Parcelas: ${pedidoDTO?.numeroParcelasCartao}"
        }
        if (pedidoDTO?.tipoPagamento == TIPO_PAGAMENTO.PIX) {
            lista_pedido_tipo_pagamento.text = "Pagamento com Pix"
            lista_pedido_forma_pagamento.text = "A Vista"
        }
        if (pedidoDTO?.tipoPagamento == TIPO_PAGAMENTO.BOLETO) {
            lista_pedido_tipo_pagamento.text = "Pagamento com Boleto Bancario"
            val dataPedidoConvertida = pedidoDTO?.dataVencimentoBoleto?.
                                                stringToDate("yyyy-MM-dd")
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val dataVencimento = sdf.format(dataPedidoConvertida)
            lista_pedido_forma_pagamento.text = "Vencimento do Boleto : $dataVencimento"
        }
    }

    fun calculaSubTotal(valor: BigDecimal?, quantidade: Int): BigDecimal {
        var valorDouble: Double? = valor?.toDouble()
        return valorDouble?.let {
            BigDecimal(it * quantidade)
        } ?: BigDecimal.ZERO
    }

    private fun imprimeTotalCarrinho(listaCarrinhoCliente: List<Carrinho>) {
        for (lista in listaCarrinhoCliente) {
            totalCarrinho += calculaSubTotal(lista.preco, lista.quantidade)
        }
        val totalTexto = TOTAL_CARRINHO +
                totalCarrinho.formatParaMoedaBrasileira()
        lista_pedido_valor_total.text = totalTexto
    }
}
