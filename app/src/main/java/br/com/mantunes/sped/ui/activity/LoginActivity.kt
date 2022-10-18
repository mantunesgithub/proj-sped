package br.com.mantunes.sped.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.toast
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.ui.fragment.*
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class LoginActivity : AppCompatActivity() {
    private val clienteId: Long = 0
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteId) }
    private val viewModelLogin: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        supportActionBar?.hide()
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        login_botao_entrar.setOnClickListener {
            val email = login_email.text.toString()
            val senha = login_senha.text.toString()
            Log.i("{LoginActivity}", "configuraBotaoEntrar: $email / $senha")
            autentica(email, senha)
        }
    }

    private fun autentica(email: String, senha: String) {

        viewModel.autentica(email, senha).observe(
            this, Observer {
                it?.let { clienteEncontrado ->
//                    val prefs =
//                       getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE) // Declare xml file
//                    prefs.edit().putLong(CHAVE_LOGIN_CLIENTE, clienteEncontrado.id).apply()
                    viewModelLogin.salvaIdDoCliente(clienteEncontrado)
                    vaiPara(MainActivityNavigationDrawer::class.java) { finish() }

                } ?: toast("Cliente não encontrado - Cadastrar o Cliente")
            })
    }

    fun configuraBotaoCadastrar() {
        login_botao_cadastrar.setOnClickListener {
            it?.let {
                val clienteFormCadastroFragment: ClienteFormCadastroFragment by inject()
                setContentView(R.layout.activity_main)
                transacaoFragment {
                    replace(R.id.container, clienteFormCadastroFragment)
                }
            }
        }
    }
}


