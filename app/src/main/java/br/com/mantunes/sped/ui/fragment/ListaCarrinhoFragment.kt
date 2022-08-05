package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.formatParaMoedaBrasileira
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.ui.recyclerview.adapter.CarrinhoAdapter
import br.com.mantunes.sped.ui.viewmodel.CarrinhoViewModel
import kotlinx.android.synthetic.main.lista_carrinho.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal
class ListaCarrinhoFragment : ClienteBaseLogadoFragment() {

    private var totalCarrinho: BigDecimal = BigDecimal.ZERO
    var quandoClienteSaiDoApp: ()-> Unit = {}
    private val viewModel: CarrinhoViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val adapter: CarrinhoAdapter by inject()

    var quandoProdutoSelecionado: (carrinho: Carrinho) -> Unit = {}
    var quandoAdicionaCarrinho: (carrinho: Carrinho) -> Unit = {}
    var quandoRemoveCarrinho: (carrinho: Carrinho) -> Unit = {}
    var quandoDeletaCarrinho: (carrinho: Carrinho) -> Unit = {}
    var quandoContinuarComprando: (clienteIdLogin : Long) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        buscaClienteLogado()
        buscaCarrinho()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_lista_categoria,menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lista_categoria -> { quandoClienteSaiDoApp() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buscaCarrinho() {
        viewModel.buscaTodos(clienteIdLogado).observe(this,
            Observer { carrinhoEncontrado ->

                carrinhoEncontrado?.let {listaCarrinhoCliente->
                    adapter.atualiza(listaCarrinhoCliente)
                    imprimeTotalCarrinho(listaCarrinhoCliente)
                    item_carrinho_botao_continuar.setOnClickListener { continuarComprando ->
                        continuarComprando?.let { quandoContinuarComprando(clienteIdLogado) }
                }
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.lista_carrinho,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        activity?.title = TITULO_APPBAR_CARRINHO
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, VERTICAL)
        lista_carrinho_recyclerView.addItemDecoration(divisor)
        adapter.onItemClickListener = quandoProdutoSelecionado
        adapter.onItemClickListenerAdicionado = quandoAdicionaCarrinho
        adapter.onItemClickListenerRemovido = quandoRemoveCarrinho
        adapter.onItemClickListenerDelete = quandoDeletaCarrinho
        lista_carrinho_recyclerView.adapter = adapter
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
        item_carrinho_valor_total.text = totalTexto
    }
}
