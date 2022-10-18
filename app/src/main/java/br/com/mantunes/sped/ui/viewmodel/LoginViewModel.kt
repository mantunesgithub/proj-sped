package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.repository.LoginRepository

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    fun removeIdDoCliente() {
        repository.removeIdDoCliente()
    }
    fun salvaIdDoCliente(cliente: Cliente) {
        repository.salvaIdDoCliente(cliente)
    }
    fun buscaLoginDoCliente(): Long {
        return repository.buscaLoginDoCliente()
    }
}
