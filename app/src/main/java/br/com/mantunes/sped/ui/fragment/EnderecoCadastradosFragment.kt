package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.EnderecoViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.enderecos.endereco1_cardview
import kotlinx.android.synthetic.main.enderecos.endereco1_logradouro
import kotlinx.android.synthetic.main.enderecos.endereco2_cardview
import kotlinx.android.synthetic.main.enderecos.endereco2_logradouro
import kotlinx.android.synthetic.main.enderecos.endereco3_cardview
import kotlinx.android.synthetic.main.enderecos.endereco3_logradouro
import kotlinx.android.synthetic.main.enderecos_cadastrados.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EnderecoCadastradosFragment : Fragment() {
    private var atingiuLimiteDeEnderecos: Boolean = false
    private val viewModel: EnderecoViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val viewModelCliente: ClienteViewModel by viewModel { parametersOf(clienteIdLogado) }
    private var clienteIdLogado: Long = 0
    private val viewModelLogin: LoginViewModel by viewModel()
    private var enderecosDisponiveis: List<Endereco> = emptyList()
    private val controlador by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.enderecos_cadastrados,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaIdClienteLogado()
        configuraBotaoFAB()
//        activity?.title = TITULO_APPBAR_ENDERECO_ENTREGA
    }

    private fun buscaIdClienteLogado() {
        clienteIdLogado = viewModelLogin.buscaLoginDoCliente()
        Log.i(
            "TAG", "buscaIdClienteLogado: {EnderecoCadastradosFragment}" +
                    " clienteIdlogado = $clienteIdLogado"
        )
        viewModelCliente.buscaPorId(clienteIdLogado)
            .observe(viewLifecycleOwner, Observer { clienteObserver ->
                clienteObserver?.let { cliente ->
                    buscaEnderecos(cliente)
                    configuraEndereco()
                }
            })
    }

    private fun configuraBotaoFAB() {
        endereco_cadastrados_fab_btn.setOnClickListener {
            if (atingiuLimiteDeEnderecos) {
                mostraMsg(getString(R.string.limite_de_endereco))
            } else{
                val direcao = EnderecoCadastradosFragmentDirections.
                actionEnderecoCadastradosFragmentToEnderecoFormCadastroFragment(geraEnderecoVazio())
                controlador.navigate(direcao)
            }
        }
    }

    private fun configuraEndereco() {
        endereco1_cardview.setOnClickListener {
            it?.let { enderecoParaVisualizacao(enderecosDisponiveis[0]) }
        }
        endereco2_cardview.setOnClickListener {
            it?.let { enderecoParaVisualizacao(enderecosDisponiveis[1]) }
        }
        endereco3_cardview.setOnClickListener {
            it?.let { enderecoParaVisualizacao(enderecosDisponiveis[2]) }
        }
    }

    private fun enderecoParaVisualizacao(enderecoEscolhido: Endereco) {
        val direcao = EnderecoCadastradosFragmentDirections
            .actionEnderecoCadastradosFragmentToEnderecoVisualizaDetalheFragment(enderecoEscolhido)
        controlador.navigate(direcao)
    }

    private fun buscaEnderecos(cliente: Cliente) {
        viewModel.buscaTodos(cliente.id)
            .observe(viewLifecycleOwner, Observer { listaEnderecoEncontrada ->
                listaEnderecoEncontrada?.let { listaEndereco ->
                    atingiuLimiteDeEnderecos = verificaQtdeEndereco(listaEndereco)
                    enderecosDisponiveis = listaEndereco
                    endereco1_cardview.visibility = View.GONE
                    endereco2_cardview.visibility = View.GONE
                    endereco3_cardview.visibility = View.GONE
                    for (endereco in listaEndereco) {
                        val indice = listaEndereco.indexOf(endereco)
                        if (indice == 0) {
                            endereco1_cardview.visibility = View.VISIBLE
                            endereco1_logradouro.text = endereco.logradouro + ", " + endereco.numero
                        }
                        if (indice == 1) {
                            endereco2_cardview.visibility = View.VISIBLE
                            endereco2_logradouro.text = endereco.logradouro + ", " + endereco.numero
                        }
                        if (indice == 2) {
                            endereco3_cardview.visibility = View.VISIBLE
                            endereco3_logradouro.text = endereco.logradouro + ", " + endereco.numero
                        }
                    }
                }
            })
    }
    private fun verificaQtdeEndereco(listaEndereco: List<Endereco>) : Boolean {
        return listaEndereco.size >= 3
    }

    private fun geraEnderecoVazio(): Endereco {
        return Endereco(0L, "", "", "", "", "", "",
            "", "", "", 0L)
    }
}
