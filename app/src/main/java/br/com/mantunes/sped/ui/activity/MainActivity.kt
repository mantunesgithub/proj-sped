package br.com.mantunes.sped.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.CarrinhoOper
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.ui.fragment.*
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    var clienteIdLogin: Long = 0L
    private val viewModelLogin: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clienteIdLogin = viewModelLogin.buscaLoginDoCliente()
        if (clienteIdLogin < 0) {
            vaiParaLogin()
        }

        tentaCarregarDestino()?.let {
            if (it == "categoria") { vaiParaListaCategoria(clienteIdLogin) }
            if (it == "carrinho") {
                Log.i("CarrinhoGo", "onCreate: {MainActivity}")
                vaiParaListaCarrinho(clienteIdLogin) }
        }
        if (savedInstanceState == null) {
            val listaCategoriasFragment: CategoriaListaFragment by inject()
            val argumentos = bundleOf(
                CHAVE_CLIENTE_ARGS to clienteIdLogin
            )
            listaCategoriasFragment.arguments = argumentos
            transacaoFragment {
                replace(R.id.container, listaCategoriasFragment)
            }
        }
    }
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        clienteIdLogin = viewModelLogin.buscaLoginDoCliente()

        when (fragment) {

            is CategoriaListaFragment -> {
                configuraListaCategoria(fragment)
            }
            is ListaProdutosFragment -> {
                configuraListaProdutos(fragment)
            }
            is ProdutoDetalhesFragment -> {
                configuraDetalheDoProduto(fragment)
            }
            is CarrinhoAdicionaProdutosFragment -> {
                configuraAdicionaNoCarrinho(fragment)
            }
            is CarrinhoListaFragment -> {
                configuraListaCarrinho(fragment)
            }
            is CarrinhoAtualizaMaisUmFragment -> {
                configuraAtualizaMaisUmNoCarrinho(fragment)
            }
        }
    }

    private fun configuraAtualizaMaisUmNoCarrinho(fragment: CarrinhoAtualizaMaisUmFragment) {
        fragment.apply {
            quandoAtualizaMaisUm = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
            quandoDeletaCarrinho = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
        }
    }

    private fun configuraListaCarrinho(listaFragment: CarrinhoListaFragment) {
        listaFragment.apply {
            quandoAdicionaCarrinho = { carrinho ->
                preparaAtualizacaoCarrinho(carrinho, ADICIONA_NO_CARRINHO)
            }
            quandoRemoveCarrinho = { carrinho ->
                preparaAtualizacaoCarrinho(carrinho, REMOVE_DO_CARRINHO)
            }
            quandoDeletaCarrinho = { carrinho ->
                preparaAtualizacaoCarrinho(carrinho, DELETE_DO_CARRINHO)
            }
            quandoContinuarComprando = { clienteIdLogin ->
                vaiParaListaCategoria(clienteIdLogin)
            }
            quandoFinalizaPedido = {clienteIdLogin ->
                vaiPara(MainFinalizaPedidoActivity::class.java)
            }
            quandoClienteVaiParaHome = {
                vaiPara(MainActivityNavigationDrawer::class.java) { finish() }
            }
            quandoClienteSaiDoApp = ::vaiParaLogin
        }
    }

    private fun configuraAdicionaNoCarrinho(fragment: CarrinhoAdicionaProdutosFragment) {
        fragment.apply {
            quandoSalvaCarrinho = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
            quandoClienteNaoLogado = ::vaiParaLogin
        }
    }

    private fun configuraDetalheDoProduto(fragment: ProdutoDetalhesFragment) {
        fragment.apply {
            quandoProdutoAdiciona = ::irParaAdicionaNoCarrinhoFragment
            quandoDetalheProdutoFab = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
            quandoClienteVaiParaHome = {
                vaiPara(MainActivityNavigationDrawer::class.java) { finish() }
            }
            quandoClienteSaiDoApp = ::vaiParaLogin
        }
    }

    private fun configuraListaProdutos(fragment: ListaProdutosFragment) {
        fragment.apply {
            quandoProdutoSelecionado = { produtoSelecionado ->
                val produtoDetalhesFragment: ProdutoDetalhesFragment by inject()
                val argumentos = bundleOf(
                    CHAVE_CLIENTE_ARGS to clienteIdLogin,
                    CHAVE_PRODUTO_ID_ARGS to produtoSelecionado.id
                )
                produtoDetalhesFragment.arguments = argumentos
                transacaoFragment {
                    addToBackStack("R.id.produtos")
                    replace(R.id.container, produtoDetalhesFragment)
                }
            }
            quandoProdutoFab = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
            quandoClienteVaiParaHome = {
                vaiPara(MainActivityNavigationDrawer::class.java) { finish() }
            }
            quandoClienteSaiDoApp = ::vaiParaLogin
        }
    }

    private fun configuraListaCategoria(fragment: CategoriaListaFragment) {
        fragment.apply {
            quandoCategoriaSelecionado = { categoriaSelecionado ->
                irParaListaProdutos(categoriaSelecionado)
            }
            quandoCategoriaFab = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
            quandoClienteVaiParaHome = {
                vaiPara(MainActivityNavigationDrawer::class.java) { finish() }
            }
            quandoClienteNaoLogado = ::vaiParaLogin
            quandoClienteSaiDoApp = {
                vaiParaLogin()
            }
        }
    }

    fun vaiParaListaCategoria(clienteIdLogin: Long) {
        val listaCategoriasFragment: CategoriaListaFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        listaCategoriasFragment.arguments = argumentos
        transacaoFragment {
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            //addToBackStack("R.id.categoria")
            replace(R.id.container, listaCategoriasFragment)
        }
    }

    fun irParaListaProdutos(categoriaSelecionado: Categoria) {
        val listaProdutoFragment: ListaProdutosFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin,
            CHAVE_CATEGORIA_ID_ARGS to categoriaSelecionado.id
        )
        listaProdutoFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack("R.id.produtos")
            replace(R.id.container, listaProdutoFragment)
        }
    }

    private fun vaiParaListaCarrinho(clienteIdLogin: Long) {
        val carrinhoListaFragment: CarrinhoListaFragment by inject()
        val argumentos = bundleOf(CHAVE_CLIENTE_ARGS to clienteIdLogin)
        carrinhoListaFragment.arguments = argumentos
        transacaoFragment {
            //supportFragmentManager.popBackStack(R.id.categorias, 0)
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            replace(R.id.container, carrinhoListaFragment)
        }
    }

    private fun irParaAdicionaNoCarrinhoFragment(produtoAdicionado: Produto) {
        val carrinhoAdicionaProdutosFragment: CarrinhoAdicionaProdutosFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin,
            CHAVE_PRODUTO_ARGS to produtoAdicionado
        )
        carrinhoAdicionaProdutosFragment.arguments = argumentos
        transacaoFragment {
            replace(R.id.container, carrinhoAdicionaProdutosFragment)
        }
    }

    fun preparaAtualizacaoCarrinho(carrinho: Carrinho, tipoOperacao: String) {
        val carrinhoOper = CarrinhoOper(carrinhoOper = carrinho, tipoOper = tipoOperacao)
        val carrinhoAtualizaMaisUmFragment: CarrinhoAtualizaMaisUmFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin,
            CHAVE_CARRINHO_OPER to carrinhoOper
        )
        carrinhoAtualizaMaisUmFragment.arguments = argumentos
        transacaoFragment {
            replace(R.id.container, carrinhoAtualizaMaisUmFragment)
        }
    }

    fun vaiParaLogin() {
        viewModelLogin.removeIdDoCliente()
        vaiPara(LoginActivity::class.java) {
            Log.i("TAG", "vaiParaLogin: popBack")
//            supportFragmentManager.popBackStack("R.id.login",0)
//            val fm: FragmentManager = supportFragmentManager
//            val count: Int = fm.getBackStackEntryCount()
//            Log.i("TAG", "vaiParaLogin: getBackStack count = $count" )
//            for (i in 0 until count) {
//                fm.popBackStackImmediate()
//                Log.i("TAG", "fez popBackStack count = $count" )
//            }
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
        }
    }
    private fun tentaCarregarDestino(): String? {
        val destino = intent.getStringExtra(CHAVE_DESTINO)
        return destino
    }

}
//        val prefs = getSharedPreferences(FILE_PREFERENCE, Context.MODE_PRIVATE)
//        prefs.edit().remove(CHAVE_LOGIN_CLIENTE).commit();
//    fun buscaLoginDoCliente(): Long {
//        val sp = getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE)
//        val clienteIdLogin: Long = sp.getLong(CHAVE_LOGIN_CLIENTE, -1)
//        return preferences.getLong(CHAVE_LOGIN_CLIENTE,-1)
//        return clienteIdLogin
//    }