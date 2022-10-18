package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mantunes.sped.database.dao.ClienteDAO
import br.com.mantunes.sped.model.Cliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ClienteRepository (private val daoCliente: ClienteDAO) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun salva(cliente: Cliente): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                daoCliente.salva(cliente)
                liveDate.postValue(Resource(cliente.id))
            }
        }
    }
    fun buscaPorId (clienteId: Long) : LiveData<Cliente> {
        return daoCliente.buscaPorId(clienteId)
    }
    fun  autentica (email: String, senha: String) : LiveData<Cliente> {
        return daoCliente.autentica(email, senha)
    }
    fun atualiza(cliente: Cliente): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                daoCliente.atualiza(cliente)
                liveDate.postValue(Resource(cliente.id))
            }
        }
    }

}