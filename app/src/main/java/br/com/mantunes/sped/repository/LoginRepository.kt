package br.com.mantunes.sped.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.ui.activity.CHAVE_LOGIN_CLIENTE

class LoginRepository(private val preferences: SharedPreferences) {
    fun salvaIdDoCliente(cliente: Cliente) {
        preferences.edit {
            putLong(CHAVE_LOGIN_CLIENTE, cliente.id).commit()
        }
    }
    fun removeIdDoCliente() {
        preferences.edit {
            remove(CHAVE_LOGIN_CLIENTE).commit()
        }
    }
    fun buscaLoginDoCliente(): Long {
//        val sp = getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE)
//        val clienteIdLogin: Long = sp.getLong(CHAVE_LOGIN_CLIENTE, -1)
        return preferences.getLong(CHAVE_LOGIN_CLIENTE, -1)
//        return clienteIdLogin
    }
}
