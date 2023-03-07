package br.com.mantunes.sped.ui.fragment
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.mantunes.sped.R
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class LoginFragment : Fragment() {
    private val clienteId: Long = 0
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteId) }
    private val viewModelLogin: LoginViewModel by viewModel()
    var quandoAutenticaOk: ()-> Unit = {}
    var quandoCadastroInicial: ()-> Unit = {}
    private val contextVaiPara by lazy {
        requireContext().applicationContext
            ?: throw IllegalArgumentException(PROBLEMA_EXTRAIR_CONTEXTO)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.login,
            container,
            false
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true)
        configuraTeclado()
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraTeclado() {
        login_email.setOnFocusChangeListener(OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                (contextVaiPara!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    login_email.windowToken, 0
                )
            }
        })
        login_senha.setOnFocusChangeListener(OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                (contextVaiPara!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    login_senha.windowToken, 0)
            }
        })
    }

    private fun configuraBotaoEntrar() {
        login_entrar_btn.setOnClickListener {
            val email = login_email.text.toString()
            val senha = login_senha.text.toString()
            autentica(email, senha)
        }
    }

    private fun autentica(email: String, senha: String) {

        if ( ! temErroValidacao(email, senha) ) {
            viewModel.autentica(email, senha).observe(
                viewLifecycleOwner, Observer {
                    it?.let { clienteEncontrado ->
                        viewModelLogin.salvaIdDoCliente(clienteEncontrado)
                        quandoAutenticaOk()
                    } ?: mostraMsg("Email/Senha não encontrado")
                })
        }
    }
    fun configuraBotaoCadastrar() {
        login_cadastrar_btn.setOnClickListener {
            it?.let {
                quandoCadastroInicial()
            }
        }
    }

    private fun temErroValidacao(email: String, senha: String): Boolean {
        var error = false
        if (login_email.text.isNullOrBlank()) {
            login_email_txtinplay.error = "Digite seu Email!"
            error = true
        } else {
            login_email_txtinplay.error = ""
        }
        if (!isValidString(email)) {
            login_email_txtinplay.error = "Email inválido"
            error = true
        } else {
            login_email_txtinplay.error = ""
        }
        if (login_senha.text.isNullOrBlank()) {
            login_senha_txtinplay.error = "Digite uma senha!"
            error = true
        } else {
            login_senha_txtinplay.error = ""
        }
        val senha = login_senha.text.toString()
        val senhaTam = senha.length
        if (senhaTam < 2  ) {
            login_senha_txtinplay.error = "Senha deve conter no minimo 2 caracteres."
            error = true
        } else {
            login_senha_txtinplay.error = ""
        }
        return error
    }
     fun isValidString(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }
}


