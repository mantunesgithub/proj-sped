package br.com.mantunes.sped.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.model.*
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import br.com.mantunes.sped.ui.fragment.*
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainFinalizaPedidoActivity : AppCompatActivity() {
    var clienteIdLogin: Long = 0L
    private val viewModelLogin: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clienteIdLogin = buscaLoginDoCliente()
        if (clienteIdLogin < 0) {
            vaiParaLogin()
        }
        if (savedInstanceState == null) {

            var pedidoDTO = inicializaPedidoDTO()
            val enderecoFragment: EnderecoFragment by inject()
            val argumentos = bundleOf(
                CHAVE_CLIENTE_ARGS to clienteIdLogin,
                CHAVE_PEDIDODTO_ARGS to pedidoDTO
            )
            enderecoFragment.arguments = argumentos
            transacaoFragment {
                replace(R.id.container, enderecoFragment)
            }
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is EnderecoFragment -> {
                configuraEnderecos(fragment)
            }
            is PagamentoFormasFragment -> {
                verificaFormaPagamento(fragment)
            }
            is PagamentoComCartaoFragment -> {
                confirmaPagamentoCartaoFragment(fragment)
            }
            is PagamentoComPixFragment -> {
                confirmaPagamentoPixFragment(fragment)
            }
            is PagamentoComBoletoFragment -> {
                confirmaPagamentoBoletoFragment(fragment)
            }
            is PedidoFragment -> {
                Log.i("VoltarIs", "onAttachFragment: ")
                pagamentoDoPedidoConfirmadoFragment(fragment)
            }
            is PagamentoConfirmadoFragment -> {
                configuraPagamentoDoPedidoConfirmadoFragment(fragment)
            }
            is PagamentoRegistradoFragment -> {
                fragment.quandoReiniciaCompras = {
                    Log.i("Reinicia", "onAttachFragment: ")
                    vaiPara(MainActivityNavigationDrawer::class.java) {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        finish()
                    }
                }
                fragment.quandoClienteSaiDoApp = ::vaiParaLogin
            }
        }
    }


    private fun pagamentoDoPedidoConfirmadoFragment(fragment: PedidoFragment) {
        fragment.quandoBotaoVoltar = {
                vaiPara(MainActivityNavigationDrawer::class.java) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    finish()
            }
        }

        fragment.apply {
            quandoConfirmaPedido = { pedidoDTOPagamentoConfirmado ->
                configuraConfirmarPagamentoDoPedido(pedidoDTOPagamentoConfirmado)
            }
        }
    }

    private fun verificaFormaPagamento(fragment: PagamentoFormasFragment) {
        fragment.apply {
            quandoSelecionaPagamento = { pedidoDTOComFormaPagamento ->
                if (pedidoDTOComFormaPagamento?.tipoPagamento == TIPO_PAGAMENTO.CARTAO) {
                    configuraPagamentoComCartao(pedidoDTOComFormaPagamento)
                }
                if (pedidoDTOComFormaPagamento?.tipoPagamento == TIPO_PAGAMENTO.PIX) {
                    configuraPagamentoComPix(pedidoDTOComFormaPagamento)
                }
                if (pedidoDTOComFormaPagamento?.tipoPagamento == TIPO_PAGAMENTO.BOLETO) {
                    configuraPagamentoComBoleto(pedidoDTOComFormaPagamento)
                }
            }
        }
    }

    private fun confirmaPagamentoCartaoFragment(fragment: PagamentoComCartaoFragment) {
        fragment.apply {
            quandoPagamentoComCartao = { pedidoDTOcomCartao ->
                configuraPedidoFragment(pedidoDTOcomCartao)
            }
        }
    }

    private fun confirmaPagamentoPixFragment(fragment: PagamentoComPixFragment) {
        fragment.apply {
            quandoPagamentoComPix = { pedidoDTOcomPix ->
                configuraPedidoFragment(pedidoDTOcomPix)
            }
        }
    }

    private fun confirmaPagamentoBoletoFragment(fragment: PagamentoComBoletoFragment) {
        fragment.apply {
            quandoPagamentoComBoleto = { pedidoDTOcomBoleto ->
                configuraPedidoFragment(pedidoDTOcomBoleto)
            }
        }
    }

    private fun configuraPedidoFragment(pedidoDTOComPagamento: PedidoDTO?) {
        val pedidoFragment: PedidoFragment by inject()
        val argumentos = bundleOf(
            CHAVE_CLIENTE_ARGS to clienteIdLogin,
            CHAVE_PEDIDODTO_ARGS to pedidoDTOComPagamento
        )
        pedidoFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack(null)
            replace(R.id.container, pedidoFragment)
        }
    }

    private fun configuraPagamentoDoPedidoConfirmadoFragment(fragment: PagamentoConfirmadoFragment) {
        fragment.apply {
            quandoPagamentoSalvo = { idPedidoRegistrado ->

                val pagamentoRegistradoFragment: PagamentoRegistradoFragment by inject()
                val argumentos = bundleOf(
                    CHAVE_CLIENTE_ARGS to clienteIdLogin,
                    CHAVE_PEDIDO_REGISTRADO_ID_ARGS to idPedidoRegistrado
                )
                pagamentoRegistradoFragment.arguments = argumentos
                transacaoFragment {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.popBackStack()
                    replace(R.id.container, pagamentoRegistradoFragment)
                }
            }
        }
    }

    fun configuraEnderecos(fragment: EnderecoFragment) {
        fragment.apply {
            quandoEnderecoSelecionado = { pedidoDTOComEndereco ->
                val pagamentoFormasFragment: PagamentoFormasFragment by inject()
                val argumentos = bundleOf(
                    CHAVE_PEDIDODTO_ARGS to pedidoDTOComEndereco
                )
                pagamentoFormasFragment.arguments = argumentos
                transacaoFragment {
                    addToBackStack(null)
                    replace(R.id.container, pagamentoFormasFragment)
                }
            }
        }
    }

    fun configuraConfirmarPagamentoDoPedido(pedidoDTOPagamentoConfirmado: PedidoDTO?) {
        val confirmaPagamentoDoPedidoFragment: PagamentoConfirmadoFragment by inject()
        val argumentos = bundleOf(
            CHAVE_PEDIDODTO_ARGS to pedidoDTOPagamentoConfirmado,
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        confirmaPagamentoDoPedidoFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack(null)
            replace(R.id.container, confirmaPagamentoDoPedidoFragment)
        }
    }

    fun configuraPagamentoComCartao(pedidoDTOComFormaPagamento: PedidoDTO) {
        val pagamentoComCartaoFragment: PagamentoComCartaoFragment by inject()
        val argumentos = bundleOf(
            CHAVE_PEDIDODTO_ARGS to pedidoDTOComFormaPagamento,
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        pagamentoComCartaoFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack(null)
            replace(R.id.container, pagamentoComCartaoFragment)
        }
    }

    fun configuraPagamentoComPix(pedidoDTOComFormaPagamento: PedidoDTO) {
        val pagamentoComPixFragment: PagamentoComPixFragment by inject()
        val argumentos = bundleOf(
            CHAVE_PEDIDODTO_ARGS to pedidoDTOComFormaPagamento,
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        pagamentoComPixFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack(null)
            replace(R.id.container, pagamentoComPixFragment)
        }
    }

    fun configuraPagamentoComBoleto(pedidoDTOComFormaPagamento: PedidoDTO) {
        val pagamentoComBoletoFragment: PagamentoComBoletoFragment by inject()
        val argumentos = bundleOf(
            CHAVE_PEDIDODTO_ARGS to pedidoDTOComFormaPagamento,
            CHAVE_CLIENTE_ARGS to clienteIdLogin
        )
        pagamentoComBoletoFragment.arguments = argumentos
        transacaoFragment {
            addToBackStack(null)
            replace(R.id.container, pagamentoComBoletoFragment)
        }
    }

    fun vaiParaLogin() {
        val prefs = getSharedPreferences(FILE_PREFERENCE, Context.MODE_PRIVATE)
        prefs.edit().remove(CHAVE_LOGIN_CLIENTE).commit();
        vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
        }
    }

    private fun buscaLoginDoCliente(): Long {
        val clienteIdLogin: Long = viewModelLogin.buscaLoginDoCliente()
        return clienteIdLogin
    }

    fun inicializaPedidoDTO(): PedidoDTO {
        val clienteLogado = Cliente(
            0L, "null", "", "", "", "", "", "", "", "", "", "", "", "", "",
            ""
        )
        val endereco = Endereco(
            0L, "null", "", "", "", "", "",
            "", "", "", 0L,
        )
        val listaCarrinho: MutableList<Carrinho> = mutableListOf()
        return PedidoDTO(
            TIPO_PAGAMENTO.PIX, 0L, "", 0, 0, "",
            "", clienteLogado, endereco, listaCarrinho
        )
    }
}
//    override fun onBackPressed() {
//        Log.i("TAG", "onBackPressed: MainFinalizaPedidoActivity")
//        val fragment =
//            this.supportFragmentManager.findFragmentById(R.id.container)
//        (fragment as? OnBackPressListenerFragment.IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
//    }
//        val sp = getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE)
//        val clienteIdLogin: Long = sp.getLong(CHAVE_LOGIN_CLIENTE, -1)
