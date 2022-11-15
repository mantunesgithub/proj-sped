package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.recyclerview.adapter.ProdutosAdapter
import br.com.mantunes.sped.ui.viewmodel.ProdutosViewModel
import kotlinx.android.synthetic.main.lista_produtos.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
private const val MENSAGEM_FALHA_CARREGAR_PRODUTOS =
        "Não foi possível carregar novos produtos"

class ListaProdutosFragment : ClienteBaseLogadoFragment() {
    private val viewModel: ProdutosViewModel by viewModel{ parametersOf(categoriaIdSelecionada) }
    private val adapter: ProdutosAdapter by inject()

    var quandoProdutoSelecionado: (produto: Produto) -> Unit = {}
    var quandoProdutoFab: (View)-> Unit = {}
    var quandoClienteSaiDoApp: ()-> Unit = {}
    var quandoClienteVaiParaHome: ()-> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        buscaClienteLogado()
        buscaProdutos()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_app_bar,menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_app_sair -> { quandoClienteSaiDoApp() }
            R.id.menu_app_home -> { quandoClienteVaiParaHome() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buscaProdutos() {
        viewModel.buscaTodos(categoriaIdSelecionada).
            observe(this, Observer { resource->
            resource.dado?.let { adapter.atualiza(it) }
            resource.erro?.let {
                mostraMsg(MENSAGEM_FALHA_CARREGAR_PRODUTOS)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.lista_produtos,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabCarrinho()
        activity?.title = TITULO_APPBAR_PRODUTOS
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, VERTICAL)
        lista_produtos_recyclerView.addItemDecoration(divisor)
        adapter.onItemClickListener = quandoProdutoSelecionado
        lista_produtos_recyclerView.adapter = adapter
    }
    private fun configuraFabCarrinho() {
        lista_produtos_fab.setOnClickListener {
            it?.let { fab ->   quandoProdutoFab(fab)  }
        }
    }
}
