package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import br.com.mantunes.sped.ui.fragment.extensions.mostraErro
import kotlinx.android.synthetic.main.forma_pagamento.*
import kotlinx.android.synthetic.main.pagamento_com_pix.*

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
        activity?.title = TITULO_APPBAR_FORMA_PAGAMENTO
        buscaClienteLogado()
        configuraBotaoProximo()

    }

    private fun configuraBotaoProximo() {
        forma_pagamento_botao_proximo.setOnClickListener {
            it?.let {
                var id: Int = forma_pagamento_radio_grupo.checkedRadioButtonId
                if (id != -1) {
                    val radio: RadioButton = requireView().findViewById(id)
                    if (radio == forma_pagamento_pix_radio) {
                        pedidoDTO?.tipoPagamento = TIPO_PAGAMENTO.PIX
                        quandoSelecionaPagamento(pedidoDTO)
                    }
                    if (radio == forma_pagamento_cartao_radio) {
                        pedidoDTO?.tipoPagamento = TIPO_PAGAMENTO.CARTAO
                        quandoSelecionaPagamento(pedidoDTO)
                    }
                    if (radio == forma_pagamento_boleto_radio) {
                        pedidoDTO?.tipoPagamento = TIPO_PAGAMENTO.BOLETO
                        quandoSelecionaPagamento(pedidoDTO)
                    }
                } else {
                    mostraErro("Escolher uma forma de pagamento")
                }
            }
        }
    }
}
