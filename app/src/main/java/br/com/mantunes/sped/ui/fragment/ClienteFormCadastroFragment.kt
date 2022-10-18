package br.com.mantunes.sped.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.ui.activity.LoginActivity
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import kotlinx.android.synthetic.main.form_cadastro_cliente.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClienteFormCadastroFragment : Fragment() {

    private val clienteId: Long = 0
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteId) }
    private val contextVaiPara by lazy {
        requireContext().applicationContext
            ?: throw IllegalArgumentException(PROBLEMA_EXTRAIR_CONTEXTO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = TITULO_APPBAR_CADASTRO

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.form_cadastro_cliente,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraBotaoCadastrar()
        activity?.title = TITULO_APPBAR_CADASTRO
    }

    private fun configuraBotaoCadastrar() {
        form_cadastro_cliente_botao_cadastrar.setOnClickListener {
            criaClienteNovo()
            vaiParaLogin()
        }
    }

    private fun criaClienteNovo() {
        val nome = formulario_cadastro_nome.text.toString()
        val senha = formulario_cadastro_senha.text.toString()
        val cpfCnpj = formulario_cadastro_cpfCnpj.text.toString()
        val tipoPessoa = formulario_cadastro_tipoPessoa.text.toString()
        val email = formulario_cadastro_email.text.toString()
        salva(Cliente(clienteId, nome, tipoPessoa, cpfCnpj, email, senha,"","",
        "","","","","","","",null))
    }

    private fun salva(cliente: Cliente) {
        viewModel.salva(cliente).observe(viewLifecycleOwner, Observer {
            it?.dado?.let {
                it
            }
        })
    }

    private fun vaiParaLogin() {
        contextVaiPara.vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
