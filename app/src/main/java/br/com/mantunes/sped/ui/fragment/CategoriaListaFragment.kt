package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.recyclerview.adapter.CategoriaAdapter
import br.com.mantunes.sped.ui.viewmodel.CategoriaViewModel
import kotlinx.android.synthetic.main.lista_categoria.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
private const val MENSAGEM_FALHA_CARREGAR_CATEGORIAS =
        "Não foi possível carregar as novas categorias"

class CategoriaListaFragment : ClienteBaseLogadoFragment() {

    private val viewModel: CategoriaViewModel by viewModel()
    private val adapter: CategoriaAdapter by inject()
    var quandoCategoriaSelecionado: (categoria: Categoria) -> Unit = {}
    var quandoCategoriaFab: (View) -> Unit = {}
    var quandoClienteSaiDoApp: () -> Unit = {}
    var quandoClienteVaiParaHome: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        buscaClienteLogado()
        buscaCategoria()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.lista_categoria,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuraRecyclerView()
        configuraFabCarrinho()
        activity?.title = TITULO_APPBAR_CATEGORIAS
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_app_bar, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_app_sair -> {
                quandoClienteSaiDoApp()
            }
            R.id.menu_app_home -> {
                quandoClienteVaiParaHome()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        lista_categoria_recyclerview.addItemDecoration(divisor)
        lista_categoria_recyclerview.adapter = adapter

        adapter.onItemClickListener = {
            quandoCategoriaSelecionado(it)
        }
    }

    private fun configuraFabCarrinho() {
        lista_categoria_fab.setOnClickListener {
            it?.let { fab -> quandoCategoriaFab(fab) }
        }
    }

    private fun buscaCategoria() {
        viewModel.buscaTodos().observe(this, Observer { resource->
            resource.dado?.let {
                        adapter.atualiza(it) }
            resource.erro?.let {
                        mostraMsg(MENSAGEM_FALHA_CARREGAR_CATEGORIAS)
            }
        })
    }
}
