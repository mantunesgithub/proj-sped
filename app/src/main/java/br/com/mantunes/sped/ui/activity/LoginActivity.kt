package br.com.mantunes.sped.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.ui.fragment.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            val loginFragment: LoginFragment by inject()
            Log.i("TAG", "onCreate: Login passou savedInstance ")
            transacaoFragment {
                //addToBackStack("R.id.login")
                replace(R.id.login_container, loginFragment)
            }
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is LoginFragment -> {
                configuraLogin(fragment)
            }
            is ClienteCadastroInicialFragment -> {
                configuraCadastroInicial(fragment)
            }
        }
    }

    private fun configuraLogin(fragment: LoginFragment) {
        fragment.apply {
            quandoAutenticaOk = {
                vaiPara(MainActivityNavigationDrawer::class.java)
                { finish() }
            }
            quandoCadastroInicial = {
                val clienteCadastroInicialFragment: ClienteCadastroInicialFragment by inject()
                transacaoFragment {
                    addToBackStack(null)
                    replace(R.id.login_container, clienteCadastroInicialFragment)
                }
            }
        }
    }

    private fun configuraCadastroInicial(fragment: ClienteCadastroInicialFragment) {
        fragment.apply {
            quandoEfetuaCadastroInicial = ::vaiParaLogin
        }
    }

    fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
        }
    }
}