package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.addDays
import br.com.mantunes.sped.extensions.dateToString
import br.com.mantunes.sped.extensions.getCurrentDateTime
import br.com.mantunes.sped.model.PedidoDTO
import kotlinx.android.synthetic.main.pagamento_com_boleto.*

class PagamentoComBoletoFragment : ClienteBaseLogadoFragment() {
    var quandoPagamentoComBoleto: (pedidoDTOComBoleto: PedidoDTO?) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.pagamento_com_boleto,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = TITULO_APPBAR_FORMA_PAGAMENTO
        buscaClienteLogado()
        preencheDadosDoBoleto()
        configuraBotaoProximo()
    }

    private fun preencheDadosDoBoleto() {
        var dataHoje = getCurrentDateTime()
        var dataHojeAdicionaDias = dataHoje.addDays(5)
        val dataAdicionadaConvertida =
            dataHojeAdicionaDias.dateToString("yyyy-MM-dd")

        pedidoDTO?.dataVencimentoBoleto = dataAdicionadaConvertida
        val dataVencimentoConvertida =
            dataHojeAdicionaDias.dateToString("dd/MM/yyyy")
        pagamento_com_boleto_data_vecimento.text = dataVencimentoConvertida
        val nomeEcpfCnpj = "${pedidoDTO?.clienteLogado?.nome} - " +
                "${pedidoDTO?.clienteLogado?.cnpjCpf}"
        pagamento_com_boleto_nome_cpfcnpj_cliente.text = nomeEcpfCnpj
    }
    private fun configuraBotaoProximo() {
        pagamento_com_boleto_botao_proximo.setOnClickListener {
            it?.let {
                quandoPagamentoComBoleto(pedidoDTO)
            }
        }
    }
}