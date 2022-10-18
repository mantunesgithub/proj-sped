package br.com.mantunes.sped.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putLong
import android.system.Os.remove
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    var clienteIdLogin: Long = 0L
    private val viewModelLogin: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        clienteIdLogin = buscaLoginDoCliente()
        clienteIdLogin = viewModelLogin.buscaLoginDoCliente()
        if (clienteIdLogin < 0) {
            vaiParaLogin()
        }

        tentaCarregarDestino()?.let {
//            intent.removeExtra(CHAVE_DESTINO)
            if (it == "categoria") { vaiParaListaCategoria(clienteIdLogin) }
            if (it == "carrinho") {
                Log.i("CarrinhoGo", "onCreate: {MainActivity}")
                vaiParaListaCarrinho(clienteIdLogin) }
        }
        if (savedInstanceState == null) {
            val listaCategoriasFragment: ListaCategoriasFragment by inject()
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

            is ListaCategoriasFragment -> {
                configuraListaCategoria(fragment)
            }
            is ListaProdutosFragment -> {
                configuraListaProdutos(fragment)
            }
            is DetalhesProdutoFragment -> {
                configuraDetalheDoProduto(fragment)
            }
            is AdicionaNoCarrinhoFragment -> {
                configuraAdicionaNoCarrinho(fragment)
            }
            is ListaCarrinhoFragment -> {
                configuraListaCarrinho(fragment)
            }
            is AtualizaMaisUmNoCarrinhoFragment -> {
                configuraAtualizaMaisUmNoCarrinho(fragment)
            }
        }
    }

    private fun configuraAtualizaMaisUmNoCarrinho(fragment: AtualizaMaisUmNoCarrinhoFragment) {
        fragment.apply {
            quandoAtualizaMaisUm = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
            quandoDeletaCarrinho = {
                vaiParaListaCarrinho(clienteIdLogin)
            }
        }
    }

    private fun configuraListaCarrinho(fragment: ListaCarrinhoFragment) {
        fragment.apply {
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

    private fun configuraAdicionaNoCarrinho(fragment: AdicionaNoCarrinhoFragment) {
        fragment.apply {
            quandoSalvaCarrinho = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
            quandoClienteNaoLogado = ::vaiParaLogin
        }
    }

    private fun configuraDetalheDoProduto(fragment: DetalhesProdutoFragment) {
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
                val detalhesProdutoFragment: DetalhesProdutoFragment by inject()
                val argumentos = bundleOf(
                    CHAVE_CLIENTE_ARGS to clienteIdLogin,
                    CHAVE_PRODUTO_ID_ARGS to produtoSelecionado.id
                )
                detalhesProdutoFragment.arguments = argumentos
                transacaoFragment {
                    addToBackStack("R.id.produtos")
                    replace(R.id.container, detalhesProdutoFragment)
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

    private fun configuraListaCategoria(fragment: ListaCategoriasFragment) {
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
            quandoClienteSaiDoApp = ::vaiParaLogin
        }
    }

    fun vaiParaListaCategoria(clienteIdLogin: Long) {
        val listaCategoriasFragment: ListaCategoriasFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        listaCategoriasFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack("R.id.categoria")
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
        val listaCarrinhoFragment: ListaCarrinhoFragment by inject()
        val argumentos = bundleOf(CHAVE_CLIENTE_ARGS to clienteIdLogin)
        listaCarrinhoFragment.arguments = argumentos
        transacaoFragment {
            supportFragmentManager.popBackStack(R.id.produtos, 0)
            replace(R.id.container, listaCarrinhoFragment)
        }
    }

    private fun irParaAdicionaNoCarrinhoFragment(produtoAdicionado: Produto) {
        val adicionaNoCarrinhoFragment: AdicionaNoCarrinhoFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin,
            CHAVE_PRODUTO_ARGS to produtoAdicionado
        )
        adicionaNoCarrinhoFragment.arguments = argumentos
        transacaoFragment {
            replace(R.id.container, adicionaNoCarrinhoFragment)
        }
    }

    fun preparaAtualizacaoCarrinho(carrinho: Carrinho, tipoOperacao: String) {
        val carrinhoOper = CarrinhoOper(carrinhoOper = carrinho, tipoOper = tipoOperacao)
        val atualizaMaisUmNoCarrinhoFragment: AtualizaMaisUmNoCarrinhoFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin,
            CHAVE_CARRINHO_OPER to carrinhoOper
        )
        atualizaMaisUmNoCarrinhoFragment.arguments = argumentos
        transacaoFragment {
            replace(R.id.container, atualizaMaisUmNoCarrinhoFragment)
        }
    }

    fun vaiParaLogin() {
//        val prefs = getSharedPreferences(FILE_PREFERENCE, Context.MODE_PRIVATE)
//        prefs.edit().remove(CHAVE_LOGIN_CLIENTE).commit();
        viewModelLogin.removeIdDoCliente()
        Log.i("TAG", "vaiParaLogin: Logim removido ")
        vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
        }
    }

//    fun buscaLoginDoCliente(): Long {
//        val sp = getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE)
//        val clienteIdLogin: Long = sp.getLong(CHAVE_LOGIN_CLIENTE, -1)
//        return preferences.getLong(CHAVE_LOGIN_CLIENTE,-1)
//        return clienteIdLogin
//    }

    private fun tentaCarregarDestino(): String? {
        val destino = intent.getStringExtra(CHAVE_DESTINO)
        return destino
    }
}
