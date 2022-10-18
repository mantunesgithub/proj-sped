package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import kotlinx.android.synthetic.main.pagamento_com_pix.*

class PagamentoComPixFragment : ClienteBaseLogadoFragment() {
    var quandoPagamentoComPix: (pedidoDTOComPix: PedidoDTO?) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.pagamento_com_pix,
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
        pagamento_pix_botao_proximo.setOnClickListener {
            it?.let {
                val preencheuPagamento = preencheChavePix()
                if (preencheuPagamento == 1) {
                    quandoPagamentoComPix(pedidoDTO)
                } else {
                    mostraMsg("Preencher pagamento pix escolhendo somente uma opção")
                }
            }
        }
    }

        private fun preencheChavePix(): Int {
        var preencheuChavePagamentoPix = 0

        if (pagamento_pix_numero_cpf_cnpj?.text.toString() != null) {
            if (pagamento_pix_numero_cpf_cnpj?.text.toString() != "") {
                pedidoDTO?.chavePix = pagamento_pix_numero_cpf_cnpj?.text.toString()
                preencheuChavePagamentoPix += 1
            }
        }
        if (pagamento_pix_celular?.text.toString() != null) {
            if (pagamento_pix_celular?.text.toString() != "") {
                pedidoDTO?.chavePix = pagamento_pix_celular?.text.toString()
                preencheuChavePagamentoPix += 1
            }
        }
        if (pagamento_pix_email?.text.toString() != null) {
            if (pagamento_pix_email?.text.toString() != "") {
                pedidoDTO?.chavePix = pagamento_pix_email?.text.toString()
                preencheuChavePagamentoPix += 1
            }
        }
        if (pagamento_pix_chave_aleatoria?.text.toString() != null) {
            if (pagamento_pix_chave_aleatoria?.text.toString() != "") {
                pedidoDTO?.chavePix = pagamento_pix_chave_aleatoria?.text.toString()
                preencheuChavePagamentoPix += 1
            }
        }
        return preencheuChavePagamentoPix
    }
}
