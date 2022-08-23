package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import br.com.mantunes.sped.MainActivity
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.ui.fragment.extensions.mostraErro
import br.com.mantunes.sped.ui.viewmodel.CarrinhoViewModel
import kotlinx.android.synthetic.main.pagamento_com_pix.*
import kotlinx.android.synthetic.main.pagamento_pedido_registrado.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PagamentoRegistradoFragment : ClienteBaseLogadoFragment() {

    private val viewModel: CarrinhoViewModel by viewModel { parametersOf(clienteIdLogado) }
    var quandoReiniciaCompras: () -> Unit = {}
    var quandoClienteSaiDoApp: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.pagamento_pedido_registrado,
            container,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaClienteLogado()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_lista_categoria, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lista_categoria -> {
                quandoClienteSaiDoApp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        var numeroPedido = "Número do pedido: $idPedidoRegistrado"
        pagamento_pedido_registrado_idPedido.text = numeroPedido
        excluiCarrinhoDoCliente()
        configuraBotaoContinuarComprando()
        activity?.title = TITULO_APPBAR_PEDIDO_REGISTRADO
    }

    private fun configuraBotaoContinuarComprando() {
        pagamento_pedido_registrado_botao_continuar.setOnClickListener {
            it?.let { quandoReiniciaCompras() }
        }
    }
    private fun excluiCarrinhoDoCliente() {
        viewModel.exclui(clienteIdLogado)
            .observe(this, Observer { idRetorno ->
                idRetorno?.dado?.let {
                }
            })
    }
}
