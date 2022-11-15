package br.com.mantunes.sped.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.ui.activity.LoginActivity
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import kotlinx.android.synthetic.main.cliente_cadastro_inicial_form.*
import kotlinx.android.synthetic.main.login.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClienteCadastroInicialFragment : Fragment() {

    private val clienteId: Long = 0
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteId) }
    private val contextVaiPara by lazy {
        requireContext().applicationContext
            ?: throw IllegalArgumentException(PROBLEMA_EXTRAIR_CONTEXTO)
    }
    var quandoEfetuaCadastroInicial: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.cliente_cadastro_inicial_form,
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
            if (criaClienteNovo() ) {
                quandoEfetuaCadastroInicial()
            }
        }
    }

    private fun criaClienteNovo() :Boolean {
        if ( ! temErroClienteCadastroInicialForm() ) {
            val nome = formulario_cadastro_nome.text.toString()
            val senha = formulario_cadastro_senha.text.toString()
            val cpfCnpj = formulario_cadastro_cpfCnpj.text.toString()
            val tipoPessoa = formulario_cadastro_tipoPessoa.text.toString()
            val email = formulario_cadastro_email.text.toString()

            salva(Cliente(clienteId, nome, tipoPessoa, cpfCnpj, email, senha,"","",
                "","","","","","","",null))
            return true
        }
        return false
    }

    private fun temErroClienteCadastroInicialForm(): Boolean {
        var error = false
        if (formulario_cadastro_nome.text.isNullOrBlank()) {
            textInputlayout_nome.error = "Digite seu Nome!"
            error = true
        } else {
            textInputlayout_nome.error = ""
        }

        if (formulario_cadastro_tipoPessoa.text.isNullOrBlank()) {
            textInputlayout_tipoPessoa.error = "Digite tipo de pessoa PF/PJ!"
            error = true
        } else {
            textInputlayout_tipoPessoa.error = ""
        }

        if (formulario_cadastro_tipoPessoa.text.toString() != "PF" &&
            formulario_cadastro_tipoPessoa.text.toString() != "PJ" ) {
            textInputlayout_tipoPessoa.error = "Digite tipo de pessoa PF/PJ!"
            error = true
        } else {
            textInputlayout_tipoPessoa.error = ""
        }

        if (formulario_cadastro_cpfCnpj.text.isNullOrBlank()) {
            textInputlayout_cpfCnpj.error = "Digite seu Cpf ou Cnpj!"
            error = true
        } else {
            textInputlayout_cpfCnpj.error = ""
        }

        if (formulario_cadastro_email.text.isNullOrBlank()) {
            textInputlayout_email.error = "Digite seu e-mail!"
            error = true
        } else {
            textInputlayout_email.error = ""
        }
        val email = formulario_cadastro_email.text.toString()
        if (!isValidString(email)) {
            textInputlayout_email.error = "Email inválido"
            error = true
        } else {
            textInputlayout_email.error = ""
        }

        if (formulario_cadastro_senha.text.isNullOrBlank()) {
            textInputlayout_cadastro_senha.error = "Digite uma senha!"
            error = true
        } else {
            textInputlayout_cadastro_senha.error = ""
        }
        val senha = formulario_cadastro_senha.text.toString()
        val senhaTam = senha.length
        if (senhaTam < 2  ) {
            textInputlayout_cadastro_senha.error = "Senha deve conter no minimo 2 caracteres."
            error = true
        } else {
            textInputlayout_cadastro_senha.error = ""
        }
        return error
    }
    fun isValidString(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun salva(cliente: Cliente) {
        viewModel.salva(cliente).observe(viewLifecycleOwner, Observer {
            it?.dado?.let {
                it
            }
        })
    }
//
//    private fun vaiParaLogin() {
//        contextVaiPara.vaiPara(LoginActivity::class.java) {
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//    }
}
