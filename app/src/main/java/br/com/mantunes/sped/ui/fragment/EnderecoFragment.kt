package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.model.PedidoDTO
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.EnderecoViewModel
import kotlinx.android.synthetic.main.enderecos.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EnderecoFragment : ClienteBaseLogadoFragment() {

    private val viewModel: EnderecoViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val viewModelCliente: ClienteViewModel by viewModel { parametersOf(clienteIdLogado) }
    var quandoEnderecoSelecionado: (pedidoDTOComEndereco: PedidoDTO?) -> Unit = {}
    private var enderecosDisponiveis: List<Endereco> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.enderecos,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaClienteLogado()
        formataPedidoDTOcliente()
        activity?.title = TITULO_APPBAR_ENDERECO_ENTREGA
    }

    private fun formataPedidoDTOcliente() {
        viewModelCliente.buscaPorId(clienteIdLogado).
            observe(viewLifecycleOwner, Observer {clienteObserver->
            clienteObserver?.let { cliente ->
                pedidoDTO?.clienteLogado =  cliente
                buscaEnderecos()
                configuraEndereco()
            }
        })
    }

    private fun configuraEndereco() {
        endereco1_cardview.setOnClickListener {
            it?.let { formataPedidoDTOenderecoEscolhido(enderecosDisponiveis[0]) }
        }
        endereco2_cardview.setOnClickListener {
            it?.let { formataPedidoDTOenderecoEscolhido(enderecosDisponiveis[1]) }
        }
        endereco3_cardview.setOnClickListener {
            it?.let { formataPedidoDTOenderecoEscolhido(enderecosDisponiveis[2]) }
        }
    }

    private fun formataPedidoDTOenderecoEscolhido(enderecoEscolhido: Endereco) {
        pedidoDTO?.enderecoEscolhido = enderecoEscolhido
        quandoEnderecoSelecionado(pedidoDTO)
    }

    private fun buscaEnderecos() {
        viewModel.buscaTodos(clienteIdLogado).observe(viewLifecycleOwner, Observer { listaEnderecoEncontrada ->
            listaEnderecoEncontrada?.let { listaEndereco ->

                if (listaEndereco.isEmpty()) {
                    mostraMsg(" Endereço não cadastrado! Retornar p/ Home e cadastrar.")
                }
                enderecosDisponiveis = listaEndereco
                endereco1_cardview.visibility = View.GONE
                endereco2_cardview.visibility = View.GONE
                endereco3_cardview.visibility = View.GONE
                for (endereco in listaEndereco) {
                    val indice = listaEndereco.indexOf(endereco)
                    if (indice == 0) {
                        endereco1_cardview.visibility = View.VISIBLE
                        endereco1_logradouro.text = endereco.logradouro + ", " + endereco.numero
                        endereco1_compl_bairro_cep.text =
                            endereco.complemento + " " + endereco.bairro + " " + endereco.cep
                        endereco1_cidade_estado.text = endereco.cidade + ", " + endereco.pais
                    }
                    if (indice == 1) {
                        endereco2_cardview.visibility = View.VISIBLE
                        endereco2_logradouro.text = endereco.logradouro + ", " + endereco.numero
                        endereco2_compl_bairro_cep.text =
                            endereco.complemento + " " + endereco.bairro + " " + endereco.cep
                        endereco2_cidade_estado.text = endereco.cidade + ", " + endereco.pais
                    }
                    if (indice == 2) {
                        endereco3_cardview.visibility = View.VISIBLE
                        endereco3_logradouro.text = endereco.logradouro + ", " + endereco.numero
                        endereco3_compl_bairro_cep.text =
                            endereco.complemento + " " + endereco.bairro + " " + endereco.cep
                        endereco3_cidade_estado.text = endereco.cidade + ", " + endereco.pais
                    }
                }
            }
        } )
    }
}
