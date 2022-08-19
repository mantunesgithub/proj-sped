package br.com.mantunes.sped.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mantunes.sped.database.dao.PedidoDAO
import br.com.mantunes.sped.model.Pedido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PedidoRepository(private val dao: PedidoDAO) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun salva(pedido: Pedido): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                val idPedido = dao.salva(pedido)
                liveDate.postValue(Resource(idPedido))
            }
        }
    }
}
