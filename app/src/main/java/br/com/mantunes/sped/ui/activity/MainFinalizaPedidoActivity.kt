package br.com.mantunes.sped.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.model.*
import br.com.mantunes.sped.model.enum.ESTADO_PAGAMENTO
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import br.com.mantunes.sped.ui.fragment.*
import org.koin.android.ext.android.inject

class MainFinalizaPedidoActivity : AppCompatActivity() {
    var clienteIdLogin: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
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
            is FormaPagamentoFragment -> {
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
            is PagamentoDoPedidoConfirmadoFragment -> {
                configuraPagamentoDoPedidoConfirmadoFragment(fragment)
            }
            is PagamentoRegistradoFragment -> {
                fragment.quandoReiniciaCompras = {
                    Log.i("Reinicia", "onAttachFragment: ")
                    Intent(this, MainActivity::class.java).apply {
                        putExtra(CHAVE_DESTINO, "categoria")
                        startActivity(this)
                    }
                }
                fragment.quandoClienteSaiDoApp = ::vaiParaLogin
            }
        }
    }

    private fun pagamentoDoPedidoConfirmadoFragment(fragment: PedidoFragment) {
        fragment.quandoBotaoVoltar = {
            Intent(this, MainActivity::class.java).apply {
                putExtra(CHAVE_DESTINO, "categoria")
                startActivity(this)
            }

        }

        fragment.apply {
            quandoConfirmaPedido = { pedidoDTOPagamentoConfirmado ->
                configuraConfirmarPagamentoDoPedido(pedidoDTOPagamentoConfirmado)
            }
        }
    }

    private fun verificaFormaPagamento(fragment: FormaPagamentoFragment) {
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

    private fun configuraPagamentoDoPedidoConfirmadoFragment(fragment: PagamentoDoPedidoConfirmadoFragment) {
        fragment.apply {
            quandoPagamentoSalvo = { idPedidoRegistrado ->

                val pagamentoRegistradoFragment: PagamentoRegistradoFragment by inject()
                val argumentos = bundleOf(
                    CHAVE_CLIENTE_ARGS to clienteIdLogin,
                    CHAVE_PEDIDO_REGISTRADO_ID_ARGS to idPedidoRegistrado
                )
                pagamentoRegistradoFragment.arguments = argumentos
                transacaoFragment {
                    replace(R.id.container, pagamentoRegistradoFragment)
                }
            }
        }
    }

    fun configuraEnderecos(fragment: EnderecoFragment) {
        fragment.apply {
            quandoEnderecoSelecionado = { pedidoDTOComEndereco ->
                val formaPagamentoFragment: FormaPagamentoFragment by inject()
                val argumentos = bundleOf(
                    CHAVE_PEDIDODTO_ARGS to pedidoDTOComEndereco
                )
                formaPagamentoFragment.arguments = argumentos
                transacaoFragment {
                    addToBackStack(null)
                    replace(R.id.container, formaPagamentoFragment)
                }
            }
        }
    }

    fun configuraConfirmarPagamentoDoPedido(pedidoDTOPagamentoConfirmado: PedidoDTO?) {
        val confirmaPagamentoDoPedidoFragment: PagamentoDoPedidoConfirmadoFragment by inject()
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
            finish()
        }
    }

    private fun buscaLoginDoCliente(): Long {
        val sp = getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE)
        val clienteIdLogin: Long = sp.getLong(CHAVE_LOGIN_CLIENTE, -1)
        return clienteIdLogin
    }

    fun inicializaPedidoDTO(): PedidoDTO {
        val clienteLogado = Cliente(0L, "null", "", "", "", "")
        val endereco = Endereco(
            0L, "null", "", "", "", "", "",
            "", "", 0L
        )
        val listaCarrinho: MutableList<Carrinho> = mutableListOf()
        return PedidoDTO(
            TIPO_PAGAMENTO.PIX, 0L, "", 0, 0, "",
            "", clienteLogado, endereco, listaCarrinho
        )
    }
}
