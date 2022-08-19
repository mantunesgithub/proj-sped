package br.com.mantunes.sped.ui.fragment

import android.graphics.Insets.add
import android.os.Bundle
import android.util.Log
import br.com.mantunes.sped.extensions.*
import br.com.mantunes.sped.model.PedidoDTO
import java.text.SimpleDateFormat
import java.time.Period
import java.util.*

class PagamentoComBoletoFragment : ClienteBaseLogadoFragment() {
    var quandoPagamentoComBoleto: (pedidoDTOComBoleto: PedidoDTO?) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaClienteLogado()

        var dataHoje = getCurrentDateTime()
        var dataHojeAdicionaDias = dataHoje.addDays(5)
        val dataAdicionadaConvertida =
            dataHojeAdicionaDias.dateToString("yyyy-MM-dd")
        pedidoDTO?.dataVencimentoBoleto = dataAdicionadaConvertida
        printPedidoDTO()
        quandoPagamentoComBoleto(pedidoDTO)
    }

    fun printPedidoDTO() {
        Log.i("Print Pedido Boleto ", "data vencimento ${pedidoDTO?.dataVencimentoBoleto}")
    }
}
