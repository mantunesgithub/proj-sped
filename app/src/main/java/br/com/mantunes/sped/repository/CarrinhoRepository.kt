package br.com.mantunes.sped.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mantunes.sped.database.dao.CarrinhoDAO
import br.com.mantunes.sped.model.Carrinho
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CarrinhoRepository (private val daoCarrinho: CarrinhoDAO) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun inclui(carrinho: Carrinho): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                try {
                    daoCarrinho.inclui(carrinho)
                    liveDate.postValue(Resource(carrinho.idCliente))
                } catch (e: Exception) {
                    Log.e("Carrinho Repository", "inclui: $e", )
                }
            }
        }
    }
    fun salva(carrinho: Carrinho): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                daoCarrinho.salva(carrinho)
                liveDate.postValue(Resource(carrinho.idCliente))
            }
        }
    }
    fun delete(carrinho: Carrinho): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                daoCarrinho.delete(carrinho)
            }
        }
    }
    fun exclui(clienteId: Long): LiveData<Resource<Void>> {
        return MutableLiveData<Resource<Void>>().also { liveDate ->
            scope.launch {
                daoCarrinho.exclui(clienteId)
            }
        }
    }
    fun buscaTodos (clienteId: Long) : LiveData<List<Carrinho>> {
        return daoCarrinho.buscaTodos(clienteId)
    }
}