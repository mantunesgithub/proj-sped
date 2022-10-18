package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.repository.ClienteRepository

class ClienteViewModel(
    private val clienteId: Long,
    private val clienteRepository: ClienteRepository
) : ViewModel() {

    fun salva(cliente: Cliente) =
        clienteRepository.salva(cliente)

    fun buscaPorId(clienteId: Long): LiveData<Cliente> {
        return clienteRepository.buscaPorId(clienteId)
    }

    fun autentica(email: String, senha: String): LiveData<Cliente> {
        return clienteRepository.autentica(email, senha)
    }

    fun atualiza(cliente: Cliente) =
        clienteRepository.atualiza(cliente)
}
