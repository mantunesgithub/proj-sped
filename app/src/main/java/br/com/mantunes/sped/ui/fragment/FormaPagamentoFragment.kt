package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import kotlinx.android.synthetic.main.forma_pagamento.*

class FormaPagamentoFragment : ClienteBaseLogadoFragment() {
    var quandoSelecionaPagamento: (pedidoDTO: PedidoDTO?) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.forma_pagamento,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaClienteLogado()
        verificaOpcaoDePagamento()
        activity?.title = TITULO_APPBAR_FORMA_PAGAMENTO
    }

    private fun verificaOpcaoDePagamento() {
        // Get radio group selected item using on checked change listener
        forma_pagamento_radio_grupo.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = requireView().findViewById(checkedId)
                if (radio == forma_pagamento_pix_radio) {
                    obtemFormaPagamentoPedidoDTO(TIPO_PAGAMENTO.PIX)
                }
                if (radio == forma_pagamento_cartao_radio) {
                    obtemFormaPagamentoPedidoDTO(TIPO_PAGAMENTO.CARTAO)
                }
                if (radio == forma_pagamento_boleto_radio) {
                    obtemFormaPagamentoPedidoDTO(TIPO_PAGAMENTO.BOLETO)
                }
            })

    }
    private fun obtemFormaPagamentoPedidoDTO(tipoPagamento: TIPO_PAGAMENTO) {
        pedidoDTO?.tipoPagamento = tipoPagamento
        Log.i("TAG", "obtemFormaPagamentoPedidoDTO: tipo pagto= ${pedidoDTO?.tipoPagamento}")
        quandoSelecionaPagamento(pedidoDTO)
    }

//    fun onRadioButtonClicked(view: View) {
//        Log.i("TAG", "onRadioButtonClicked: entrou ")
//        if (view is RadioButton) {
//            Log.i("TAG", "onRadioButtonClicked: é radioButton ")
//            // Is the button now checked?
//            val checked = view.isChecked
//
//            // Check which radio button was clicked
//            when (view.getId()) {
//                R.id.forma_pagamento_cartao_radio ->
//                    if (checked) {
//                        Log.i("TAG", "onRadioButtonClicked: cartão ")
//                        quandoSelecionaPagamento("cartao")
//                    }
//                R.id.forma_pagamento_pix_radio ->
//                    if (checked) {
//                        quandoSelecionaPagamento("pix")
//                    }
//                R.id.forma_pagamento_boleto_radio ->
//                    if (checked) {
//                        quandoSelecionaPagamento("boleto")
//                    }
//            }
//        }
//    }
}
