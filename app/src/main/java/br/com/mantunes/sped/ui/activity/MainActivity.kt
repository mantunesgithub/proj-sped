package br.com.mantunes.sped.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.CarrinhoOper
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.ui.fragment.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    var clienteIdLogin: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        clienteIdLogin = buscaLoginDoCliente()
        if (clienteIdLogin < 0) {
            vaiParaLogin()
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
        clienteIdLogin = buscaLoginDoCliente()

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
        fragment.quandoAtualizaMaisUm = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
        fragment.quandoDeletaCarrinho = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
    }

    private fun configuraListaCarrinho(fragment: ListaCarrinhoFragment) {
        fragment.quandoAdicionaCarrinho = { carrinho ->
            preparaAtualizacaoCarrinho(carrinho, ADICIONA_NO_CARRINHO)
        }
        fragment.quandoRemoveCarrinho = { carrinho ->
            preparaAtualizacaoCarrinho(carrinho, REMOVE_DO_CARRINHO)
        }
        fragment.quandoDeletaCarrinho = { carrinho ->
            preparaAtualizacaoCarrinho(carrinho, DELETE_DO_CARRINHO)
        }
        fragment.quandoContinuarComprando = { clienteIdLogin ->
            irParaListaCategoria(clienteIdLogin)
        }
        fragment.quandoClienteSaiDoApp = this::vaiParaLogin
    }

    private fun configuraAdicionaNoCarrinho(fragment: AdicionaNoCarrinhoFragment) {
        fragment.quandoSalvaCarrinho = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
        fragment.quandoClienteNaoLogado = this::vaiParaLogin
    }

    private fun configuraDetalheDoProduto(fragment: DetalhesProdutoFragment) {
        fragment.quandoProdutoAdiciona = this::irParaAdicionaNoCarrinhoFragment
        fragment.quandoDetalheProdutoFab = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
        fragment.quandoClienteSaiDoApp = this::vaiParaLogin
    }

    private fun configuraListaProdutos(fragment: ListaProdutosFragment) {
        fragment.quandoProdutoSelecionado = { produtoSelecionado ->
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
        fragment.quandoProdutoFab = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
        fragment.quandoClienteSaiDoApp = this::vaiParaLogin
    }

    private fun configuraListaCategoria(fragment: ListaCategoriasFragment) {
        fragment.quandoCategoriaSelecionado = { categoriaSelecionado ->
            irParaListaProdutos(categoriaSelecionado)
        }
        fragment.quandoCategoriaFab = {
            vaiParaListaCarrinho(clienteIdLogin)
        }
        fragment.quandoClienteNaoLogado = this::vaiParaLogin
        fragment.quandoClienteSaiDoApp = this::vaiParaLogin
    }

    fun irParaListaCategoria(clienteIdLogin: Long) {
        val listaCategoriasFragment: ListaCategoriasFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        listaCategoriasFragment.arguments = argumentos
        transacaoFragment {
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
//        val listaCat = supportFragmentManager.getBackStackEntryAt(0).name
//        supportFragmentManager.popBackStack(listaCat, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        println("Conteudo de = $listaCat")
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
        val prefs = getSharedPreferences(FILE_PREFERENCE, Context.MODE_PRIVATE)
        prefs.edit().remove(CHAVE_LOGIN_CLIENTE).commit();
        vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
        }
    }

    private fun buscaLoginDoCliente(): Long {
        val sp = getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE)
        val clienteIdLogin: Long = sp.getLong(CHAVE_LOGIN_CLIENTE, -1)
        return clienteIdLogin
    }
}
