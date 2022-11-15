package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.formatParaMoedaBrasileira
import br.com.mantunes.sped.extensions.tentaCarregarImagem
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.ui.viewmodel.DetalhesProdutoViewModel
import kotlinx.android.synthetic.main.detalhes_produto.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ProdutoDetalhesFragment : ClienteBaseLogadoFragment() {

    private val viewModel: DetalhesProdutoViewModel by
                viewModel { parametersOf(produtoIdSelecionado) }
    var quandoProdutoAdiciona: (produto: Produto) -> Unit = {}
    var quandoDetalheProdutoFab: (View) -> Unit = {}
    var quandoClienteSaiDoApp: ()-> Unit = {}
    var quandoClienteVaiParaHome: ()-> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.detalhes_produto,
            container,
            false
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        buscaClienteLogado()
        buscaProduto()
        configuraBotaoAdicionaCarrinho()
        configuraFabCarrinho()
        activity?.title = TITULO_APPBAR_DETALHES_DO_PRODUTO
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

    private fun configuraBotaoAdicionaCarrinho() {
        adicionar_carrinho.setOnClickListener {
            viewModel.produtoEncontrado.value?.let(quandoProdutoAdiciona)
        }
    }
    private fun buscaProduto() {
        viewModel.produtoEncontrado.observe(viewLifecycleOwner, Observer {
            it?.let { produto ->
                detalhes_produto_nome.text = produto.nome
                detalhes_produto_descricao.text = produto.descricao
                detalhes_produto_preco.text = produto.preco.formatParaMoedaBrasileira()
                detalhe_produto_imagem.tentaCarregarImagem(produto.imagem)
                if (produto.imagem?.isEmpty() == true) {
                    detalhe_produto_imagem.visibility = View.GONE
                } else {
                    detalhe_produto_imagem.visibility = View.VISIBLE
                }
            }
        })
    }
    private fun configuraFabCarrinho() {
        detalhe_produto_fab.setOnClickListener {
            it?.let { fab -> quandoDetalheProdutoFab(fab)  }
        }
    }
}