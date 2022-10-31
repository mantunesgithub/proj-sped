package br.com.mantunes.sped.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.EnderecoFormCadastroBinding
import br.com.mantunes.sped.model.CEP
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.EnderecoViacepViewModel
import br.com.mantunes.sped.ui.viewmodel.EnderecoViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
private const val MENSAGEM_FALHA_CARREGAR_ENDERECO_VIACEP =
    "Não foi possível carregar endereço via CEP"

class EnderecoFormCadastroFragment : Fragment() {
    private val argumentos by navArgs<EnderecoFormCadastroFragmentArgs>()
    private val endereco by lazy { argumentos.endereco }
    private val controlador by lazy { findNavController() }
    private lateinit var _binding: EnderecoFormCadastroBinding
    private val viewModelEndereco: EnderecoViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val viewModelLogin: LoginViewModel by viewModel()
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteIdLogado) }
    private val viewModelEnderecoViacep: EnderecoViacepViewModel by viewModel()
    private var tipoEndereco: String = ""
    private var clienteIdLogado: Long = 0L
    private var clienteLogado: Cliente? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnderecoFormCadastroBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaClienteLogado()
        preencheFormulario()
        configuraTipoEndereco()
        buscaEnderecoViacep()
        configuraBotaoSalvar()
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
    private fun configuraBotaoSalvar() {
        _binding.enderecoFormCadastroBtnSalvar.setOnClickListener {
            clienteLogado?.let { clienteLogado ->
                Log.i("TAG", "criaEndereco: id logado = $it")
                criaEndereco(clienteLogado)?.let(this::salva) ?: mostraMsg(
                    getString(R.string.falha_ao_criar_endereco)
                )
            }
        }
    }

    private fun salva(endereco: Endereco) {
        Log.i("TAG", "atualiza endereco = : $endereco")
        viewModelEndereco.salva(endereco)
            .observe(viewLifecycleOwner, Observer {
                it?.dado?.let {
                    mostraMsg(getString(R.string.endereco_salvo_com_sucesso))
                    vaiParaEnderecosCadastrados()
                }
            })
    }

    private fun preencheFormulario() {
        if (endereco.id > 0L) {
            viewModelEndereco.buscaPorId(endereco.id).observe(viewLifecycleOwner,
                Observer { enderecoEncontrado ->
                    enderecoEncontrado?.let {
                        preencheFormularioEndereco(it)
                    }
                })
        }
    }

    private fun preencheFormularioEndereco(enderecoEncontrado: Endereco) {
        _binding.enderecoFormCadastroEndereco.setText(enderecoEncontrado.logradouro)
        _binding.enderecoFormCadastroNumero.setText(enderecoEncontrado.numero)
        _binding.enderecoFormCadastroComplemento.setText(enderecoEncontrado.complemento)
        _binding.enderecoFormCadastroBairro.setText(enderecoEncontrado.bairro)
        _binding.enderecoFormCadastroCidade.setText(enderecoEncontrado.cidade)
        _binding.enderecoFormCadastroEstado.setText(enderecoEncontrado.estado)
        _binding.enderecoFormCadastroPais.setText(enderecoEncontrado.pais)
        _binding.enderecoFormCadastroCep.setText(enderecoEncontrado.cep)
    }

    private fun criaEndereco(cliente: Cliente): Endereco? {
        val logradouro = _binding.enderecoFormCadastroEndereco.text.toString()
        val numero = _binding.enderecoFormCadastroNumero.text.toString()
        val complemento: String = _binding.enderecoFormCadastroComplemento.text.toString()
        val bairro: String = _binding.enderecoFormCadastroBairro.text.toString()
        val cidade: String = _binding.enderecoFormCadastroCidade.text.toString()
        val estado: String = _binding.enderecoFormCadastroEstado.text.toString()
        val pais: String = _binding.enderecoFormCadastroPais.text.toString()
        val cep: String = _binding.enderecoFormCadastroCep.text.toString()
        val clienteId: Long = cliente.id

        return geraEndereco(
            endereco.id, logradouro, numero, complemento, bairro, cep, cidade, estado, pais,
            tipoEndereco, clienteId
        )
    }

    private fun geraEndereco(
        id: Long,
        logradouro: String,
        numero: String,
        complemento: String,
        bairro: String,
        cep: String,
        cidade: String,
        estado: String,
        pais: String,
        tipo: String,
        clienteId: Long
    ): Endereco? = try {
        Endereco(
            id = id,
            logradouro = logradouro,
            numero = numero,
            complemento = complemento,
            bairro = bairro,
            cep = cep,
            cidade = cidade,
            estado = estado,
            pais = pais,
            tipo = tipo,
            clienteId = clienteId
        )
    } catch (e: NumberFormatException) {
        null
    }

    private fun vaiParaEnderecosCadastrados() {
        val direcao =
            EnderecoFormCadastroFragmentDirections.actionEnderecoFormCadastroFragmentToEnderecoCadastradosFragment()
        controlador.navigate(direcao)
//        controlador.navigate(
//            R.id.action_enderecoFormCadastroFragment_to_enderecoCadastradosFragment)
    }

    private fun configuraTipoEndereco() {
        val spinner = view?.findViewById<Spinner>(R.id.endereco_form_cadastro_spinner_tipo)
        ArrayAdapter.createFromResource(
            requireActivity().applicationContext,
            R.array.values_array_tipo_endereco,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            if (spinner != null) {
                spinner.adapter = adapter
            }
        }
        if (spinner != null) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    tipoEndereco = parent.getItemAtPosition(pos) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }
    private fun buscaEnderecoViacep() {
        _binding.enderecoFormBtnViacep.setOnClickListener {
            dialogParaObterCEP()
        }
   }

    private fun dialogParaObterCEP() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Endereço ViaCep")

// Set up the input
        val input = EditText(requireActivity().applicationContext)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Digite o cep do endereço")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener {
                dialog, which ->
// Here you get get input text from the Edittext
            var cepDigitado = input.text.toString()
            mostraMsg("Cep = $cepDigitado")
            viewModelEnderecoViacep.buscaEnderecoPorCep(cepDigitado).observe(viewLifecycleOwner,
                Observer { resource->
                    resource?.dado?.let {
                                carregarEnderecoViaCep(it) }
                    resource?.erro?.let {
                                mostraMsg(MENSAGEM_FALHA_CARREGAR_ENDERECO_VIACEP)}
                })
        })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun carregarEnderecoViaCep(it: CEP) {
        _binding.enderecoFormCadastroEndereco.setText(it.logradouro)
        _binding.enderecoFormCadastroComplemento.setText(it.complemento)
        _binding.enderecoFormCadastroBairro.setText(it.bairro)
        _binding.enderecoFormCadastroCidade.setText(it.localidade)
        _binding.enderecoFormCadastroEstado.setText(it.uf)
        _binding.enderecoFormCadastroCep.setText(it.cep)
    }
}
