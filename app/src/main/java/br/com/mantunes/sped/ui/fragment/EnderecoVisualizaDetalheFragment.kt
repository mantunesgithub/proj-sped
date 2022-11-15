package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.EnderecoVisualizacaoBinding
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.ui.activity.CHAVE_ENDERECO
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.EnderecoViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.endereco_visualizacao.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EnderecoVisualizaDetalheFragment : Fragment() {
    private val argumentos by navArgs<EnderecoVisualizaDetalheFragmentArgs>()
    private val endereco by lazy {argumentos.endereco}
    private val controlador by lazy { findNavController() }
    private lateinit var _binding: EnderecoVisualizacaoBinding
    private val viewModelEndereco: EnderecoViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val viewModelLogin: LoginViewModel by viewModel()
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteIdLogado) }
    private var clienteIdLogado: Long = 0L
    private var clienteLogado: Cliente? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnderecoVisualizacaoBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated:-{EnderecoFormCadastroBinding} chegou")
        buscaClienteLogado()
        preencheFormulario()
        configuraBotaoEditar()
        configuraBotaoRemover()
    }

    private fun buscaClienteLogado() {
        clienteIdLogado = viewModelLogin.buscaLoginDoCliente()
        Log.i(
            "TAG",
            "buscaClienteLogado: {ClienteFormCadastroPerfilPFFragment} clid= $clienteIdLogado"
        )
        clienteIdLogado.let {
            viewModel.buscaPorId(it)
                .observe(viewLifecycleOwner, Observer { clienteObserver ->
                    clienteObserver?.let { cliente ->
                        clienteLogado = cliente
                        Log.i("TAG", "buscaClienteLogado: $clienteLogado")
                    }
                })

        }
    }
    private fun preencheFormulario() {
        viewModelEndereco.buscaPorId(endereco.id).observe(viewLifecycleOwner,
            Observer { enderecoEncontrado ->
                enderecoEncontrado?.let {
                    preencheFormularioEndereco(it)
                }
            })
    }

    private fun preencheFormularioEndereco(enderecoEncontrado: Endereco) {
        _binding.enderecoVisualizacaoEndereco.setText(enderecoEncontrado.logradouro)
        _binding.enderecoVisualizacaoNumero.setText(enderecoEncontrado.numero)
        _binding.enderecoVisualizacaoComplemento.setText(enderecoEncontrado.complemento)
        _binding.enderecoVisualizacaoBairro.setText(enderecoEncontrado.bairro)
        _binding.enderecoVisualizacaoCidade.setText(enderecoEncontrado.cidade)
        _binding.enderecoVisualizacaoEstado.setText(enderecoEncontrado.estado)
        _binding.enderecoVisualizacaoPais.setText(enderecoEncontrado.pais)
        _binding.enderecoVisualizacaoCep.setText(enderecoEncontrado.cep)
        _binding.enderecoVisualizacaoTipo.setText(enderecoEncontrado.tipo)
    }

    private fun configuraBotaoEditar() {
        _binding.enderecoVisualizacaoBtnEditar.setOnClickListener {
            val direcao = EnderecoVisualizaDetalheFragmentDirections
                .actionEnderecoVisualizaDetalheFragmentToEnderecoFormCadastroFragment(endereco)
            controlador.navigate(direcao)
        }
    }

    private fun configuraBotaoRemover() {
        _binding.enderecoVisualizacaoBtnRemover.setOnClickListener {
            remove(endereco)
            vaiParaEnderecosCadastrados()
        }
    }

    private fun remove(endereco: Endereco) {
        Log.i("TAG", "atualiza cliente = : $endereco")
        viewModelEndereco.remove(endereco).observe(viewLifecycleOwner, Observer {
            it?.dado?.let {
                mostraMsg(getString(R.string.endereco_removido_ok))
            }
        })
    }

    private fun vaiParaEnderecosCadastrados() {
        val direcao =
            EnderecoVisualizaDetalheFragmentDirections.
                actionEnderecoVisualizaDetalheFragmentToEnderecoCadastradosFragment()
        controlador.navigate(direcao)
    }
}
