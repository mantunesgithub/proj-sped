package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mantunes.sped.database.dao.ItensDoPedidoDAO
import br.com.mantunes.sped.model.ItensDoPedido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ItensDoPedidoRepository(private val dao: ItensDoPedidoDAO) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun salva(itensDoPedido: ItensDoPedido): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                val idItensDoPedido = dao.salva(itensDoPedido)
                liveDate.postValue(Resource(idItensDoPedido))
            }
        }
    }
}
