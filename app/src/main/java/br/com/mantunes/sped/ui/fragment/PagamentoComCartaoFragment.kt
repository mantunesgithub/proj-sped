package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import kotlinx.android.synthetic.main.pagamento_com_cartao.*

class PagamentoComCartaoFragment : ClienteBaseLogadoFragment() {

    private var parcelas: String = ""
    var quandoPagamentoComCartao: (pedidoDTOComCartao: PedidoDTO?) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.pagamento_com_cartao,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = TITULO_APPBAR_FORMA_PAGAMENTO
        buscaClienteLogado()
        configuraParcelasCartao()
        configuraBotaoProximo()
    }

    private fun configuraBotaoProximo() {
        Log.i("entrou", "configuraBotaoProximo: ")
        pagamento_cartao_botao_proximo.setOnClickListener {
            it?.let {
                Log.i("clicou", "configuraBotaoProximo: ")
                validaDadosDoCartao().let {resultadoValidacaoCartao->
                    if (resultadoValidacaoCartao == "ok" ) {
                        Log.i("ok", "configuraBotaoProximo: ")
                        atualizaPedidoDTO()
                        quandoPagamentoComCartao(pedidoDTO)
                    }
                }
            }
        }
    }

    private fun validaDadosDoCartao(): String {
        Log.i("entrou", "validaDadosDoCartao: ")
        var dadosDoCartaoEstaValido = "ok"

        if (pagamento_cartao_numero_cartao.text.toString() == null ||
            pagamento_cartao_numero_cartao.text.toString() == "") {
            mostraMsg("Preencher numero do cartao")
            dadosDoCartaoEstaValido = "erro"
        }
        if (pagamento_cartao_data_validade.text.toString() == null ||
            pagamento_cartao_data_validade.text.toString() == "") {
            mostraMsg("Preencher data validade do cartao")
            dadosDoCartaoEstaValido = "erro"
        }
        if (pagamento_cartao_cvc.text.toString() == null ||
            pagamento_cartao_cvc.text.toString() == "") {
            mostraMsg("Preencher cvc do cartao")
            dadosDoCartaoEstaValido = "erro"
        }
        return dadosDoCartaoEstaValido
    }

    private fun atualizaPedidoDTO() {
        pedidoDTO?.numeroCartao = pagamento_cartao_numero_cartao.text.toString().toLong()
        pedidoDTO?.cvcCartao = pagamento_cartao_cvc.text.toString().toInt()
        pedidoDTO?.dataValidadeCartao = pagamento_cartao_data_validade.text.toString()
        pedidoDTO?.numeroParcelasCartao = parcelas.toInt()
        pedidoDTO?.tipoPagamento = TIPO_PAGAMENTO.CARTAO
    }

    private fun configuraParcelasCartao() {
        val spinner = view?.findViewById<Spinner>(R.id.pagamento_cartao_spinner_parcelas)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity().applicationContext,
            R.array.values_array_parcelas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            if (spinner != null) {
                spinner.adapter = adapter
            }
        }
        if (spinner != null) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    parcelas = parent.getItemAtPosition(pos) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }
    }

    fun printPedidoDTO() {
        Log.i(
            "Print Pedido ", "atualizaPedidoDTO: Pagto Com Cartao Credito" +
                    "parcelas: ${pedidoDTO?.numeroParcelasCartao} " +
                    "nr cartao: ${pedidoDTO?.numeroCartao} " +
                    "cvc: ${pedidoDTO?.cvcCartao} " +
                    "tipo: ${pedidoDTO?.tipoPagamento} " +
                    "endereco: ${pedidoDTO?.enderecoEscolhido} " +
                    "cliente: ${pedidoDTO?.clienteLogado} " +
                    "Carrnho: ${pedidoDTO?.listaCarrinhoDoCliente} " +
                    "ch pix: ${pedidoDTO?.chavePix} " +
                    "dataValidade: ${pedidoDTO?.dataValidadeCartao} "
        )
    }
}
